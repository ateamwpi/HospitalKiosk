package controllers.mapView;

import com.jfoenix.controls.JFXTextField;
import controllers.AbstractController;
import core.KioskMain;
import core.exception.FloorNotReachableException;
import core.exception.NearestNotFoundException;
import core.exception.PathNotFoundException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import models.dir.Location;
import models.path.Path;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by dylan on 4/16/17.
 */
public class OptionsMenuController extends AbstractController {

    @FXML
    private Label drawerClose;
    @FXML
    private Pane scrim;
    @FXML
    private HBox adminButton;
    @FXML
    private HBox aboutButton;

    @Override
    public void initData(Object... data) {

    }

    @FXML
    private void initialize() {
        adminButton.setOnMouseClicked(event -> {

        });

        aboutButton.setOnMouseClicked(event -> {
            
        });
    }

    public Label getDrawerClose() {
        return drawerClose;
    }

    public Pane getScrim() {
        return this.scrim;
    }

    @Override
    public String getURL() {
        return "views/OptionsMenu.fxml";
    }
}
