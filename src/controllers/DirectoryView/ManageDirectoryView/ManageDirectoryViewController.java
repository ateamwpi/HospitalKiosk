package controllers.DirectoryView.ManageDirectoryView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextField;
import controllers.DirectoryView.AbstractDirectoryViewController;
import controllers.MapView.MapView.MapViewController;
import controllers.NavigationDrawer.MenuItem;
import controllers.NavigationDrawer.NavigationDrawerController;
import controllers.PopupView.IPopup;
import core.KioskMain;
import core.Utils;
import core.exception.RoomNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import models.dir.Location;
import models.dir.LocationType;
import models.path.Node;

/**
 * Created by dylan on 4/9/17.
 *
 */
public class ManageDirectoryViewController extends AbstractDirectoryViewController {

    @FXML
    protected JFXButton addEntry;
    @FXML
    protected JFXButton removeEntry;
    @FXML
    private JFXButton backButton;
    @FXML
    private Label hamburger;
    @FXML
    private JFXDrawer navigationDrawer;

    public ManageDirectoryViewController(Object... data) {
        super(data);
    }

    @Override
    public String getURL() {
        return "resources/views/DirectoryView/ManageDirectoryView/ManageDirectoryView.fxml";
    }

    @FXML
    private void initialize() {
        // bind event handlers
        //backButton.setOnAction(this::clickBack);
        addEntry.setOnAction(this::clickAdd);
        removeEntry.setOnAction(this::clickRemove);
        // setup table and search
        initializeTable();
        initializeFilter();
        setFullDirectory();
        // choose admin mode
        adminMode();
        addLocationBtns();
        // listen to location table selection event
        locationsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (locationsTable.getSelectionModel().getSelectedItem() != null) {
                selectedLocation = newValue;
                removeEntry.setDisable(false);
            } else {
                removeEntry.setDisable(true);
            }
        });

        // setup navigation drawer
        NavigationDrawerController navigationDrawerController = new NavigationDrawerController(getRoot(), MenuItem.EnumMenuItem.ManageDir);
        navigationDrawer.setSidePane(navigationDrawerController.getRoot());
        //optionsMenu.open();
        hamburger.setOnMouseClicked(event -> navigationDrawer.open());
        navigationDrawerController.getDrawerClose().setOnMouseClicked(event -> navigationDrawer.close());
        navigationDrawerController.getScrim().setOnMouseClicked(event -> navigationDrawer.close());
        navigationDrawerController.getScrim().prefWidthProperty().bind(KioskMain.getUI().getStage().widthProperty().add(100));
        navigationDrawerController.getScrim().prefHeightProperty().bind(KioskMain.getUI().getStage().heightProperty());
    }

    protected void adminMode() {
        setTableEdit();
        removeEntry.setDisable(true);
    }

    @FXML  //when user clicks "back" button, they will return to main menu
    private void clickBack(ActionEvent event) {
        KioskMain.getUI().setScene(new MapViewController());
    }

    @FXML
    protected void clickAdd(ActionEvent event) {
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
    protected void clickRemove(ActionEvent event) {
        if (selectedLocation != null) {
            Utils.showOption(getRoot(), "Delete Directory Entry", "Please confirm that you would like to delete " + selectedLocation.getName() + " from the directory.", "Cancel", "Delete", this::processRemove);
        }
    }

    private void processRemove(boolean delete) {
        if(delete) {
            KioskMain.getDir().removeLocation(selectedLocation);
            KioskMain.getUI().setScene(new ManageDirectoryViewController());
        }
    }
}
