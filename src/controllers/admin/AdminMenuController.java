package controllers.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import controllers.AbstractController;
import controllers.MainMenuController;
import core.KioskMain;
import core.exception.RoomNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import models.path.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

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
    private JFXComboBox<String> kioskLocations;
    @FXML
    private JFXComboBox<String> pathAlgos;

    private JFXButton kioskButton;
    @FXML
    private Label title;
    @FXML
    private AnchorPane root;

    @FXML
    private void initialize(){
        title.prefWidthProperty().bind(root.widthProperty());

        pathAlgos.getItems().addAll(KioskMain.getPath().getAlgorithms());
        pathAlgos.getSelectionModel().select(KioskMain.getPath().getSelectedAlgorithm().getName());
        pathAlgos.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            KioskMain.getPath().selectAlgorithm(newValue);
        }));


        kioskLocations.getItems().addAll(KioskMain.getPath().getRoomNames());
        kioskLocations.getSelectionModel().select(KioskMain.getDir().getTheKiosk().getNode().getRoomName());
        kioskLocations.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            try {
                Node newNode = KioskMain.getPath().getRoom(newValue);
                KioskMain.getDir().getTheKiosk().setNode(newNode);
            } catch (RoomNotFoundException e) {
                e.printStackTrace();
            }
        }));
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


    @Override
    public String getURL() {
        return "views/AdminMenu.fxml";
    }




}
