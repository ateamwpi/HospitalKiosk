package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import controllers.admin.AdminMenuController;
import core.KioskMain;
import core.Utils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Created by mattm on 3/29/2017.
 */
public class LoginController extends AbstractPopupController {

    @FXML
    private JFXButton loginBtn;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXTextField username;

    public LoginController(Parent parent) {
        super(parent);
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
        if (event.getCode().equals(KeyCode.ENTER) && this.hasFocus()) {
            checkPassword();
        }
    }

    private void checkPassword() {
        if(KioskMain.getLogin().tryLogin(username.getText(), password.getText())) {
            this.getInstance().hide();
            Utils.showAlert(this.getParent(), "Success!", "Welcome, " + KioskMain.getLogin().getState() + "!");
        }
        else {
            this.loginBtn.requestFocus();
            Utils.showAlert(this.getParent(), "Access Denied!", "The password was incorrect!\nPlease try again!", this::regainFocus);
        }
    }

    private void regainFocus(Event e) {
        this.password.requestFocus();
        KioskMain.getUI().setPopup(this);
    }

    @Override
    public String getURL() {
        return "views/Login.fxml";
    }
}
