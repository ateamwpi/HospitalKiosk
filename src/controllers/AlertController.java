package controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * Created by mattm on 3/29/2017.
 */
public class AlertController extends AbstractPopupController {

    @FXML
    private JFXButton okButton;
    @FXML
    private Label alertTitle;
    @FXML
    private Text alertBody;
    @FXML
    private AnchorPane root;

    public AlertController(Parent parent, String title, String body) {
        super(parent);
        this.alertTitle.setText(title);
        this.alertBody.setText(body);

        this.root.setOnKeyPressed(event -> {
            System.out.println("ALERT PRESSED");
            if (event.getCode().equals(KeyCode.ENTER)) {
                this.getInstance().hide();
            }
        });
    }

    @FXML
    private void initialize() {
        //int lines = alertBody.getText().length() / 29;
        //this.root.setPrefHeight(24+45+20+(15*lines)+20+12+36+24);
    }

    @FXML
    private void clickOk(ActionEvent event) {
        this.getInstance().hide();
    }

    @Override
    public String getURL() {
        return "views/Alert.fxml";
    }
}
