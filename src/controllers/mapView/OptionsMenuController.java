package controllers.mapView;

import com.jfoenix.controls.JFXTextField;
import controllers.AboutPageController;
import controllers.AbstractController;
import controllers.OptionAlertController;
import controllers.admin.AdminLoginController;
import core.KioskMain;
import core.exception.FloorNotReachableException;
import core.exception.NearestNotFoundException;
import core.exception.PathNotFoundException;
import javafx.fxml.FXML;
import javafx.scene.Parent;
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

    private Parent mainRoot;

    public OptionsMenuController(Parent mainRoot) {
        super(mainRoot);
    }

    @Override
    public void initData(Object... data) {
        this.mainRoot = (Parent)data[0];
    }

    @FXML
    private void initialize() {
        adminButton.setOnMouseClicked(event -> {
            AdminLoginController login = new AdminLoginController(mainRoot);
            login.showCentered();
        });

        aboutButton.setOnMouseClicked(event -> {
            KioskMain.getUI().setScene(new AboutPageController());
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
