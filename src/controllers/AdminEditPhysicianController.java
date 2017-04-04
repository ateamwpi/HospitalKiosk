package controllers;

import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

/**
 * Created by Jonathan on 3/31/2017.
 */
public class AdminEditPhysicianController {

    @FXML
    private Button backBtn;

    @FXML
    private void clickBack(ActionEvent event) throws IOException {
        KioskMain.setScene("views/ManageDirectoryView.fxml");
    }
}
//KioskMain.getDir().addLocation or removeLocation()