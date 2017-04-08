package controllers;

import core.KioskMain;
import core.RoomNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.dir.Location;
import models.dir.LocationType;

import java.sql.*;

import java.io.IOException;

/**
 * Created by Jonathan on 3/31/2017.
 */
public class AdminModifyLocationController implements IControllerWithParams {

    private Location existingLoc;

    @FXML
    private TextField name;
    @FXML
    private TextField place;
    @FXML
    private ComboBox<LocationType> locationDropdown;

    @FXML
    private void initialize() {
        locationDropdown.getItems().addAll(LocationType.values());
        locationDropdown.getSelectionModel().selectFirst();
    }

    @FXML
    private void clickBack(ActionEvent event) throws IOException {
        //back button goes to Admin menu
        KioskMain.setScene("views/DirectoryView.fxml", true);
    }

    @FXML
    private void clickSubmit(ActionEvent event) throws IOException {
        try {
            // Edit Location
            if (existingLoc != null) {
                editLocation();
            } else { // New Location
                newLocation();
            }
        } catch(RoomNotFoundException e) {
            invalidRoomAlert();
        }
    }

    private void editLocation() throws RoomNotFoundException {
        String locName = (String) name.getText();
        String roomName = (String) place.getText();
        LocationType locType = locationDropdown.getValue();
        if (!locName.equals(existingLoc.getName())) {
            existingLoc.setName(locName);
        }
        if (!roomName.equals(existingLoc.getNode().getRoomName())) {
            existingLoc.setNode(KioskMain.getPath().getRoom(roomName));
        }
        if (!locType.equals(existingLoc.getLocType())) {
            existingLoc.setLocType(locType);
        }
        KioskMain.setScene("views/DirectoryView.fxml", true);
    }

    private void newLocation() throws RoomNotFoundException {
        String locName = (String) name.getText();
        String roomName = (String) place.getText();
        LocationType locType = locationDropdown.getValue();
        Location L = new Location(locName, locType, KioskMain.getPath().getRoom(roomName));
        KioskMain.getDir().addLocation(L);
        KioskMain.setScene("views/DirectoryView.fxml", true);
    }

    /**
     * Displays an alert notifying the user that the room they entered does not exist. Then clears the text box
     * for rooms.
     */
    private void invalidRoomAlert() {
        Alert invalidRoom = new Alert(Alert.AlertType.ERROR);
        invalidRoom.setHeaderText("Invalid Room!");
        invalidRoom.setTitle("Try Again!");
        invalidRoom.setContentText("Please enter a room that is currently in the database!");
        invalidRoom.showAndWait();
        place.clear();
    }

    /**
     * Used when editing a Location.
     * @param data The Location object to edit.
     */
    @Override
    public void initData(Object... data) {
        existingLoc = (Location) data[0];
        name.setText(existingLoc.getName());
        place.setText(existingLoc.getNode().getRoomName());
        locationDropdown.getSelectionModel().select(existingLoc.getLocType());
    }
}
