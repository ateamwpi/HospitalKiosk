package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 * Created by mattm on 3/29/2017.
 */
public class AlertController extends AbstractController implements IPopup {

    @FXML
    private JFXButton okButton;
    @FXML
    private Label alertTitle;
    @FXML
    private Text alertBody;
    @FXML
    private AnchorPane root;
    private Parent parent;
    private JFXPopup instance;

    public AlertController(Parent parent, String title, String body) {
        this.parent = parent;
        this.instance = new JFXPopup(this.getRegion());
        this.alertTitle.setText(title);
        this.alertBody.setText(body);
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
