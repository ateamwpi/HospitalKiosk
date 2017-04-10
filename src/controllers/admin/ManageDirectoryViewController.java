package controllers.admin;

import controllers.AbstractController;
import controllers.AbstractDirectoryViewController;
import controllers.DirectionsViewController;
import controllers.MainMenuController;
import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private Button modifyEntry;
    @FXML
    private Button addEntry;
    @FXML
    private Button removeEntry;
    @FXML
    private Button kiosk;


    ManageDirectoryViewController() {}

    @Override
    public String getURL() {
        return "views/ManageDirectoryView.fxml";
    }

    @FXML
    private void initialize() {
        initializeTable();
        initializeDropdown();
        initializeFilter();
        // choose admin mode
            adminMode();
        // listen to location table selection event
        locationsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (locationsTable.getSelectionModel().getSelectedItem() != null) {
                selectedLocation = newValue;
                removeEntry.setDisable(false);
                modifyEntry.setDisable(false);
            }
        });
    }

    private void adminMode() {
        removeEntry.setDisable(true);
        modifyEntry.setDisable(true);
        kiosk.setVisible(false);
        goToFinalSel.setVisible(false);
        title.setText("Manage Directory");
        directions.setText("Add a new Location with the 'New' Button. To edit or remove a location, select a Location" +
                " from the table and press the corresponding button");
    }

    @FXML  //when user clicks "back" button, they will return to main menu
    private void clickBack(ActionEvent event) {
        KioskMain.setScene(new AdminMenuController());
    }

    @FXML
    private void clickModify(ActionEvent event) throws IOException {
        if (selectedLocation != null) {
            KioskMain.setScene(new AdminModifyLocationController(selectedLocation));
        }
    }

    @FXML
    private void clickAdd(ActionEvent event) throws IOException {
        KioskMain.setScene(new AdminAddLocationController());
    }

    @FXML
    private void clickRemove(ActionEvent event)throws IOException {
        if (selectedLocation != null) {
            KioskMain.getDir().removeLocation(selectedLocation);
            KioskMain.setScene(new ManageDirectoryViewController());
        }
    }

}
