package controllers.LoginView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controllers.PopupView.AbstractPopupViewController;
import core.KioskMain;
import core.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Created by mattm on 3/29/2017.
 */
public class LoginViewController extends AbstractPopupViewController {

    @FXML
    private JFXButton loginBtn;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXTextField username;

    public LoginViewController(Parent parent) {
        super(parent);
    }

    @FXML
    private void initialize() {
        // bind event handlers
        password.setOnAction(this::checkPassword);
        loginBtn.setOnAction(this::checkPassword);
//        loginBtn.getStyleClass().add("button-raised");
    }

    private void checkPassword(ActionEvent event) {
        if(KioskMain.getLogin().tryLogin(username.getText(), password.getText())) {
            getInstance().hide();
            Utils.showAlert(getParent(), "Success!", "Welcome, " + KioskMain.getLogin().getState() + "!");
        }
        else {
            Utils.showAlert(getParent(), "Access Denied!", "The password was incorrect!\nPlease try again!");
        }
    }

    @Override
    public String getURL() {
        return "resources/views/LoginView/Login.fxml";
    }
}
