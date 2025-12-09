package ui;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import service.Neo4jService;

import java.util.List;

/**
 * Controller that displays artists related to a given artist using Neo4j data.
 */
public class RelatedArtistsController {

    @FXML
    private ListView<String> relatedList;

    public void loadData(String artistName, Neo4jService neoService) {
        List<String> related = neoService.getRelated(artistName);
        relatedList.getItems().addAll(related);
    }
}