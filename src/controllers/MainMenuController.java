package controllers;

import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    private static final String MAP_URL = "resources/4_thefourthfloor.png";

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


    @FXML
    private void initialize() {
    }

    @FXML
    private void clickViewMap(ActionEvent event) {
        KioskMain.setScene("views/MapView.fxml");
    }

    @FXML
    private void clickViewDirectory(ActionEvent event) {
        KioskMain.setScene("views/DirectoryView.fxml", false);
    }

    @FXML
    private void clickAdmin(ActionEvent event) {
        KioskMain.setScene("views/AdminLogin.fxml");
    }

}
