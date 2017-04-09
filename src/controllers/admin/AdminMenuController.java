package controllers.admin;

import controllers.AbstractController;
import controllers.ChangeKioskController;
import controllers.DirectoryViewController;
import controllers.MainMenuController;
import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by mattm on 3/29/2017.
 */
public class AdminMenuController extends AbstractController {
    @FXML
    private Button logoutBtn;
    @FXML
    private Button manageDirectoryBtn;
    @FXML
    private Button manageMapBtn;
    @FXML
    private Button kioskButton;

    @FXML
    private void clickLogout(ActionEvent event) {
        KioskMain.setScene(new MainMenuController());
    }

    @FXML
    private void clickManageDirectory(ActionEvent event) {
        KioskMain.setScene(new ManageDirectoryViewController());
    }

    @FXML
    private void clickManageMap(ActionEvent event) {
        KioskMain.setScene(new ManageMapViewController());
    }

    @FXML
    private void pressedKiosk(ActionEvent event) {
        KioskMain.setScene(new ChangeKioskController());
    }

    @Override
    public String getURL() {
        return "views/AdminMenu.fxml";
    }
}
