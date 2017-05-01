package controllers.DirectoryView.ManageDirectoryView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXPopup;
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
public class ManageDirectoryPopupController extends ManageDirectoryViewController implements IPopup {

    private Parent parent;
    private Node node;
    private JFXPopup instance;
    @FXML
    private Label title;

    public ManageDirectoryPopupController(Parent parent, Node node) {
        super(node);
        this.parent = parent;
        this.instance = new JFXPopup(getRegion());
    }

    @Override
    public void initData(Object... data) {
        super.initData(null);
        this.node = (Node)data[0];
    }

    @FXML
    private void initialize() {
        // bind event handlers
        //backButton.setOnAction(this::clickBack);
        addEntry.setOnAction(this::clickAdd);
        removeEntry.setOnAction(this::clickRemove);
        // setup table and search
        initializeTable();
        setFullDirectory();
        this.selectLocations(this.node.getLocations());
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

    @Override
    protected void clickAdd(ActionEvent event) {
        LocationType newType = LocationType.userValues()[0];
        if(dirType != null) {
            newType = dirType;
        }
        Location newLoc = new Location(newType, this.node);
        locationsTable.getItems().add(0, newLoc);
        KioskMain.getDir().addLocation(newLoc);
    }

    @Override
    protected void clickRemove(ActionEvent event) {
        if (selectedLocation != null) {
            Utils.showOption(parent, "Delete Directory Entry", "Please confirm that you would like to delete " + selectedLocation.getName() + " from the directory.", "Cancel", "Delete", this::processRemove);
        }
    }

    private void processRemove(boolean delete) {
        if(delete) {
            KioskMain.getDir().removeLocation(selectedLocation);
            this.selectLocations(this.node.getLocations());
        }
    }

    @Override
    public JFXPopup getInstance() {
        return this.instance;
    }

    @Override
    public Region getRegion() {
        return (Region) this.getRoot();
    }

    @Override
    public Parent getParent() {
        return this.parent;
    }

    @Override
    public String getURL() {
        return "resources/views/DirectoryView/ManageDirectoryView/ManageDirectoryPopup.fxml";
    }

}
