package controllers;

import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.path.Node;
import models.path.Path;

import java.io.IOException;

/**
 * Created by mattm on 3/29/2017.
 */
public class DirectionsViewController implements IControllerWithParams {
    private static final String MAP_URL = "views/Map.fxml";
    private Path thePath;

    private Node startNode;
    private Node endNode;

    @FXML
    private Button backBtn;

    @FXML
    private Button doneButton;

    @FXML
    private AnchorPane mapContainer;

    private MapController mapController;

    @FXML
    private void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(KioskMain.class.getClassLoader().getResource(MAP_URL));
            mapContainer.getChildren().add(loader.load());
            mapController = loader.<MapController>getController();
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
    }

    @FXML
    private void clickBack(ActionEvent event) {
        KioskMain.setScene("views/FinalDestSelectionView.fxml", this.startNode);
    }

    @FXML
    private void clickDone(ActionEvent event) {
        KioskMain.setScene("views/MainMenu.fxml");
    }

    @Override
    public void initData(Object... data) {
        this.startNode = (Node)data[0];
        this.endNode = (Node)data[1];

        this.thePath = KioskMain.getPath().findPath(this.startNode, this.endNode);

        //System.out.println(this.thePath);

        mapController.drawPath(this.thePath);
    }
}
