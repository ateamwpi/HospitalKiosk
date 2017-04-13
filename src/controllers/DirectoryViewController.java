package controllers;

import controllers.admin.AdminMenuController;
import controllers.admin.AdminModifyLocationController;
import controllers.admin.ManageDirectoryViewController;
import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.dir.Location;
import models.dir.LocationType;
import models.path.Node;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by mattm on 3/29/2017.
 */

public class DirectoryViewController extends AbstractDirectoryViewController {

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
    @FXML
    private Button findNearest;


    DirectoryViewController() {}

    @Override
    public String getURL() {
        return "views/DirectoryView.fxml";
    }

    @FXML
    private void initialize() {
        initializeTable();
        initializeDropdown();
        initializeFilter();
        // choose direction mode
        directionMode();
        // listen to location table selection event
        locationsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (locationsTable.getSelectionModel().getSelectedItem() != null) {
                selectedLocation = newValue;
                goToFinalSel.setDisable(false); //enable -> button once selection has been made
            }
        });
        findNearest.setDisable(true);
        findNearest.setOpacity(0);
    }

    private void directionMode() {
        addEntry.setVisible(false);
        modifyEntry.setVisible(false);
        removeEntry.setVisible(false);
        title.setText("Select Starting Location");
        directions.setText("Select a starting location from the table above.\nOnce a location is selected, click the '->' button\n" +
                "to next choose a final destination.");
        //disable the -> button so user cannot move on until they have selected an entry
        goToFinalSel.setDisable(true);
    }

    @FXML  //when user clicks "back" button, they will return to main menu
    private void clickBack(ActionEvent event) {
        KioskMain.getUI().setScene(new MainMenuController());
    }

    @FXML  //when user clicks -> button, they will be brought to new page and asked to pick final destination
    private void clickGoToFinalSel(ActionEvent event) {
        getDirections();
    }

    private void getDirections() {
        if (startNode == null) {
            title.setText("Select Ending Location");
            directions.setText("Select an ending location from the table above. Once a location\nis selected, click the 'Get Path' button " +
                    "to view a path connecting\nthe selected starting and ending locations.");
            goToFinalSel.setText("Get Path");
            startNode = selectedLocation.getNode();
            updateNearestButton();
        } else {
            endNode = selectedLocation.getNode();
            KioskMain.getUI().setScene(new DirectionsViewController(this.startNode, this.endNode));
        }
    }

    @Override
    protected void selectDirectory(String s) {
        updateNearestButton();
        super.selectDirectory(s);
    }

    private void updateNearestButton() {
        if(startNode != null) {// selecting end location only
            if (LocationType.getType(locationDropdown.getSelectionModel().getSelectedItem()).hasNearest()) {
                findNearest.setDisable(false);
                findNearest.setOpacity(1);
            } else {
                findNearest.setDisable(true);
                findNearest.setOpacity(0);
            }
        }
    }

    @FXML
    private void clickKiosk(ActionEvent event) {
        selectedLocation = KioskMain.getDir().getTheKiosk();
        System.out.println(selectedLocation);
        getDirections();
        //KioskMain.getUI().setScene("views/FinalDestSelectionView.fxml", kiosk);
    }

    @FXML
    private void pressedFindNearest(ActionEvent event) {
        LocationType lt = LocationType.getType(locationDropdown.getSelectionModel().getSelectedItem());
        HashMap<Location, Double> near = KioskMain.getPath().getNearest(lt, startNode);
        Location min = Collections.min(near.entrySet(), (entry1, entry2) -> (int)entry1.getValue().doubleValue() - (int)entry2.getValue().doubleValue()).getKey();
        selectedLocation = min;
        getDirections();
    }


}

