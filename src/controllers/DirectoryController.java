package controllers;

import core.KioskMain;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import models.dir.Directory;
import models.dir.Location;
import models.dir.LocationType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Kevin O'Brien on 4/7/2017.
 */
public abstract class DirectoryController {
    protected HashMap<LocationType, Directory> directories;
    protected Collection<Location> selectedLocations; // all Locations of the current LocationType
    protected Collection<Location> filteredLocations; // all Locations that match the searchBox
    @FXML
    protected TableView<Location> locationsTable; //table to hold all locations
    protected Location selectedLocation;
    @FXML
    private TableColumn<Location, String> nameCol; //column that holds names of locations
    @FXML
    private TableColumn<Location, String> roomCol; //column that holds room names
    @FXML
    private TextField searchBox;
    @FXML
    private ComboBox<String> locationDropdown;

    public DirectoryController() {
        directories = KioskMain.getDir().getDirectories();
        selectedLocations = new ArrayList<>();
        filteredLocations = new ArrayList<>();
    }

    /**
     * Populate the Table of Locations. Sorts the table by name. When entry is selected, allow for use as starting
     * location.
     */
    protected void initializeTable() {
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        roomCol.setCellValueFactory(new PropertyValueFactory("roomName"));
        locationsTable.getSortOrder().add(nameCol); // Default to sort by name
    }

    /**
     * Populate LocationType Dropdown menu with all LocationTypes. Additionally, allow for Full Directory to be selected.
     */
    protected void initializeDropdown() {
        locationDropdown.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            selectDirectory(locationDropdown.getSelectionModel().getSelectedItem());
        }));
        locationDropdown.getItems().add("Full Directory");
        for (LocationType locType : LocationType.values()) {
            locationDropdown.getItems().add(locType.name());
        }
        locationDropdown.getSelectionModel().selectFirst();
    }

    protected void initializeFilter() {
        searchBox.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterLocations();
            }
        });
    }

    private void filterLocations() {
        filteredLocations.clear(); // Clear the list keeping track of filtered locations
        String filterString = searchBox.getText().toLowerCase(); // get the keyword
        // check which locations contain the keyword
        for (Location loc : selectedLocations) {
            if (loc.getName().toLowerCase().contains(filterString)) {
                filteredLocations.add(loc);
            }
        }
        showLocations(filteredLocations); // update the table to reflect the filteredLocations
    }

    /**
     * Establishes all locations to search through, displays them on table
     * @param locations
     */
    private void selectLocations(Collection<Location> locations) {
        searchBox.clear();
        filteredLocations.clear();
        selectedLocations = locations;
        showLocations(locations);
    }

    /**
     * Shows the given locations on the table and sorts them.
     * @param locations
     */
    private void showLocations(Collection<Location> locations) {
        locationsTable.getItems().setAll(locations);
        locationsTable.sort();
    }

    /**
     * Select the LocationType to use for the directory.
     * @param locType
     */
    private void selectDirectory(LocationType locType) {
        selectLocations(getLocationsOfType(locType));
    }

    /**
     * Selects the directory based upon a given string.
     * @param s
     */
    private void selectDirectory(String s) {
        if (s.equalsIgnoreCase("Full Directory")) {
            setFullDirectory();
        } else {
            selectDirectory(LocationType.getType(s));
        }
    }

    /**
     * Selects the locations based upon every location type.
     */
    private void setFullDirectory() {
        Collection<Location> locations = new ArrayList<Location>(); //make an array list of location// s
        KioskMain.getDir().getDirectories().values();
        for(LocationType locType : LocationType.values()) {
            locations.addAll(getLocationsOfType(locType));
        }
        //set the list of locations shown on table to be all the locations added above
        selectLocations(locations);
    }

    /**
     * Find locations of a given type.
     * @param locType
     * @return Collection of Locations of a given LocationType.
     */
    private Collection<Location> getLocationsOfType(LocationType locType) {
        if (directories.containsKey(locType)) {
            Directory dir = directories.get(locType);
            HashMap<Integer, Location> locations = dir.getLocations();
            return locations.values();
        }
        return new ArrayList<Location>(); //returns empty array list if there are no locations of given type
    }
}
