package controllers;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import controllers.admin.AdminLoginController;
import controllers.admin.AdminMapController;
import controllers.map.MapController;
import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import sun.plugin.javascript.navig.Anchor;

import javax.swing.*;
import java.io.IOException;

public class MainMenuController extends AbstractController {

    @FXML
    private Button viewMapBtn;
    @FXML
    private Button viewDirectoryBtn;
    @FXML
    private Button adminBtn;
    @FXML
    private HBox mapContainer;
    @FXML
    private SplitPane splitPane;
    @FXML
    private AnchorPane root;

    @Override
    public String getURL() {
        return "views/MainMenu.fxml";
    }

    @FXML
    private void initialize() {
        // load the map controller
        MapController mapController = new MapController();
        // add the map to the container
        mapController.setFloor(KioskMain.getDir().getTheKiosk().getNode().getFloor());
        mapContainer.getChildren().add(mapController.getRoot());
        mapContainer.setAlignment(Pos.CENTER);
    }

    @FXML
    private void clickViewMap(ActionEvent event) {
        KioskMain.getUI().setScene(new MapViewController());
    }

    @FXML
    private void clickViewDirectory(ActionEvent event) {
        KioskMain.getUI().setScene(new DirectoryViewController());
    }

    @FXML
    private void clickAdmin(ActionEvent event) {
        AdminLoginController login = new AdminLoginController(root);
        login.showCentered();
    }

    @FXML
    private void clickAbout(ActionEvent event) {
        KioskMain.getUI().setScene(new AboutPageController());
    }

}
