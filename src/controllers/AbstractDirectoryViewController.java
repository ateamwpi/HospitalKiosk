package controllers;

import core.KioskMain;
import core.RoomNotFoundException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.print.PageLayout;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import models.dir.Directory;
import models.dir.Location;
import models.dir.LocationType;
import models.path.Node;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * The abstract class that handles the Directory views.
 * Created by Kevin O'Brien on 4/7/2017.
 */
public abstract class AbstractDirectoryViewController extends AbstractController {
    private HashMap<LocationType, Directory> directories;
    private Collection<Location> selectedLocations; // all Locations of the current LocationType
    private Collection<Location> filteredLocations; // all Locations that match the searchBox
    @FXML
    protected TableView<Location> locationsTable; //table to hold all locations
    protected Location selectedLocation;
    @FXML
    private TableColumn<Location, String> nameCol; //column that holds names of locations
    @FXML
    private TableColumn<Location, String> roomCol; //column that holds room names
    @FXML
    private TableColumn<Location, LocationType> typeCol;
    @FXML
    private TextField searchBox;
    @FXML
    protected ComboBox<String> locationDropdown;

    protected AbstractDirectoryViewController(Object... data) {
        super(data);
    }

    @Override
    public void initData(Object... data) {
        directories = KioskMain.getDir().getDirectories();
        selectedLocations = new ArrayList<>();
        filteredLocations = new ArrayList<>();
    }

    /**
     * Populate the Table of Locations. Sorts the table by name. When entry is selected, allow for use as starting
     * location.
     */
    protected void initializeTable() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        roomCol.setCellValueFactory(new PropertyValueFactory<>("roomName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("locType"));
        locationsTable.getSortOrder().add(nameCol); // Default to sort by name
    }

    /**
     * Populate LocationType Dropdown menu with all LocationTypes. Additionally, allow for Full Directory to be selected.
     */
    protected void initializeDropdown() {
        locationDropdown.getSelectionModel().selectedItemProperty().addListener((
                (observable, oldValue, newValue) -> selectDirectory(locationDropdown.getSelectionModel().getSelectedItem())));
        locationDropdown.getItems().add("Full Directory");
        for (LocationType locType : LocationType.values()) {
            if (!locType.isInternal()) {
                locationDropdown.getItems().add(locType.name());
            }

        }
        locationDropdown.getSelectionModel().selectFirst();
    }

    protected void initializeFilter() {
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> filterLocations());
    }

    private void filterLocations() {
        filteredLocations.clear(); // Clear the list keeping track of filtered locations
        String filterString = searchBox.getText().toLowerCase(); // get the keyword
        // check which locations contain the keyword
        for (Location loc : selectedLocations) {
            if (loc.getName().toLowerCase().contains(filterString) ||
                    loc.getNode().getRoomName().toLowerCase().contains(filterString)) {
                filteredLocations.add(loc);
            }
        }
        showLocations(filteredLocations); // update the table to reflect the filteredLocations
    }

    /**
     * Establishes all locations to search through, displays them on table
     * @param locations Locations
     */
    private void selectLocations(Collection<Location> locations) {
        searchBox.clear();
        filteredLocations.clear();
        selectedLocations = locations;
        showLocations(locations);
    }

    /**
     * Shows the given locations on the table and sorts them.
     * @param locations Locations
     */
    private void showLocations(Collection<Location> locations) {
        locationsTable.getItems().setAll(locations);
        locationsTable.sort();
    }

    /**
     * Select the LocationType to use for the directory.
     * @param locType Location type of directory
     */
    private void selectDirectory(LocationType locType) {
        selectLocations(getLocationsOfType(locType));
    }

    /**
     * Selects the directory based upon a given string.
     * @param s Location type string
     */
    protected void selectDirectory(String s) {
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
        Collection<Location> locations = new ArrayList<>();
        KioskMain.getDir().getDirectories().values();
        for(LocationType locType : LocationType.values()) {
            if(!locType.isInternal()) {
                locations.addAll(getLocationsOfType(locType));
            }
        }
        //set the list of locations shown on table to be all the locations added above
        selectLocations(locations);
    }

    /**
     * Find locations of a given type.
     * @param locType Location type
     * @return Collection of Locations of a given LocationType.
     */
    private Collection<Location> getLocationsOfType(LocationType locType) {
        if (directories.containsKey(locType)) {
            Directory dir = directories.get(locType);
            HashMap<Integer, Location> locations = dir.getLocations();
            return locations.values();
        }
        return new ArrayList<>(); //returns empty array list if there are no locations of given type
    }

    protected void setTableEdit() {
        locationsTable.setEditable(true);

        nameCol.setCellFactory(TextFieldTableCell.<Location>forTableColumn());
        nameCol.setOnEditCommit((TableColumn.CellEditEvent<Location, String> t) -> {
            Location editedLoc = t.getRowValue();
            t.getRowValue().setName(t.getNewValue());
            locationsTable.sort();
            locationsTable.scrollTo(editedLoc);
        });

        ArrayList<String> roomNames = new ArrayList<String>(KioskMain.getPath().getRoomNames());
        Collections.sort(roomNames);
        roomCol.setCellFactory(ComboBoxTableCell.forTableColumn(roomNames.toArray(new String[roomNames.size()])));
        roomCol.setOnEditCommit((TableColumn.CellEditEvent<Location, String> t) -> {
            try {
                Node roomNode = KioskMain.getPath().getRoom(t.getNewValue());
                t.getRowValue().setNode(roomNode);
            } catch (RoomNotFoundException e) {
                invalidRoomAlert();
                t.getTableView().getItems().set(t.getTablePosition().getRow(), t.getRowValue());
            }
        });

        typeCol.setCellFactory(ComboBoxTableCell.forTableColumn(LocationType.userValues()));
        typeCol.setOnEditCommit((TableColumn.CellEditEvent<Location, LocationType> t) -> {
            t.getRowValue().setLocType(t.getNewValue());
        });

    }


    private void invalidRoomAlert() {
        Alert invalidRoom = new Alert(Alert.AlertType.ERROR);
        invalidRoom.setHeaderText("Invalid Room!");
        invalidRoom.setTitle("Try Again!");
        invalidRoom.setContentText("Please enter a room that is currently in the database!");
        invalidRoom.showAndWait();
    }
}
