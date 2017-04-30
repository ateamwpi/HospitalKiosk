package controllers.PopupView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import controllers.AbstractController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.util.function.Consumer;

/**
 * Created by mattm on 3/29/2017.
 */
public class AlertViewController extends AbstractController implements IPopup {

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

    private Consumer<Event> onClose;

    public AlertViewController(Parent parent, String title, String body) {
        this(parent, title, body, null);
    }

    public AlertViewController(Parent parent, String title, String body, Consumer<Event> onClose) {
        this.parent = parent;
        this.instance = new JFXPopup(this.getRegion());
        this.alertTitle.setText(title);
        this.alertBody.setText(body);
        this.onClose = onClose;

        this.root.setOnKeyPressed(event -> {
            System.out.println("ALERT PRESSED");
            if (event.getCode().equals(KeyCode.ENTER)) {
                if(this.onClose != null) onClose.accept(event);
                this.getInstance().hide();
                event.consume();
            }
        });

        showCentered();
    }

    @FXML
    private void initialize() {
        // bind event handlers
        okButton.setOnAction(this::clickOk);

//        int lines = alertBody.getText().length() / 29;
//        this.root.setPrefHeight(24+45+20+(15*lines)+20+12+36+24);
    }

    @FXML
    private void clickOk(ActionEvent event) {
        if(this.onClose != null) onClose.accept(event);
        this.getInstance().hide();
    }

    @Override
    public String getURL() {
        return "resources/views/PopupView/Alert.fxml";
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
