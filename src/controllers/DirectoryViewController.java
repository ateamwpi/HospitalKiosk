package controllers;

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
public class DirectoryViewController {
    @FXML
    private Button backBtn;
    @FXML
    private Button fullDirectoryBtn;
    @FXML
    private Button professionalsBtn;
    @FXML
    private Button departmentsBtn;
    @FXML
    private Button directoryOtherBtn;
    @FXML
    private Button frequentLocationsBtn;

    @FXML
    private void clickBack(ActionEvent event) throws IOException {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/MainMenu.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
