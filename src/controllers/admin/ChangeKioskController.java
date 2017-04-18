package controllers.admin;

import controllers.AbstractController;
import core.KioskMain;
import core.Utils;
import core.exception.RoomNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import models.path.Node;

/**
 * Created by Kevin O'Brien on 4/8/2017.
 */
public class ChangeKioskController extends AbstractController {
    @FXML
    private TextField room;
    

    @FXML
    private void initialize() {
        String curKiosk = KioskMain.getDir().getTheKiosk().getNode().getRoomName();
        room.setText(curKiosk);

    }

    @FXML
    private void clickBack(ActionEvent event) {
        KioskMain.getUI().setScene(new AdminMenuController());
    }

    @FXML
    private void clickSubmit(ActionEvent event) {
        try {
            Node kioskNode = KioskMain.getPath().getRoom(room.getText());
            KioskMain.getDir().getTheKiosk().setNode(kioskNode);
            KioskMain.getUI().setScene(new AdminMenuController());
        } catch(RoomNotFoundException e) {
            Utils.showAlert(getRoot(), "Invalid Room!", "Please enter a room that is currently in the database!");
            room.clear();
        }

    }

    @Override
    public String getURL() {
        return "views/ChangeKiosk.fxml";
    }
}
