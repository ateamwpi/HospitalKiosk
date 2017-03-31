package controllers;

import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import models.dir.Directory;
import models.dir.Location;
import models.dir.LocationType;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by mattm on 3/29/2017.
 */
public class DirectoryViewController {

    private HashMap<LocationType, Directory> directories;

    @FXML
    private Button backBtn;
    @FXML
    private Button fullDirectoryBtn;
    @FXML
    private Button physiciansBtn;
    @FXML
    private Button pointOfInterestBtn;
    @FXML
    private Button otherBtn;

    @FXML
    private ListView<HashMap<Integer, Location>> locationsList;

    public DirectoryViewController() {
        // TODO
        // init the list view
        locationsList = new ListView<HashMap<Integer, Location>>();
        // getAllDirectories from dbMg
        directories = KioskMain.getDB().getAllDirectories();
        // select default directory
        selectDirectory(LocationType.Physician);
    }

    @FXML
    private void clickBack(ActionEvent event) {
        KioskMain.setScene("views/MainMenu.fxml");
    }

    @FXML
    private void clickFullDirectory(ActionEvent event) {
        // TODO
    }

    @FXML
    private void clickPhysicians(ActionEvent event) {
        selectDirectory(LocationType.Physician);
    }

    @FXML
    private void clickPOI(ActionEvent event) {
        selectDirectory(LocationType.PointOfInterest);
    }

    @FXML
    private void clickOther(ActionEvent event) {
        // TODO
    }

    // add the directory locations to the list view
    private void selectDirectory(LocationType locType) {
        if (directories.containsKey(locType)) {
            Directory dir = directories.get(locType);
            HashMap<Integer, Location> locations = dir.getEntries();
            locationsList.getItems().setAll(locations);
        }
    }
}
