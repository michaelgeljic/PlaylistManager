package ui;

import model.Song;
import repository.SongRepository;
import service.SongService;
import config.MongoConfig;
import com.mongodb.client.MongoDatabase;

import service.Neo4jPopulateService;
import service.Neo4jService;
import repository.Neo4jRepository;
import config.Neo4jConfig;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
        MongoDatabase db = MongoConfig.getDatabase();
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
            }
        });
    }

    @FXML
    private void onSearchClicked() {
        String keyword = searchField.getText().trim();
        ObservableList<Song> items = FXCollections.observableArrayList(songService.searchSongs(keyword));
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
            stage.setTitle("Related Artists");
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
        pop.populate();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Neo4j database populated successfully!");
        alert.showAndWait();
    }
}
