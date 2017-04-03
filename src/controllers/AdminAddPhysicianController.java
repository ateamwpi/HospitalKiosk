package controllers;

import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
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

        //person, physician, node
        Location L = new Location(person, LocationType.Physician, KioskMain.getPath().getRoom(loc));
        KioskMain.getDir().addLocation(L);
    }
}
