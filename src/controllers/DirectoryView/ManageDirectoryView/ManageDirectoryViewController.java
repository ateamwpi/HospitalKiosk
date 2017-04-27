package controllers.DirectoryView.ManageDirectoryView;

import com.jfoenix.controls.JFXButton;
import controllers.DirectoryView.AbstractDirectoryViewController;
import controllers.MapView.MapView.MapViewController;
import core.KioskMain;
import core.exception.RoomNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.dir.Location;
import models.dir.LocationType;
import models.path.Node;

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
    @FXML
    private JFXButton backButton;

    @Override
    public String getURL() {
        return "resources/views/DirectoryView/ManageDirectoryView/ManageDirectoryView.fxml";
    }

    @FXML
    private void initialize() {
        // bind event handlers
        backButton.setOnAction(this::clickBack);
        addEntry.setOnAction(this::clickAdd);
        removeEntry.setOnAction(this::clickRemove);
        // setup table and search
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
        KioskMain.getUI().setScene(new MapViewController());
    }

    @FXML
    private void clickAdd(ActionEvent event) {
        String defaultRoomName = KioskMain.getPath().getRoomNames().iterator().next();
        Node defaultNode = null;
        try {
            defaultNode = KioskMain.getPath().getRoom(defaultRoomName);
        } catch (RoomNotFoundException e) {
            // TODO
        }
        LocationType newType = LocationType.userValues()[0];
        if(dirType != null) {
            newType = dirType;
        }
        Location newLoc = new Location(newType, defaultNode);
        locationsTable.getItems().add(0, newLoc);
        KioskMain.getDir().addLocation(newLoc);
    }

    @FXML
    private void clickRemove(ActionEvent event) {
        if (selectedLocation != null) {
            KioskMain.getDir().removeLocation(selectedLocation);
            KioskMain.getUI().setScene(new ManageDirectoryViewController());
        }
    }


}
