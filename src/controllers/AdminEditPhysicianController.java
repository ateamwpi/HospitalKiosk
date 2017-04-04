package controllers;

import core.KioskMain;
import core.RoomNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import models.dir.Location;

import java.io.IOException;

/**
 * Created by Jonathan on 3/31/2017.
 */
public class AdminEditPhysicianController implements IControllerWithParams {

    @FXML
    private Button backBtn;
    @FXML
    private TextField name;
    @FXML
    private TextField room;
    @FXML
    private Button submit;

    private Location physician;

    @FXML
    private void clickBack(ActionEvent event) throws IOException {
        KioskMain.setScene("views/ManageDirectoryView.fxml");
    }

    @Override
    public void initData(Object... data) {
        for(Object o : data) {
            this.physician = (Location) o;
        }
        name.setText(physician.getName());
        room.setText(physician.getNode().getRoomName());
    }

    @FXML
    private void clickSubmit(ActionEvent event) throws IOException {
        if (!name.getText().equals(physician.getName())) {
            physician.setName(name.getText());
        }
        if (!room.getText().equals(physician.getNode().getRoomName())) {
            try {
                physician.setNode(KioskMain.getPath().getRoom(room.getText()));
                KioskMain.setScene("views/ManageDirectoryView.fxml");
            } catch(RoomNotFoundException e) {
                Alert invalidRoom = new Alert(Alert.AlertType.ERROR);
                invalidRoom.setHeaderText("Invalid Room!");
                invalidRoom.setTitle("Try Again!");
                invalidRoom.setContentText("Please enter a room that is currently in the database!");
                invalidRoom.showAndWait();
                room.clear();
            }
        }
    }

}
//KioskMain.getDir().addLocation or removeLocation()