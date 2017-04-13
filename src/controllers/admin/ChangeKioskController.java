package controllers.admin;

import controllers.AbstractController;
import core.KioskMain;
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
        KioskMain.setScene(new AdminMenuController());
    }

    @FXML
    private void clickSubmit(ActionEvent event) {
        try {
            Node kioskNode = KioskMain.getPath().getRoom(room.getText());
            KioskMain.getDir().getTheKiosk().setNode(kioskNode);
            KioskMain.setScene(new AdminMenuController());
        } catch(RoomNotFoundException e) {
            Alert invalidRoom = new Alert(Alert.AlertType.ERROR);
            invalidRoom.setHeaderText("Invalid Room!");
            invalidRoom.setTitle("Try Again!");
            invalidRoom.setContentText("Please enter a room that is currently in the database!");
            invalidRoom.showAndWait();
            room.clear();
        }

    }

    @Override
    public String getURL() {
        return "views/ChangeKiosk.fxml";
    }
}
