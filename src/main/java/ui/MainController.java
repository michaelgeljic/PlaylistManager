package ui;

import javafx.application.Platform;
import model.Song;
import repository.PlaylistRepository;
import repository.SongRepository;
import service.PlaylistService;
import service.SongService;
import config.MongoConfig;
import com.mongodb.client.MongoDatabase;

import service.Neo4jPopulateService;
import service.Neo4jService;
import repository.Neo4jRepository;
import config.Neo4jConfig;

import javafx.fxml.FXML;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Main UI controller for searching, viewing and managing songs and playlists.
 * Handles user interactions from the main JavaFX view.
 */
public class MainController {

    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<Song> resultsTable;
    @FXML
    private TableColumn<Song, String> titleCol;
    @FXML
    private TableColumn<Song, String> artistCol;
    @FXML
    private TableColumn<Song, String> albumCol;
    @FXML
    private TableColumn<Song, String> genreCol;
    @FXML
    private Label detailTitle;
    @FXML
    private Label detailArtist;
    @FXML
    private Label detailAlbum;
    @FXML
    private Label detailGenre;
    @FXML
    private Label detailDuration;

    @FXML
    private Button updateButton;
    @FXML
    private Button relatedButton;

    private SongService songService;

    private Neo4jService neo4jService;

    @FXML
    public void initialize() {
        System.out.println("[app] Starting application initialization...");
        System.out.println("[app] Initializing MongoDB connection...");
        MongoDatabase db = MongoConfig.getDatabase();
        System.out.println("[app] MongoDB ready. Creating services...");
        songService = new SongService(new SongRepository(db));

        neo4jService = new Neo4jService(
                new Neo4jRepository(Neo4jConfig.getDriver()));

        titleCol.setCellValueFactory(
                cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getTitle()));
        artistCol.setCellValueFactory(
                cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getArtist()));
        albumCol.setCellValueFactory(
                cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getAlbum()));
        genreCol.setCellValueFactory(
                cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getGenre()));

        resultsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSong, newSong) -> {
            if (newSong != null) {
                detailTitle.setText("Title: " + newSong.getTitle());
                detailArtist.setText("Artist: " + newSong.getArtist());
                detailAlbum.setText("Album: " + newSong.getAlbum());
                detailGenre.setText("Genre: " + newSong.getGenre());
                detailDuration.setText("Duration: " + newSong.getDuration() + " sec");
                resultsTable.refresh();
                System.out.println("[ui] Selected song: " + newSong.getId() + " - " + newSong.getTitle());
            }
        });
    }

    @FXML
    private void onSearchClicked() {
        String keyword = searchField.getText().trim();
        System.out.println("[ui] User requested search: '" + keyword + "'");
        List<Song> found = songService.searchSongs(keyword);
        System.out.println("[ui] Search returned " + found.size() + " results");
        ObservableList<Song> items = FXCollections.observableArrayList(found);
        resultsTable.setItems(items);
    }

    @FXML
    private void onUpdateClicked() {
        Song selected = resultsTable.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/update-song.fxml"));
            Parent root = loader.load();

            UpdateSongController controller = loader.getController();
            controller.setData(selected, songService);

            Stage stage = new Stage();
            stage.setTitle("Update Song");
            stage.setScene(new Scene(root));

            stage.setOnHidden(e -> {
                detailTitle.setText("Title: " + selected.getTitle());
                detailArtist.setText("Artist: " + selected.getArtist());
                detailAlbum.setText("Album: " + selected.getAlbum());
                detailGenre.setText("Genre: " + selected.getGenre());
                detailDuration.setText("Duration: " + selected.getDuration() + " sec");
            });

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onRelatedClicked() {
        Song selected = resultsTable.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/related-artists.fxml"));
            Parent root = loader.load();

            RelatedArtistsController controller = loader.getController();
            controller.loadData(selected.getArtist(), neo4jService);

            Stage stage = new Stage();
            stage.setTitle("Artists similar to " + selected.getArtist());
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onPopulateNeo4jClicked() {

        MongoDatabase db = MongoConfig.getDatabase();
        Neo4jPopulateService pop = new Neo4jPopulateService(db);

        // Run populate in a background thread
        new Thread(() -> {
            pop.populate();

            // After it's done, notify the UI
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Neo4j database populated successfully!");
                alert.showAndWait();
            });
        }).start();
    }

    @FXML
    private void onPlaylistsClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/playlist.fxml"));
            Parent root = loader.load();

            PlaylistController controller = loader.getController();
            controller.load(new PlaylistService(new PlaylistRepository(MongoConfig.getDatabase())));

            Stage stage = new Stage();
            stage.setTitle("Playlists");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAddToPlaylistClicked() {
        Song selected = resultsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Select a song first.");
            alert.showAndWait();
            return;
        }

        System.out.println("[ui] Add to playlist requested for song: " + selected.getId());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/add-to-playlist.fxml"));
            Parent root = loader.load();

            AddToPlaylistController controller = loader.getController();
            PlaylistService ps = new PlaylistService(new PlaylistRepository(MongoConfig.getDatabase()));
            controller.setData(selected, ps); // pass the song and playlist service

            Stage stage = new Stage();
            stage.setTitle("Add to Playlist");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
