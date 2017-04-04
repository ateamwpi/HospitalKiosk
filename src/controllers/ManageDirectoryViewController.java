package controllers;

import core.KioskMain;
import core.RoomNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.dir.Directory;
import models.dir.Location;
import models.dir.LocationType;
import models.path.Node;


import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ManageDirectoryViewController {
    private HashMap<LocationType, Directory> directories;
    Location physician;
    Node location;

    @FXML
    private Button backBtn;
    @FXML
    private Button addPhysician;
    @FXML
    private Button removePhysician;
    @FXML
    private Button editPhysician;
    @FXML
    private Label errorRemove;
    @FXML
    private Label errorEdit;
    @FXML
    private TableView<Location> locationsTable;
    @FXML
    private TableColumn<Location, String> nameCol;
    @FXML
    private TableColumn<Location, String> nodeCol;


    public ManageDirectoryViewController() {
        // get all directories from dbMg
        directories = KioskMain.getDB().getAllDirectories();
    }

    @FXML
    private void initialize() {
        // setup column cell factories
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        // select default directory
        selectDirectory(LocationType.Physician);

        removePhysician.setDisable(true);

        locationsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (locationsTable.getSelectionModel().getSelectedItem() != null) {
                physician = newValue;
                location = newValue.getNode();

                removePhysician.setDisable(false);
            }
        });

    }

    private void selectDirectory(LocationType locType) {

        setLocations(getLocationsOfType(locType));
    }

    private void setLocations(Collection<Location> locations) {
        locationsTable.getItems().setAll(locations);
    }

    private Collection<Location> getLocationsOfType(LocationType locType) {
        if (directories.containsKey(locType)) {
            Directory dir = directories.get(locType);
            HashMap<Integer, Location> locations = dir.getLocations();
            return locations.values();
        }
        return new ArrayList<Location>(); //returns empty array list if there are no locations of given type
    }


    @FXML
    private void clickRemovePhysician(ActionEvent event)throws IOException {
        KioskMain.getDir().removeLocation(physician);
        KioskMain.setScene("views/ManageDirectoryView.fxml");
    }

    @FXML
    private void clickPhysician(ActionEvent event)throws IOException {
        Location destination = locationsTable.getSelectionModel().getSelectedItem();

        removePhysician.setDisable(false); //set 'get path' button to not disabled so user will know they can click it
    }


    @FXML
    private void clickBack(ActionEvent event) {
        KioskMain.setScene("views/AdminMenu.fxml");
    }

    @FXML
    private void clickAddPhysician(ActionEvent event) throws IOException {
        KioskMain.setScene("views/AdminAddPhysician.fxml");
    }

    @FXML
    private void clickEditPhysician(ActionEvent event) throws IOException {

    }

}

