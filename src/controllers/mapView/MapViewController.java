package controllers.mapView;

import com.jfoenix.controls.JFXDrawer;
import controllers.AbstractController;
import controllers.map.MapController;
import controllers.mapView.DrawerController;
import core.KioskMain;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import models.ui.UIManager;

public class MapViewController extends AbstractController {

    @FXML
    private JFXDrawer drawer;

    @FXML
    private JFXDrawer optionsMenu;

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
        DrawerController drawerController = new DrawerController(path -> mapController.drawPath(path));
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

        OptionsMenuController menuController = new OptionsMenuController(getRoot());
        optionsMenu.setSidePane(menuController.getRoot());
        //optionsMenu.open();
        drawerController.getOptionsMenuButton().setOnMouseClicked(event -> {
            optionsMenu.open();
        });
        menuController.getDrawerClose().setOnMouseClicked(event -> {
            optionsMenu.close();
        });
        menuController.getScrim().setOnMouseClicked(event -> {
            optionsMenu.close();
        });

        menuController.getScrim().prefWidthProperty().bind(KioskMain.getUI().getStage().widthProperty().add(100));
        menuController.getScrim().prefHeightProperty().bind(KioskMain.getUI().getStage().heightProperty());
    }

}
