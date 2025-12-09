package ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Playlist;
import model.Song;
import service.PlaylistService;
import service.SongService;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for editing a single playlist: viewing and removing songs.
 * Adding songs is done via a selection dialog elsewhere.
 */
public class PlaylistEditController {

    @FXML
    private Label headerLabel;
    @FXML
    private ListView<Song> songsList;

    private String playlistId;
    private PlaylistService playlistService;

    private SongService songService = null; // for resolving song titles

    public void load(String playlistId, PlaylistService playlistService, SongService songService) {
        this.playlistId = playlistId;
        this.playlistService = playlistService;
        this.songService = songService;

        Playlist playlist = playlistService.get(playlistId);
        headerLabel.setText("Playlist: " + playlist.getName());

        // show song title in the list view cells
        songsList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Song item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null)
                    setText(null);
                else
                    setText(item.getTitle() + " - " + item.getArtist());
            }
        });

        refreshList();
    }

    private void refreshList() {
        Playlist playlist = playlistService.get(playlistId);
        songsList.getItems().clear();

        if (playlist.getSongIds() == null || playlist.getSongIds().isEmpty())
            return;

        List<Song> resolved = new ArrayList<>();
        for (String id : playlist.getSongIds()) {
            Song s = songService == null ? null : songService.getSong(id);
            if (s != null)
                resolved.add(s);
            else
                resolved.add(new Song(id, "Unknown", "Unknown", "", "", 0));
        }

        songsList.getItems().addAll(resolved);
    }

    @FXML
    private void onAddSongClicked() {
        // show a dialog with all songs to pick from
        Dialog<Song> dialog = new Dialog<>();
        dialog.setTitle("Add Song");
        ButtonType addBtn = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addBtn, ButtonType.CANCEL);

        ListView<Song> allSongsList = new ListView<>();
        allSongsList.getItems().addAll(FXCollections.observableArrayList(songService.getAllSongs()));
        allSongsList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Song item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTitle() + " - " + item.getArtist());
            }
        });

        dialog.getDialogPane().setContent(allSongsList);

        dialog.setResultConverter(btn -> {
            if (btn == addBtn)
                return allSongsList.getSelectionModel().getSelectedItem();
            return null;
        });

        var res = dialog.showAndWait();
        if (res.isPresent()) {
            Song picked = res.get();
            if (picked != null) {
                playlistService.addSong(playlistId, picked.getId());
                refreshList();
            }
        }
    }

    @FXML
    private void onRemoveSongClicked() {
        Song selected = songsList.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;

        playlistService.removeSong(playlistId, selected.getId());
        refreshList();
    }
}
