package controllers;

import controllers.map.MapController;
import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import models.path.Node;
import models.path.Path;

import java.io.IOException;

/**
 * Created by mattm on 3/29/2017.
 */
public class DirectionsViewController implements IControllerWithParams {
    private static final String MAP_URL = "views/Map.fxml";

    private Node startNode;
    private Node endNode;
    private Path path;

    @FXML
    private Button backBtn;

    @FXML
    private Button doneButton;

    @FXML
    private AnchorPane mapContainer;

    private MapController mapController;

    @FXML
    private Text directionsText;

    @FXML
    private void initialize() {
        // load the map controller
        mapController = new MapController();
        // get the map
        Region map = mapController.getRoot();
        // add the map to the container
        mapContainer.getChildren().add(map);
    }

    @FXML
    private void clickBack(ActionEvent event) {
        KioskMain.setScene("views/DirectoryView.fxml", false);
    }

    @FXML
    private void clickDone(ActionEvent event) {
        KioskMain.setScene("views/MainMenu.fxml");
    }

    @Override
    public void initData(Object... data) {
        // set the start and end nodes
        startNode = (Node) data[0];
        endNode = (Node) data[1];
        // find the shortest path
        path = KioskMain.getPath().findPath(startNode, endNode);
        // draw the path on the map
        mapController.drawPath(path);
        // show the text directions
        directionsText.setText("Directions:\n" + path.textPath());
    }
}
