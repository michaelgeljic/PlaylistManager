package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import model.Playlist;
import service.PlaylistService;
import service.SongService;
import repository.SongRepository;
import config.MongoConfig;

/**
 * Controller that displays the list of playlists and opens the editor for a
 * selected playlist.
 */
public class PlaylistController {

    @FXML
    private ListView<Playlist> playlistList;

    private PlaylistService playlistService;

    public void load(PlaylistService service) {
        this.playlistService = service;

        playlistList.getItems().clear();
        playlistList.getItems().addAll(playlistService.getAll());

        // display only playlist name
        playlistList.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Playlist item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
    }

    @FXML
    private void onCreatePlaylist() {
        TextInputDialog input = new TextInputDialog();
        input.setHeaderText("Enter playlist name:");
        var result = input.showAndWait();
        if (result.isPresent()) {
            playlistService.create(result.get());
            load(playlistService);
        }
    }

    @FXML
    private void onOpenPlaylist() {
        Playlist selected = playlistList.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;

        String playlistId = selected.getId();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/playlist-edit.fxml"));
            Parent root = loader.load();

            PlaylistEditController controller = loader.getController();
            // pass playlist service and a song service for resolving song titles
            controller.load(playlistId, playlistService,
                    new SongService(new SongRepository(MongoConfig.getDatabase())));

            Stage stage = new Stage();
            stage.setTitle("Edit Playlist");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
