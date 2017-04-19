package controllers.admin;

import controllers.AbstractController;
import controllers.mapView.MapViewController;
import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Created by mattm on 3/29/2017.
 */
public class AdminLoginController extends AbstractController {

    @FXML
    private Button loginBtn;
    @FXML
    private Button backBtn;
    @FXML
    private PasswordField password;

    @FXML
    private void clickBack(ActionEvent event) {
        KioskMain.getUI().setScene(new MapViewController());
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
