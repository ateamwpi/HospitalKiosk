package controllers.admin;

import core.KioskMain;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by mattm on 3/29/2017.
 */
public class AdminLoginController {

    @FXML
    private Button loginBtn;
    @FXML
    private Button backBtn;
    @FXML
    private PasswordField password;


    @FXML
    private void clickBack(ActionEvent event) {
        KioskMain.setScene("views/MainMenu.fxml");
    }

    @FXML
    private void clickLogin(ActionEvent event) {
        checkPassword();
    }

    @FXML
    private void keyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            checkPassword();
        }
    }

    private void checkPassword() {
        if (password.getText().equals("admin")) {
            KioskMain.setScene("views/AdminMenu.fxml");
        } else {
            Alert wrongPassword = new Alert(Alert.AlertType.WARNING);
            wrongPassword.setHeaderText("Wrong Password!");
            wrongPassword.setTitle("Access Denied!");
            wrongPassword.setContentText("Please enter the proper password...");
            wrongPassword.showAndWait();
            password.clear();
        }
    }
}
