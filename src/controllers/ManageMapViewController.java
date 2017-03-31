package controllers;

import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class ManageMapViewController {

    @FXML
    private Button backBtn;


    @FXML
    private void clickBack(ActionEvent event) throws IOException {
        KioskMain.setScene("views/AdminMenu.fxml");
    }
}
