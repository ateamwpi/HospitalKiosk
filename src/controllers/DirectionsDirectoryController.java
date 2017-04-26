package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import models.dir.Location;

import java.util.function.Consumer;

/**
 * Created by Kevin O'Brien on 4/24/2017.
 */
public class DirectionsDirectoryController extends AbstractDirectoryViewController implements IPopup {

    private Parent parent;
    private JFXPopup instance;
    private Consumer<Location> onSelect;
    private Location destination;
    @FXML
    private JFXButton okButton;

    public DirectionsDirectoryController(Parent parent, Consumer<Location> onSelect) {
        this.parent = parent;
        this.onSelect = onSelect;
        this.instance = new JFXPopup(this.getRegion());
    }

    @Override
    public String getURL() {
        return "views/DirectionsDirectoryView.fxml";
    }

    @FXML
    private void initialize() {
        initializeTable();
        initializeFilter();
        setFullDirectory();
        addLocationBtns("alert-button", 150);
        okButton.setDisable(true);


        // listen to location table selection event
        locationsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (locationsTable.getSelectionModel().getSelectedItem() != null) {
                destination = newValue;
                okButton.setDisable(false);
            } else {
                okButton.setDisable(true);
            }
        });
    }

    @FXML
    private void clickBack(ActionEvent event) {
        this.instance.hide();
    }

    @FXML
    private void clickOK(ActionEvent event) {
        selectLocation(destination);
        getInstance().hide();
    }

    private void selectLocation(Location loc) {
        this.onSelect.accept(loc);
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


}
