package controllers.MapView.MapView;

import com.jfoenix.controls.JFXDrawer;
import controllers.AbstractController;
import controllers.MapView.Map.MapController;
import controllers.MapView.MapView.DirectionsDrawer.DirectionsDrawerController;
import controllers.NavigationDrawer.NavigationDrawerController;
import core.KioskMain;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MapViewController extends AbstractController {

    @FXML
    private JFXDrawer directionsDrawer;

    @FXML
    private JFXDrawer navigationDrawer;

    @FXML
    private Pane drawerOpen;
    @FXML
    private StackPane mapContainer;

    @Override
    public String getURL() {
        return "resources/views/MapView/MapView/MapView.fxml";
    }

    @FXML
    private void initialize() {
        // bind event handlers
        drawerOpen.setOnMouseClicked(event -> {
            drawerOpen.setVisible(false);
            directionsDrawer.open();
        });
        directionsDrawer.setOnDrawerOpened(event -> drawerOpen.setVisible(false));
        directionsDrawer.setOnDrawerClosed(event -> drawerOpen.setVisible(true));
        // load the map controller
        MapController mapController = new MapController();
        // add the map to the container
        mapController.setFloor(KioskMain.getDir().getTheKiosk().getNode().getFloor());
        mapContainer.getChildren().add(mapController.getRoot());
        // setup directions drawer
        DirectionsDrawerController directionsDrawerController = new DirectionsDrawerController(mapController, getRoot());
        directionsDrawer.setSidePane(directionsDrawerController.getRoot());
        drawerOpen.setVisible(false);
        directionsDrawer.open();
        directionsDrawerController.getDrawerClose().setOnMouseClicked(event -> directionsDrawer.close());
        // setup navigation drawer
        NavigationDrawerController navigationDrawerController = new NavigationDrawerController(getRoot());
        navigationDrawer.setSidePane(navigationDrawerController.getRoot());
        //navigationDrawer.open();
        directionsDrawerController.getOptionsMenuButton().setOnMouseClicked(event -> navigationDrawer.open());
        navigationDrawerController.getDrawerClose().setOnMouseClicked(event -> navigationDrawer.close());
        navigationDrawerController.getScrim().setOnMouseClicked(event -> navigationDrawer.close());
//        navigationDrawerController.getMenuItems().setOnMouseClicked(event -> {
//            navigationDrawer.close();
//        });

        navigationDrawerController.getScrim().prefWidthProperty().bind(KioskMain.getUI().getStage().widthProperty().add(100));
        navigationDrawerController.getScrim().prefHeightProperty().bind(KioskMain.getUI().getStage().heightProperty());
    }

    public JFXDrawer getNavigationDrawer() {
        return navigationDrawer;
    }

}
