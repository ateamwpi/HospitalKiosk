package controllers.admin;

import com.jfoenix.controls.JFXButton;
import controllers.AbstractDirectoryViewController;
import core.KioskMain;
import core.exception.RoomNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.dir.Location;
import models.dir.LocationType;
import models.path.Node;

import java.io.IOException;

/**
 * Created by dylan on 4/9/17.
 */
public class ManageDirectoryViewController extends AbstractDirectoryViewController {

    @FXML
    private Label directions; //label to give user instructions
    @FXML
    private JFXButton addEntry;
    @FXML
    private JFXButton removeEntry;


    ManageDirectoryViewController() {}

    @Override
    public String getURL() {
        return "views/ManageDirectoryView.fxml";
    }

    @FXML
    private void initialize() {
        initializeTable();
        initializeFilter();
        setFullDirectory();
        // choose admin mode
        adminMode();
        // listen to location table selection event
        locationsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (locationsTable.getSelectionModel().getSelectedItem() != null) {
                selectedLocation = newValue;
                removeEntry.setDisable(false);
            } else {
                removeEntry.setDisable(true);
            }
        });
    }

    private void adminMode() {
        setTableEdit();
        removeEntry.setDisable(true);
        directions.setText("");
        addLocationBtns();
    }

    @FXML  //when user clicks "back" button, they will return to main menu
    private void clickBack(ActionEvent event) {
        KioskMain.getUI().setScene(new AdminMenuController());
    }

    @FXML
    private void clickAdd(ActionEvent event) throws IOException {
        String defaultRoomName = KioskMain.getPath().getRoomNames().iterator().next();
        Node defaultNode = null;
        try {
            defaultNode = KioskMain.getPath().getRoom(defaultRoomName);
        } catch (RoomNotFoundException e) {
        }
        LocationType newType = LocationType.userValues()[0];
        if(dirType != null) {
            newType = dirType;
        }
        Location newLoc = new Location("", newType, defaultNode);
        locationsTable.getItems().add(0, newLoc);
        KioskMain.getDir().addLocation(newLoc);
    }

    @FXML
    private void clickRemove(ActionEvent event)throws IOException {
        if (selectedLocation != null) {
            KioskMain.getDir().removeLocation(selectedLocation);
            KioskMain.getUI().setScene(new ManageDirectoryViewController());
        }
    }


}
