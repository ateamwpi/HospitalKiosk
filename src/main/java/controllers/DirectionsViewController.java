package controllers;

import controllers.map.MapController;
import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
public class DirectionsViewController extends AbstractController {

    private Node startNode;
    private Node endNode;

    @FXML
    private Button backBtn;
    @FXML
    private Button doneButton;
    @FXML
    private AnchorPane mapContainer;
    @FXML
    private Text directionsText;

    DirectionsViewController(Node startNode, Node endNode) {
        super(startNode, endNode);
    }

    @FXML
    private void initialize() {
        // load the map controller
        MapController mapController = new MapController();
        // add the map to the container
        mapContainer.getChildren().add(mapController.getRoot());
        // find the shortest path
        Path path = KioskMain.getPath().findPath(startNode, endNode);
        // draw the path on the map
        mapController.drawPath(path);
        // show the text directions
        directionsText.setText("Directions:\n" + path.textPath());
    }

    @FXML
    private void clickBack(ActionEvent event) {
        KioskMain.setScene(new DirectoryViewController());
    }

    @FXML
    private void clickDone(ActionEvent event) {
        KioskMain.setScene(new MainMenuController());
    }

    @Override
    public void initData(Object... data) {
        this.startNode = (Node) data[0];
        this.endNode = (Node) data[1];
    }

    @Override
    public String getURL() {
        return "views/DirectionsView.fxml";
    }
}
