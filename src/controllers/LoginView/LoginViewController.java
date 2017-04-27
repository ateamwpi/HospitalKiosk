package controllers.LoginView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextField;
import controllers.AbstractController;
import controllers.PopupView.IPopup;
import core.KioskMain;
import core.Utils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.Region;

/**
 * Created by mattm on 3/29/2017.
 */
public class LoginViewController extends AbstractController implements IPopup {

    @FXML
    private JFXButton loginBtn;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXTextField username;
    private Parent parent;
    private JFXPopup instance;

    public LoginViewController(Parent parent) {
        this.parent = parent;
        this.instance = new JFXPopup(this.getRegion());
    }

    @FXML
    private void initialize() {
        // bind event handlers
        password.setOnAction(this::checkPassword);
        username.setOnAction(this::checkPassword);
        loginBtn.setOnAction(this::checkPassword);
//        loginBtn.getStyleClass().add("button-raised");
    }

    private void checkPassword(ActionEvent event) {
        if(this.hasFocus() && KioskMain.getLogin().tryLogin(username.getText(), password.getText())) {
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
        return "resources/views/LoginView/Login.fxml";
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
