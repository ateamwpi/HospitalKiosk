package controllers;

import controllers.map.MapController;
import core.KioskMain;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
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
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by mattm on 3/29/2017.
 */
public class MapViewController extends AbstractController {
    @FXML
    private ChoiceBox<String> floors;
    @FXML
    private AnchorPane mapView;
    @FXML
    private Button backBtn;

    private ArrayList<String> floorList;

    private MapController mapController;

    @Override
    public String getURL() {
        return "views/MapView.fxml";
    }

    @Override
    public void initData(Object... data) {
        floorList = new ArrayList<String>(Arrays.asList("1st Floor", "2nd Floor","3rd Floor", "4th Floor", "5th Floor", "6th Floor", "7th Floor"));
    }

    @FXML
    private void initialize() {

        mapController = new MapController();
        mapView.getChildren().add(mapController.getRoot());

        floors.getItems().addAll(floorList);

        floors.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (floors.getSelectionModel().getSelectedItem() != null) {
                String fl = newValue;
                setBackground(fl);
            }
        });

    }

    public void setBackground(String fl){
        int floor = floorList.indexOf(fl) + 1;
        mapController.setFloor(floor);
        // add the map to the container

    }

    @FXML
    private void clickBack(ActionEvent event) {
        KioskMain.setScene(new MainMenuController());
    }
}
