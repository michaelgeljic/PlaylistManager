package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Song;
import service.SongService;

/**
 * Controller responsible for showing and updating an existing Song record.
 */
public class UpdateSongController {

    @FXML
    private TextField titleField;
    @FXML
    private TextField artistField;
    @FXML
    private TextField albumField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField durationField;

    private Song song;
    private SongService songService;

    public void setData(Song song, SongService songService) {
        this.song = song;
        this.songService = songService;

        titleField.setText(song.getTitle());
        artistField.setText(song.getArtist());
        albumField.setText(song.getAlbum());
        genreField.setText(song.getGenre());
        durationField.setText(String.valueOf(song.getDuration()));
    }

    @FXML
    private void onSaveClicked() {
        try {
            song.setTitle(titleField.getText());
            song.setArtist(artistField.getText());
            song.setAlbum(albumField.getText());
            song.setGenre(genreField.getText());
            song.setDuration(Integer.parseInt(durationField.getText()));

            songService.updateSong(song);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Song updated successfully!");
            alert.showAndWait();

            ((Stage) titleField.getScene().getWindow()).close();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error updating song");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}