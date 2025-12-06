package ui;

import model.Song;
import repository.SongRepository;
import service.SongService;
import config.MongoConfig;
import com.mongodb.client.MongoDatabase;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainController {

    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private TableView<Song> resultsTable;
    @FXML private TableColumn<Song, String> titleCol;
    @FXML private TableColumn<Song, String> artistCol;
    @FXML private TableColumn<Song, String> albumCol;
    @FXML private TableColumn<Song, String> genreCol;

    private SongService songService;

    @FXML
    public void initialize() {
        MongoDatabase db = MongoConfig.getDatabase();
        songService = new SongService(new SongRepository(db));

        titleCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getTitle()));
        artistCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getArtist()));
        albumCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getAlbum()));
        genreCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getGenre()));
    }

    @FXML
    private void onSearchClicked() {
        String keyword = searchField.getText().trim();
        ObservableList<Song> items = FXCollections.observableArrayList(songService.searchSongs(keyword));
        resultsTable.setItems(items);
    }
}
