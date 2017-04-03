package controllers;

import com.intellij.ide.actions.TechnicalSupportAction;
import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.commons.lang.ObjectUtils;


import java.io.IOException;

public class ManageDirectoryViewController {

    @FXML
    private Button backBtn;
    @FXML
    private Button addPhysician;
    @FXML
    private Button removePhysician;
    @FXML
    private Button editPhysician;
    @FXML
    private TextField removePerson;
    @FXML
    private TextField editPerson;
    @FXML
    private Label errorRemove;
    @FXML
    private Label errorEdit;




    @FXML
    private void clickBack(ActionEvent event) {
        KioskMain.setScene("views/AdminMenu.fxml");
    }

    @FXML
    private void clickAddPhysician(ActionEvent event) throws IOException {
        KioskMain.setScene("views/AdminAddPhysician.fxml");
    }

    @FXML
    private void clickRemovePhysician(ActionEvent event) throws IOException {
        String person = (String) removePerson.getText();
        //System.out.println("You did it: " + person);
        if (!person.equals("")) {
            KioskMain.setScene("views/AdminRemovePhysician.fxml");
        } else{
            //System.out.println("Error");
            errorRemove.setVisible(true);
        }
    }

    @FXML
    private void clickEditPhysician(ActionEvent event) throws IOException {
        String person = (String) editPerson.getText();
        //System.out.println("You did it: " +person );

        if (!person.equals("")) {
            KioskMain.setScene("views/AdminRemovePhysician.fxml");
        } else{
            //System.out.println("Error");
            errorEdit.setVisible(true);
        }
    }

}

