package controllers.admin;

import controllers.AbstractController;
import controllers.AbstractDirectoryViewController;
import controllers.DirectionsViewController;
import controllers.MainMenuController;
import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import models.dir.Location;
import models.dir.LocationType;
import models.path.Node;

import java.io.IOException;

/**
 * Created by dylan on 4/9/17.
 */
public class ManageDirectoryViewController extends AbstractDirectoryViewController {

    private Node startNode; // The selected starting node for pathfinding
    private Node endNode;

    @FXML
    private Label title;
    @FXML
    private Button goToFinalSel; //button user will press to get path to selected
    @FXML
    private Label directions; //label to give user instructions
    @FXML
    private Button addEntry;
    @FXML
    private Button removeEntry;
    @FXML
    private Button kiosk;
    @FXML
    private VBox locationTypes;


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
        title.setText("Manage Directory");
        directions.setText("Add a new Location with the 'New' Button. To edit or remove a location, select a Location" +
                " from the table and press the corresponding button");
        addLocationBtns();

    }

    @FXML  //when user clicks "back" button, they will return to main menu
    private void clickBack(ActionEvent event) {
        KioskMain.getUI().setScene(new AdminMenuController());
    }

    @FXML
    private void clickModify(ActionEvent event) throws IOException {
        if (selectedLocation != null) {
            KioskMain.getUI().setScene(new AdminModifyLocationController(selectedLocation));
        }
    }

    @FXML
    private void clickAdd(ActionEvent event) throws IOException {
        //locationsTable.getItems().add(0, new Location());
        //KioskMain.getUI().setScene(new AdminAddLocationController());
    }

    @FXML
    private void clickRemove(ActionEvent event)throws IOException {
        if (selectedLocation != null) {
            KioskMain.getDir().removeLocation(selectedLocation);
            KioskMain.getUI().setScene(new ManageDirectoryViewController());
        }
    }

    @FXML
    private void addLocationBtns() {

        Button fulldir = new Button();
        fulldir.setText("Full Directory");
        fulldir.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setFullDirectory();
            }
        });
        locationTypes.getChildren().add(fulldir);

        for (LocationType locType : LocationType.userValues()) {
            Button loc = new Button();
            loc.setText(locType.name());
            loc.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    selectDirectory(locType);
                }
            });
            locationTypes.getChildren().add(loc);
        }

    }


}
