package controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import javax.swing.*;

/**
 * Created by mattm on 3/29/2017.
 */
public class OptionAlertController extends AbstractPopupController {

    @FXML
    private JFXButton button1;
    @FXML
    private JFXButton button2;
    @FXML
    private Label alertTitle;
    @FXML
    private Text alertBody;
    @FXML
    private AnchorPane root;

    public OptionAlertController(Parent parent, String title, String body, String btn1text, EventHandler<ActionEvent> btn1, String btn2text, EventHandler<ActionEvent> btn2) {
        super(parent);
        this.alertTitle.setText(title);
        this.alertBody.setText(body);
        this.button1.setText(btn1text);
        this.button2.setText(btn2text);
        this.button1.setOnAction(btn1);
        this.button2.setOnAction(btn2);
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
        return "views/OptionAlert.fxml";
    }
}
