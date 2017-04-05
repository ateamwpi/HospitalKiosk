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

/**
 * Created by mattm on 3/29/2017.
 */
public class AdminMenuController {
    @FXML
    private Button logoutBtn;
    @FXML
    private Button manageDirectoryBtn;
    @FXML
    private Button manageMapBtn;

    @FXML
    private void clickLogout(ActionEvent event) {
        KioskMain.setScene("views/MainMenu.fxml");
    }

    @FXML
    private void clickManageDirectory(ActionEvent event) {
        KioskMain.setScene("views/ManageDirectoryView.fxml");
    }

    @FXML
    private void clickManageMap(ActionEvent event) {
        KioskMain.setScene("views/ManageMapView.fxml");
    }


}
