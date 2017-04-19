package controllers.mapView;

import com.jfoenix.controls.JFXDrawer;
import controllers.AbstractController;
import controllers.map.MapController;
import controllers.mapView.DrawerController;
import core.KioskMain;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import models.path.Path;

public class MapViewController extends AbstractController {

    @FXML
    private JFXDrawer drawer;

    @FXML
    private Label drawerOpen;

    @FXML
    private StackPane mapContainer;
    private MapController mapController;

    @Override
    public String getURL() {
        return "views/MainMenu.fxml";
    }

    @FXML
    private void initialize() {
        // load the map controller
        mapController = new MapController();
        // add the map to the container
        mapController.setFloor(KioskMain.getDir().getTheKiosk().getNode().getFloor());
        mapContainer.getChildren().add(mapController.getRoot());
        // setup drawer
        DrawerController drawerController = new DrawerController(this::drawPath);
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

    private void drawPath(Path path) {
        mapController.drawPath(path);
        mapController.setFloor(path.getStart().getFloor());
    }

}
