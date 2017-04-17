package controllers.admin;

import com.jfoenix.controls.JFXButton;
import controllers.AbstractController;
import controllers.MainMenuController;
import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * Created by mattm on 3/29/2017.
 */
public class AdminMenuController extends AbstractController {
    @FXML
    private JFXButton logoutBtn;
    @FXML
    private JFXButton manageDirectoryBtn;
    @FXML
    private JFXButton manageMapBtn;
    @FXML
    private JFXButton kioskButton;
    @FXML
    private Label title;
    @FXML
    private AnchorPane root;

    @FXML
    private void initialize(){
        title.prefWidthProperty().bind(root.widthProperty());
    }

    @FXML
    private void clickLogout(ActionEvent event) {
        KioskMain.getUI().setScene(new MainMenuController());
    }

    @FXML
    private void clickManageDirectory(ActionEvent event) {
        KioskMain.getUI().setScene(new ManageDirectoryViewController());
    }

    @FXML
    private void clickManageMap(ActionEvent event) {
        KioskMain.getUI().setScene(new ManageMapViewController());
    }

    @FXML
    private void pressedKiosk(ActionEvent event) {
        KioskMain.getUI().setScene(new ChangeKioskController());
    }

    @Override
    public String getURL() {
        return "views/AdminMenu.fxml";
    }




}
