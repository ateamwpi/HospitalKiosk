package controllers.mapView;

import com.jfoenix.controls.JFXDrawer;
import controllers.AbstractController;
import controllers.map.MapController;
import core.KioskMain;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import models.path.Path;

public class MapViewController extends AbstractController {

    @FXML
    private JFXDrawer drawer;

    @FXML
    private JFXDrawer optionsMenu;

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
        DrawerController drawerController = new DrawerController(mapController, getRoot());
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
//        menuController.getMenuItems().setOnMouseClicked(event -> {
//            optionsMenu.close();
//        });

        menuController.getScrim().prefWidthProperty().bind(KioskMain.getUI().getStage().widthProperty().add(100));
        menuController.getScrim().prefHeightProperty().bind(KioskMain.getUI().getStage().heightProperty());
    }

    public JFXDrawer getOptionsMenu() {
        return optionsMenu;
    }

}
