package controllers.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import controllers.AbstractController;
import controllers.MainMenuController;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by mattm on 3/29/2017.
 */
public class AdminLoginController extends AbstractController {

    @FXML
    private JFXButton loginBtn;
    @FXML
    private JFXButton backBtn;
    @FXML
    private JFXPasswordField password;
    @FXML
    private AnchorPane root;

    @FXML
    private void clickBack(ActionEvent event) {
        KioskMain.getUI().setScene(new MainMenuController());
    }

    @FXML
    private void clickLogin(ActionEvent event) {
        checkPassword();
    }

    @FXML
    private void initialize() {
//        loginBtn.getStyleClass().add("button-raised");
    }

    @FXML
    private void keyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            checkPassword();
        }
    }

    private void checkPassword() {
        if (password.getText().equals("admin")) {
            KioskMain.getUI().setScene(new AdminMenuController());
        } else {
            Alert wrongPassword = new Alert(Alert.AlertType.WARNING);
            wrongPassword.setHeaderText("Wrong Password!");
            wrongPassword.setTitle("Access Denied!");
            wrongPassword.setContentText("Please enter the proper password...");
            wrongPassword.showAndWait();
            password.clear();
        }
    }

    @Override
    public String getURL() {
        return "views/AdminLogin.fxml";
    }
}
