package controllers;

import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private Button viewMapBtn;
    @FXML
    private Button viewDirectoryBtn;
    @FXML
    private Button adminBtn;


    @FXML
    private void clickViewMap(ActionEvent event) throws IOException {
        KioskMain.setScene("views/MapView.fxml");
    }

    @FXML
    private void clickViewDirectory(ActionEvent event) throws IOException {
        KioskMain.setScene("views/DirectoryView.fxml");
    }

    @FXML
    private void clickAdmin(ActionEvent event) throws IOException {
        KioskMain.setScene("views/AdminLogin.fxml");
    }

}
