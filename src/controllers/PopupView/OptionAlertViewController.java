package controllers.PopupView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import controllers.AbstractController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 * Created by mattm on 3/29/2017.
 */
public class OptionAlertViewController extends AbstractController implements IPopup {

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
    private Parent parent;
    private JFXPopup instance;

    public OptionAlertViewController(Parent parent, String title, String body, String btn1text, EventHandler<ActionEvent> btn1, String btn2text, EventHandler<ActionEvent> btn2) {
        this.parent = parent;
        this.instance = new JFXPopup(this.getRegion());
        this.alertTitle.setText(title);
        this.alertBody.setText(body);
        this.button1.setText(btn1text.toUpperCase());
        this.button2.setText(btn2text.toUpperCase());
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
        return "resources/views/PopupView/OptionAlert.fxml";
    }

    @Override
    public JFXPopup getInstance() {
        return this.instance;
    }

    @Override
    public Region getRegion() {
        return this.root;
    }

    @Override
    public Parent getParent() {
        return this.parent;
    }
}
