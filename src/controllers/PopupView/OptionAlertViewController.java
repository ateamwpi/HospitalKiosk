package controllers.PopupView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import com.sun.org.apache.xpath.internal.operations.Bool;
import controllers.AbstractController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by mattm on 3/29/2017.
 */
public class OptionAlertViewController extends AbstractController implements IPopup {

    private final Consumer<Boolean> setConfirm;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXButton confirmButton;
    @FXML
    private Label alertTitle;
    @FXML
    private Text alertBody;
    @FXML
    private AnchorPane root;
    private Parent parent;
    private JFXPopup instance;

    // pass true to action on default, false on other
    public OptionAlertViewController(Parent parent, String title, String body, String cancelText, String confirmText, Consumer<Boolean> setConfirm) {
        this.parent = parent;
        this.setConfirm = setConfirm;
        this.instance = new JFXPopup(this.getRegion());
        this.alertTitle.setText(title);
        this.alertBody.setText(body);
        this.cancelButton.setText(cancelText.toUpperCase());
        this.confirmButton.setText(confirmText.toUpperCase());
        this.cancelButton.setOnAction(this::clickCancel);
        this.confirmButton.setOnAction(this::clickConfirm);
        showCentered();
    }

    private void clickCancel(ActionEvent event) {
        clickButton(false);
    }

    private void clickConfirm(ActionEvent event) {
        clickButton(true);
    }

    private void clickButton(Boolean isConfirmed) {
        hide();
        setConfirm.accept(isConfirmed);
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
