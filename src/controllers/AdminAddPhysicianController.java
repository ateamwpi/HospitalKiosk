package controllers;

import core.KioskMain;
import core.RoomNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.dir.Location;
import models.dir.LocationType;

import java.sql.*;

import java.io.IOException;

/**
 * Created by Jonathan on 3/31/2017.
 */
public class AdminAddPhysicianController {
   private Connection conn;
   TextField source;
   TextField loc;

    @FXML
    private Button backBtn;
    @FXML
    private Button submitBtn;
    @FXML
    private TextField name;
    @FXML
    private TextField place;
    @FXML
    private Label errorSubmit;



    @FXML
    private void clickBack(ActionEvent event) throws IOException {
        //back button goes to Admin menu
        KioskMain.setScene("views/ManageDirectoryView.fxml");
    }


    @FXML
    private void clickSubmit(ActionEvent event) throws IOException {
        //grabs value from textfield when submitBtn is pressed
        String person = (String) name.getText();
        String loc = (String) place.getText();
        //System.out.println("You did it: " +person +loc );

        //calls on a helper function that adds the given information from the user to the database
        try{
            Location L = new Location(person, LocationType.Physician, KioskMain.getPath().getRoom(loc));
            KioskMain.getDir().addLocation(L);
            KioskMain.setScene("views/AdminAddPhysician.fxml");
        }
        catch(RoomNotFoundException e){
            Alert invalidRoom = new Alert(Alert.AlertType.ERROR);
            invalidRoom.setHeaderText("Invalid Room!");
            invalidRoom.setTitle("Try Again!");
            invalidRoom.setContentText("Please enter a room that is currently in the database!");
            invalidRoom.showAndWait();
            place.clear();
        }
    }
}
