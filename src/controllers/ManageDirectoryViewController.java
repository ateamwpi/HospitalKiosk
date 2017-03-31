package controllers;

import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

import java.io.IOException;

public class ManageDirectoryViewController {

    @FXML
    private Button backBtn;


    @FXML
    private void clickBack(ActionEvent event) throws IOException {
        KioskMain.setScene("views/AdminMenu.fxml");
    }
}
