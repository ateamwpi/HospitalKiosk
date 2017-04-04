package controllers;

import core.KioskMain;
import javafx.beans.property.SimpleStringProperty;
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
import models.path.Node;

import javax.swing.*;
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
    private Button servicesBtn;
    @FXML
    private TableView<Location> locationsTable; //table to hold all locations
    @FXML
    private TableColumn<Location, String> nameCol; //column that holds names of locations
    @FXML
    private TableColumn<Location, String> roomCol; //column that holds room names
    @FXML
    private Label dirLabel; //label that shows type of directory shown
    @FXML
    private Button goToFinalSel; //button user will press to get path to selected
    @FXML
    private Label directions; //label to give user instructions


    public DirectoryViewController() {
        // get all directories from dbMg
        directories = KioskMain.getDB().getAllDirectories();
    }

    @FXML
    private void initialize() {
        // setup column cell factories
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        roomCol.setCellValueFactory(new PropertyValueFactory("roomName"));

        // select default directory
        selectDirectory(LocationType.Physician);

        //set title of default directory and directions given
        dirLabel.setText("Physicians");
        directions.setText("Select a starting location from the table below. Once a location is selected, click the '->' button " +
                "to next choose a final destination.");

        //disable the -> button so user cannot move on until they have selected an entry
        goToFinalSel.setDisable(true);

        locationsTable.getSortOrder().add(nameCol);

        locationsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (locationsTable.getSelectionModel().getSelectedItem() != null) {
                Node destination1 = newValue.getNode(); //store the node that was selected
                goToFinalSel.setDisable(false); //enable -> button once selection has been made
            }
        });

    }

    @FXML  //when user clicks "back" button, they will return to main menu
    private void clickBack(ActionEvent event) {
        KioskMain.setScene("views/MainMenu.fxml");
    }

    @FXML
    private void clickFullDirectory(ActionEvent event) {
        Collection<Location> locations = new ArrayList<Location>(); //make an array list of locations
        locations.addAll(getLocationsOfType(LocationType.Service)); // add all locations of type Service
        locations.addAll(getLocationsOfType(LocationType.Physician)); //add all locations of type Physician
        locations.addAll(getLocationsOfType(LocationType.PointOfInterest)); //add Points of Interest
        locations.addAll(getLocationsOfType(LocationType.Elevator)); //add elevators
        locations.addAll(getLocationsOfType(LocationType.Room)); //add rooms
        locations.addAll(getLocationsOfType(LocationType.Stairs)); //add stairs

        //set the list of locations shown on table to be all the locations added above
        setLocations(locations);

        //display name of directory
        dirLabel.setText("Full Directory");
    }

    @FXML
    private void clickPhysicians(ActionEvent event) {
        //set directory to get all locations of type physician
        selectDirectory(LocationType.Physician);

        //display name of directory
        dirLabel.setText("Physicians");
    }

    @FXML
    private void clickServices(ActionEvent event) {
        //set directory to get all locations of type points of interest
        selectDirectory(LocationType.Service);
        //display name of directory
        dirLabel.setText("Services");
    }

    // add the directory locations to the list view
    private void selectDirectory(LocationType locType) {

        setLocations(getLocationsOfType(locType));
    }

    //returns list of directory entries of given location type
    private Collection<Location> getLocationsOfType(LocationType locType) {
        if (directories.containsKey(locType)) {
            Directory dir = directories.get(locType);
            HashMap<Integer, Location> locations = dir.getLocations();
            return locations.values();
        }
        return new ArrayList<Location>(); //returns empty array list if there are no locations of given type
    }

    //sets what type of locations are shown on the table
    private void setLocations(Collection<Location> locations) {

        locationsTable.getItems().setAll(locations);
        locationsTable.sort();
    }

    @FXML  //when user clicks -> button, they will be brought to new page and asked to pick final destination
    private void clickGoToFinalSel(ActionEvent event) {
        KioskMain.setScene("views/FinalDestSelectionView.fxml");
    }

}

