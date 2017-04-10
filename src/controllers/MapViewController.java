package controllers;

import controllers.map.MapController;
import core.KioskMain;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by mattm on 3/29/2017.
 */
public class MapViewController extends AbstractController {
    @FXML
    private ChoiceBox floors;
    @FXML
    private AnchorPane mapView;
    @FXML
    private Button backBtn;


    @Override
    public String getURL() {
        return "views/MapView.fxml";
    }

    @FXML
    private void initialize() {

        floors.setItems(FXCollections.observableArrayList(
                "1st Floor", "2nd Floor","3rd Floor", "4th Floor", "5th Floor", "6th Floor", "7th Floor"
                )
        );

        floors.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (floors.getSelectionModel().getSelectedItem() != null) {
                String fl = (String) newValue;
                setBackground(fl);
            }
        });

    }

    public void setBackground(String fl){
        MapController mapController = new MapController();
        switch(fl){
            case "1st Floor":
                mapController.setFloor(1);
                break;
            case "2nd Floor":
                mapController.setFloor(2);
                break;
            case "3rd Floor":
                mapController.setFloor(3);
                break;
            case "4th Floor":
                mapController.setFloor(4);
                break;
            case "5th Floor":
                mapController.setFloor(5);
                break;
            case "6th Floor":
                mapController.setFloor(6);
                break;
            case "7th Floor":
                mapController.setFloor(7);
                break;

        }

        // add the map to the container
        mapView.getChildren().add(mapController.getRoot());
    }

    @FXML
    private void clickBack(ActionEvent event) {
        KioskMain.setScene(new MainMenuController());
    }
}
