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
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController extends AbstractController {

    private static final Double DRAWER_RATE = -1.0;
    private static final Integer HAMBURGER_OPENED_X = 220;
    private static final Integer HAMBURGER_CLOSED_X = 10;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private JFXHamburger hamburger;

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

        DrawerController drawerController = new DrawerController();
        drawer.setSidePane(drawerController.getRoot());
        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);
        transition.setRate(DRAWER_RATE);
        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED,(e)->{
            transition.setRate(transition.getRate()*-1);
            transition.play();
            if(drawer.isShown()) {
                drawer.close();
            } else {
                drawer.open();
                hamburger.setVisible(false);
            }
        });
        drawer.setOnDrawerClosed(new EventHandler<JFXDrawerEvent>() {
            @Override
            public void handle(JFXDrawerEvent event) {
//                hamburger.setLayoutX(HAMBURGER_CLOSED_X);
                hamburger.setVisible(true);
            }
        });
//        drawer.setOnDrawerOpened(new EventHandler<JFXDrawerEvent>() {
//            @Override
//            public void handle(JFXDrawerEvent event) {
//                hamburger.setLayoutX(HAMBURGER_OPENED_X);
//            }
//        });
    }

}
