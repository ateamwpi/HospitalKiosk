package controllers;

import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.dir.LocationType;
import models.path.Node;

import java.io.IOException;

/**
 * Created by mattm on 3/29/2017.
 */

public class DirectoryViewController extends AbstractDirectoryController implements IControllerWithParams {

    private Node startNode; // The selected starting node for pathfinding

    private Node endNode;

    boolean adminMode = false;

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



    public DirectoryViewController() {
        super();
    }

    /**
     * Used to specify whether to enable admin mode or not.
     * @param data Boolean ... True:Admin, False: User
     */
    @Override
    public void initData(Object... data) {
        if (data.length > 0) {
            adminMode = (Boolean) data[0];
        }
        if (adminMode) {
            adminMode();
        } else {
            directionMode();
        }
        locationsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (locationsTable.getSelectionModel().getSelectedItem() != null) {
                this.selectedLocation = newValue;
                if(adminMode) {

                } else {
                    goToFinalSel.setDisable(false); //enable -> button once selection has been made
                }

            }
        });
    }

    @FXML
    private void initialize() {
        initializeTable();
        initializeDropdown();
        initializeFilter();
    }

    private void adminMode() {
        kiosk.setVisible(false);
        goToFinalSel.setVisible(false);
        title.setText("Manage Directory");
        directions.setText("Add a new Location with the 'New' Button. To edit or remove a location, select a Location" +
               " from the table and press the corresponding button");
    }

    private void directionMode() {
        addEntry.setVisible(false);
        modifyEntry.setVisible(false);
        removeEntry.setVisible(false);
        title.setText("Select Starting Location");
        directions.setText("Select a starting location from the table above. Once a location is selected, click the '->' button " +
                "to next choose a final destination.");
        //disable the -> button so user cannot move on until they have selected an entry
        goToFinalSel.setDisable(true);
    }

    @FXML  //when user clicks "back" button, they will return to main menu
    private void clickBack(ActionEvent event) {
        KioskMain.setScene("views/MainMenu.fxml");
    }

    @FXML  //when user clicks -> button, they will be brought to new page and asked to pick final destination
    private void clickGoToFinalSel(ActionEvent event) {
        getDirections();
    }
    private void getDirections() {
        if (startNode == null) {
            title.setText("Select Ending Location");
            directions.setText("Select an ending location from the table above. Once a location is selected, click the 'Get Path' button " +
                    "to view a path connecting the  selected starting and ending locations.");
            goToFinalSel.setText("Get Path");
            startNode = selectedLocation.getNode();
        } else {
            endNode = selectedLocation.getNode();
            KioskMain.setScene("views/DirectionsView.fxml", this.startNode, this.endNode);
        }
    }

    @FXML
    private void clickModify(ActionEvent event) throws IOException {
        KioskMain.setScene("views/AdminModifyLocation.fxml", selectedLocation);
    }

    @FXML
    private void clickAdd(ActionEvent event) throws IOException {
        KioskMain.setScene("views/AdminModifyLocation.fxml");
    }

    @FXML
    private void clickRemove(ActionEvent event)throws IOException {
        KioskMain.getDir().removeLocation(selectedLocation);
        KioskMain.setScene("views/DirectoryView.fxml", true);
    }

    @FXML
    private void clickKiosk(ActionEvent event) {
        // TODO make this not hard-coded
        selectedLocation = KioskMain.getDir().getDirectory(LocationType.Kiosk).getLocation(110);
        getDirections();
        //KioskMain.setScene("views/FinalDestSelectionView.fxml", kiosk);
    }


}

