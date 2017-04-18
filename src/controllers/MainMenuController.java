package controllers;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.events.JFXDrawerEvent;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import controllers.admin.AdminLoginController;
import controllers.admin.AdminMapController;
import controllers.map.MapController;
import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController extends AbstractController {

    @FXML
    private JFXDrawer drawer;

    @FXML
    private Label drawerOpen;

    @FXML
    private StackPane mapContainer;

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
        // setup drawer
        DrawerController drawerController = new DrawerController();
        drawer.setSidePane(drawerController.getRoot());
        drawer.open();
        drawerOpen.setOnMouseClicked(event -> {
            drawerOpen.setVisible(false);
            drawer.open();
        });
        drawerController.getDrawerClose().setOnMouseClicked(event -> {
            drawer.close();
            drawerOpen.setVisible(true);
        });
    }

}
