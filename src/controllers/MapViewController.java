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
public class MapViewController {
    @FXML
    private Button floor1Btn;
    @FXML
    private Button floor2Btn;
    @FXML
    private Button floor3Btn;
    @FXML
    private Button floor4Btn;
    @FXML
    private Button floor5Btn;
    @FXML
    private Button floor6Btn;
    @FXML
    private Button floor7Btn;
    @FXML
    private Button floor8Btn;
    @FXML
    private Button backBtn;


    @FXML
    private void clickBack(ActionEvent event) throws IOException {
        KioskMain.setScene("views/MainMenu.fxml");
    }
}
