package controllers.admin;

import controllers.AbstractController;
import core.KioskMain;
import core.Utils;
import core.exception.RoomNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import models.dir.Location;
import models.dir.LocationType;

import java.io.IOException;

/**
 * Created by dylan on 4/9/17.
 */
public class AdminAddLocationController extends AbstractController {

    @FXML
    private TextField name;
    @FXML
    private TextField place;
    @FXML
    private ComboBox<LocationType> locationDropdown;

    AdminAddLocationController() {
    }

    @Override
    public String getURL() {
        return "views/AdminAddLocation.fxml";
    }

    @FXML
    private void initialize() {
        locationDropdown.getItems().addAll(LocationType.values());
        locationDropdown.getSelectionModel().selectFirst();
    }

    @FXML
    private void clickBack(ActionEvent event) throws IOException {
        //back button goes to Admin menu
        KioskMain.getUI().setScene(new ManageDirectoryViewController());
    }

    @FXML
    private void clickSubmit(ActionEvent event) throws IOException {
        try {
            newLocation();
        } catch(RoomNotFoundException e) {
            invalidRoomAlert();
        }
    }

    private void newLocation() throws RoomNotFoundException {
        String locName = name.getText();
        if (locName != null && !locName.isEmpty()) {
            String roomName = place.getText();
            LocationType locType = locationDropdown.getValue();
            Location L = new Location(locName, locType, KioskMain.getPath().getRoom(roomName));
            KioskMain.getDir().addLocation(L);
            KioskMain.getUI().setScene(new ManageDirectoryViewController());
        } else {
            blankNameAlert();
        }


    }

    /**
     * Displays an alert notifying the user that the room they entered does not exist. Then clears the text box
     * for rooms.
     */
    private void invalidRoomAlert() {
        Utils.showAlert(getRoot(), "Invalid Room!", "Please enter a room that is currently in the database!");
    }

    private void blankNameAlert() {
        Utils.showAlert(getRoot(), "Name cannot be blank!", "Please enter a name for this location!");
    }

}
