package controllers;

import controllers.map.MapController;
import core.KioskMain;
import core.exception.FloorNotReachableException;
import core.exception.NearestNotFoundException;
import core.exception.PathNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
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
    private TextArea directionsText;
    @FXML
    private ChoiceBox<String> floors;

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
        Path path;
        try {
            path = KioskMain.getPath().findPath(startNode, endNode);
        }
        catch (PathNotFoundException e) {
            // TODO catch and tell user
            e.printStackTrace();
            return;
        }
        catch (NearestNotFoundException e) {
            // TODO catch and tell user
            // this should only happen if there is no elevator on the current floor
            return;
        }
        catch (FloorNotReachableException e) {
            // TODO catch and tell user
            // this should only happen if the admin messes with the elvators
            return;
        }
        // draw the path on the map
        mapController.setFloor(startNode.getFloor());
        mapController.drawPath(path);
        floors.getItems().addAll(path.getFloorsSpanning());
        floors.getSelectionModel().selectFirst();
        if(path.getFloorsSpanning().size() == 1) floors.setDisable(true);
        floors.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (floors.getSelectionModel().getSelectedItem() != null) {
                String fl = floors.getSelectionModel().getSelectedItem();
                mapController.clearOverlay();
                mapController.setFloor(Integer.parseInt(fl.substring(0,1)));
                mapController.drawPath(path);
            }
        });

        // show the text directions
        directionsText.setText("Directions:\n" + path.textPath());
    }

    @FXML
    private void clickBack(ActionEvent event) {
        KioskMain.setScene(new DirectoryViewController());
    }

    @FXML
    private void clickSpeak(ActionEvent event) {
        KioskMain.getTTS().speak(directionsText.getText());
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
