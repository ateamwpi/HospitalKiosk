package controllers.MapView.MapView;

import com.jfoenix.controls.JFXDrawer;
import controllers.AbstractController;
import controllers.MapView.Map.MapController;
import controllers.NavigationDrawer.NavigationDrawerController;
import core.KioskMain;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

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
        return "resources/views/MapView/MapView/MapView.fxml";
    }

    @FXML
    private void initialize() {
        // load the map controller
        MapController mapController = new MapController();
        // add the map to the container
        mapController.setFloor(KioskMain.getDir().getTheKiosk().getNode().getFloor());
        mapContainer.getChildren().add(mapController.getRoot());
        // setup directions drawer
        DirectionsDrawerController directionsDrawerController = new DirectionsDrawerController(mapController);
        drawer.setSidePane(directionsDrawerController.getRoot());
        drawer.open();
        drawerOpen.setOnMouseClicked(event -> {
            drawerOpen.setVisible(false);
            drawer.open();
        });
        directionsDrawerController.getDrawerClose().setOnMouseClicked(event -> {
            drawer.close();
            drawerOpen.setVisible(true);
        });
        // setup navigation drawer
        NavigationDrawerController navigationDrawerController = new NavigationDrawerController(getRoot());
        optionsMenu.setSidePane(navigationDrawerController.getRoot());
        //optionsMenu.open();
        directionsDrawerController.getOptionsMenuButton().setOnMouseClicked(event -> optionsMenu.open());
        navigationDrawerController.getDrawerClose().setOnMouseClicked(event -> optionsMenu.close());
        navigationDrawerController.getScrim().setOnMouseClicked(event -> optionsMenu.close());
//        navigationDrawerController.getMenuItems().setOnMouseClicked(event -> {
//            optionsMenu.close();
//        });

        navigationDrawerController.getScrim().prefWidthProperty().bind(KioskMain.getUI().getStage().widthProperty().add(100));
        navigationDrawerController.getScrim().prefHeightProperty().bind(KioskMain.getUI().getStage().heightProperty());
    }

    public JFXDrawer getOptionsMenu() {
        return optionsMenu;
    }

}
