package controllers;

import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
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

        Statement stmt;
        ResultSet rset;
        ResultSet rset2;
        ResultSet rset3;
        if (!person.equals("") && !loc.equals("")) {
            try {
                //retrieves nodeid from users input location and stores the nodeid
                stmt = conn.createStatement();
                rset = stmt.executeQuery("SELECT NODEID FROM LOCATION WERE NAME = " + loc);
                int newNodeId = 0;
                while (rset.next()) {
                    newNodeId = rset.getInt("NODEID");
                }
                rset.close();

                //retrieves max id, stores it, and increments the value by 1
                rset2 = stmt.executeQuery("SELECT MAX(ID) FROM LOCATION GROUP BY ID");
                int newId = 0;
                while (rset2.next()) {
                    newId = rset2.getInt("ID");
                }
                newId++;
                rset2.close();

                //Inserts new id, person, title, and nodeid into database
                rset3 = stmt.executeQuery("INSERT INTO LOCATION VALUES( " + newId + ", " + person + ", 'physician', " + newNodeId + ")");
                rset3.close();
            } catch (SQLException e) {
                System.out.println("Failed to insert data");
                e.printStackTrace();
            }
        } else {
            errorSubmit.setVisible(true);
        }
    }
}
