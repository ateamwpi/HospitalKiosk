package controllers;

import core.KioskMain;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.dir.Directory;
import models.dir.Location;
import models.dir.LocationType;
import models.path.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Madeline on 4/3/2017.
 */

public class FinalDestSelectionController implements IControllerWithParams {

    private HashMap<LocationType, Directory> directories;

    private Collection<Location> currentLocations;

    private Collection<Location> filteredLocations;

    @FXML
    private Button startOverBtn;
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
    private Button getPath; //button user will press to get path to selected
    @FXML
    private Label directions; //label to give user instructions
    @FXML
    private TextField searchBox;

    private Node startNode;
    private Node endNode;

    public FinalDestSelectionController() {
        // get all directories from dbMg
        directories = KioskMain.getDB().getAllDirectories();
        currentLocations = new ArrayList<Location>();
        filteredLocations = new ArrayList<Location>();
    }

    @FXML
    private void initialize() {
        // setup column cell factories
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        roomCol.setCellValueFactory(new PropertyValueFactory("roomName"));

        // select default directory, display name of directory and display instructions
        selectDirectory(LocationType.Physician);
        dirLabel.setText("Physicians");
        directions.setText("Select an ending location from the table below. Once a location is selected, click the 'Get Path' button " +
                "to view a path connecting the  selected starting and ending locations.");

        //disable get path button so user cannot move on until they have selected a final location
        getPath.setDisable(true);

        locationsTable.getSortOrder().add(nameCol);

        locationsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (locationsTable.getSelectionModel().getSelectedItem() != null) {
                this.endNode = newValue.getNode();
                getPath.setDisable(false);
            }
        });

        searchBox.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterLocations();
            }
        });

    }

    @FXML
    private void clickStartOver(ActionEvent event) {
        KioskMain.setScene("views/DirectoryView.fxml");
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

        dirLabel.setText("Full Directory");
    }

    @FXML
    private void clickPhysicians(ActionEvent event) {

        selectDirectory(LocationType.Physician);

        dirLabel.setText("Physicians");
    }

    @FXML
    private void clickServices(ActionEvent event) {

        selectDirectory(LocationType.Service);

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
        searchBox.clear();
        currentLocations = locations;
        updateTable(locations);
    }

    private void updateTable(Collection<Location> locations) {
        locationsTable.getItems().setAll(locations);
        locationsTable.sort();
    }

    private void filterLocations() {
        filteredLocations.clear();
        String filterString = searchBox.getText().toLowerCase();
        for (Location loc : currentLocations) {
            if (loc.getName().toLowerCase().contains(filterString)) {
                filteredLocations.add(loc);
            }
        }
        updateTable(filteredLocations);
    }

    //record user selection
    @FXML
    private void clickEntry(ActionEvent event){
        Location destination = locationsTable.getSelectionModel().getSelectedItem();
        getPath.setDisable(false); //set 'get path' button to not disabled so user will know they can click it


    }

    @FXML  //when user clicks -> button, they will be brought to new page and asked to pick final destination
    private void clickGetPath(ActionEvent event) {
        System.out.println(this.startNode + ", " + this.endNode);
        KioskMain.setScene("views/DirectionsView.fxml", this.startNode, this.endNode);
    }

    @Override
    public void initData(Object... data) {
        this.startNode = (Node)data[0];
    }
}

