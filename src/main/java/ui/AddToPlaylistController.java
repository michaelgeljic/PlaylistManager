package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Playlist;
import model.Song;
import service.PlaylistService;

/**
 * Controller that allows adding a selected song to a chosen playlist.
 * Displays a list of available playlists and adds the song upon user
 * confirmation.
 */
public class AddToPlaylistController {

    @FXML
    private ListView<Playlist> playlistList;

    private Song selectedSong;

    private PlaylistService playlistService;

    public void setData(Song song, PlaylistService playlistService) {
        this.selectedSong = song;
        this.playlistService = playlistService;

        playlistList.getItems().clear();
        playlistList.getItems().addAll(playlistService.getAll());
        // show only the playlist name in the list
        playlistList.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Playlist item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
    }

    @FXML
    private void onAddClicked() {
        Playlist p = playlistList.getSelectionModel().getSelectedItem();

        if (p == null) {
            new Alert(Alert.AlertType.WARNING, "Select a playlist").showAndWait();
            return;
        }

        playlistService.addSong(p.getId(), selectedSong.getId());

        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setHeaderText("Success");
        info.setContentText("Added '" + selectedSong.getTitle() + "' to playlist '" + p.getName() + "'");
        info.showAndWait();

        ((Stage) playlistList.getScene().getWindow()).close();
    }
}
