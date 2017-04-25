package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextField;
import core.KioskMain;
import core.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;

/**
 * Created by mattm on 3/29/2017.
 */
public class LoginController extends AbstractController implements IPopup {

    @FXML
    private JFXButton loginBtn;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXTextField username;
    private Parent parent;
    private JFXPopup instance;

    public LoginController(Parent parent) {
        this.parent = parent;
        this.instance = new JFXPopup(this.getRegion());
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
        if(KioskMain.getLogin().tryLogin(username.getText(), password.getText())) {
            this.getInstance().hide();
            Utils.showAlert(this.getParent(), "Success!", "Welcome, " + KioskMain.getLogin().getState() + "!");
        }
        else {
            Utils.showAlert(this.getParent(), "Access Denied!", "The password was incorrect!\nPlease try again!");
        }
    }

    @Override
    public String getURL() {
        return "views/Login.fxml";
    }

    @Override
    public JFXPopup getInstance() {
        return this.instance;
    }

    @Override
    public Region getRegion() {
        return (Region) this.getRoot();
    }

    @Override
    public Parent getParent() {
        return this.parent;
    }
}
