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
public class DirectionsViewController {
    @FXML
    private Button backBtn;

    @FXML
    private void clickBack(ActionEvent event) {
        KioskMain.setScene("views/MainMenu.fxml");
    }
}
