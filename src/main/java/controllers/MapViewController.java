package controllers;

import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Created by mattm on 3/29/2017.
 */
public class MapViewController extends AbstractController {
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

    @Override
    public String getURL() {
        return "views/MapView.fxml";
    }

    @FXML
    private void clickBack(ActionEvent event) {
        KioskMain.setScene(new MainMenuController());
    }
}
