package controllers;

import core.KioskMain;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.dir.Directory;
import models.dir.Location;
import models.dir.LocationType;

import java.io.IOException;
import java.util.*;

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
    private TableView<Location> locationsTable;
    @FXML
    private TableColumn<Location, String> nameCol;
    @FXML
    private TableColumn<Location, String> nodeCol;


    public DirectoryViewController() {
        // get all directories from dbMg
        directories = KioskMain.getDB().getAllDirectories();
    }

    @FXML
    private void initialize() {
        // setup column cell factories
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));

        // select default directory
        selectDirectory(LocationType.Physician);
    }

    @FXML
    private void clickBack(ActionEvent event) throws IOException {
        KioskMain.setScene("views/MainMenu.fxml");
    }

    @FXML
    private void clickFullDirectory(ActionEvent event) {
        Collection<Location> locations = new ArrayList<Location>();
        locations.addAll(getLocationsOfType(LocationType.Physician));
        locations.addAll(getLocationsOfType(LocationType.PointOfInterest));
        locations.addAll(getLocationsOfType(LocationType.Elevator));
        locations.addAll(getLocationsOfType(LocationType.Room));
        locations.addAll(getLocationsOfType(LocationType.Stairs));
        setLocations(locations);
    }

    @FXML
    private void clickPhysicians(ActionEvent event) {
        selectDirectory(LocationType.Physician);
    }

    @FXML
    private void clickPOI(ActionEvent event) {
        selectDirectory(LocationType.PointOfInterest);
    }

    // add the directory locations to the list view
    private void selectDirectory(LocationType locType) {
        setLocations(getLocationsOfType(locType));
    }

    private Collection<Location> getLocationsOfType(LocationType locType) {
        if (directories.containsKey(locType)) {
            Directory dir = directories.get(locType);
            HashMap<Integer, Location> locations = dir.getLocations();
            return locations.values();
        }
        return new ArrayList<Location>();
    }

    private void setLocations(Collection<Location> locations) {
        locationsTable.getItems().setAll(locations);
    }
}
