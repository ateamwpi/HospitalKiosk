package controllers;

import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import models.dir.Location;

/**
 * Created by Kevin O'Brien on 4/24/2017.
 */
public class DirectionsDirectoryController extends AbstractDirectoryViewController {

    private

    public DirectionsDirectoryController() {}

    @Override
    public String getURL() {
        return "views/DirectionsDirectoryView.fxml";
    }



    @FXML
    private void initialize() {
        initializeTable();
        initializeFilter();
        setFullDirectory();

        // listen to location table selection event
        locationsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (locationsTable.getSelectionModel().getSelectedItem() != null) {
                locationsTable.getItems().remove(newValue);
                selectLocation(newValue);
            }
        });
    }

    @FXML
    private void clickBack(ActionEvent event) {
    }

    private void selectLocation(Location loc) {

    }

    private void selectStart(Location start) {

    }

    private void selectEnd(Location end) {

    }


}
