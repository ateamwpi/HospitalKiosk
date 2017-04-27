package controllers.DirectoryView.DirectoryView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import controllers.DirectoryView.AbstractDirectoryViewController;
import controllers.PopupView.IPopup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import models.dir.Location;

import java.util.function.Consumer;

/**
 * Created by Kevin O'Brien on 4/24/2017.
 */
public class DirectoryViewController extends AbstractDirectoryViewController implements IPopup {

    private Parent parent;
    private JFXPopup instance;
    private Consumer<Location> onSelect;
    private Location destination;
    @FXML
    private JFXButton okButton;
    @FXML
    private JFXButton backButton;

    public DirectoryViewController(Parent parent, Consumer<Location> onSelect, boolean needsAccept) {
        this.parent = parent;
        this.onSelect = onSelect;
        if(!needsAccept) {
            okButton.setVisible(false);
            okButton.setDisable(true);
        }
        this.instance = new JFXPopup(this.getRegion());

        this.getRoot().setOnKeyPressed(this::keySubmit);
        this.locationsTable.setOnKeyPressed(this::keySubmit);
    }

    private void keySubmit(KeyEvent event) {
        System.out.println("directory press");
        if (event.getCode().equals(KeyCode.ENTER) && this.destination != null) {
            selectLocation(destination);
            getInstance().hide();
            event.consume();
        }
    }

    @Override
    public String getURL() {
        return "resources/views/DirectoryView/DirectoryView/DirectoryView.fxml";
    }

    @FXML
    private void initialize() {
        initializeTable();
        initializeFilter();
        setFullDirectory();
        addLocationBtns("alert-button", 150);
        okButton.setDisable(true);

        backButton.setOnAction(this::clickBack);
        okButton.setOnAction(this::clickOK);

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
