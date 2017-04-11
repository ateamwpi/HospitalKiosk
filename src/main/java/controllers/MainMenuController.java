package controllers;

import controllers.admin.AdminLoginController;
import controllers.map.MapController;
import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;

public class MainMenuController extends AbstractController {

    @FXML
    private Button viewMapBtn;
    @FXML
    private Button viewDirectoryBtn;
    @FXML
    private Button adminBtn;
    @FXML
    private AnchorPane mapContainer;
    @FXML
    private SplitPane splitPane;
    @FXML
    private AnchorPane anchorPane;

    @Override
    public String getURL() {
        return "views/MainMenu.fxml";
    }

    @FXML
    private void initialize() {
        // load the map controller
        MapController mapController = new MapController();
        // add the map to the container
        mapContainer.getChildren().add(mapController.getRoot());
    }

    @FXML
    private void clickViewMap(ActionEvent event) {
        KioskMain.setScene(new MapViewController());
    }

    @FXML
    private void clickViewDirectory(ActionEvent event) {
        KioskMain.setScene(new DirectoryViewController());
    }

    @FXML
    private void clickAdmin(ActionEvent event) {
        KioskMain.setScene(new AdminLoginController());
    }

}
