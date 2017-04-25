package controllers.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import controllers.AbstractPopupController;
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
public class AdminLoginController extends AbstractPopupController {

    @FXML
    private JFXButton loginBtn;
    @FXML
    private JFXPasswordField password;

    public AdminLoginController(Parent parent) {
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
        if (event.getCode().equals(KeyCode.ENTER)) {
            checkPassword();
        }
    }

    private void checkPassword() {
        if (password.getText().equals("admin")) {
            this.getInstance().hide();
            KioskMain.getUI().setScene(new AdminMenuController());
        } else {
            Utils.showAlert(this.getParent(), "Access Denied!", "The password was incorrect!\nPlease try again!\n");
        }
    }

    @Override
    public String getURL() {
        return "views/AdminLogin.fxml";
    }
}
