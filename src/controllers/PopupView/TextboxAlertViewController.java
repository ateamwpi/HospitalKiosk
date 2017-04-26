package controllers.PopupView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextField;
import controllers.AbstractController;
import core.KioskMain;
import core.Utils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by mattm on 3/29/2017.
 */
public class TextboxAlertViewController extends AbstractController implements IPopup {

    @FXML
    private JFXButton okButton;
    @FXML
    private Label alertTitle;
    @FXML
    private Text alertBody;
    @FXML
    private JFXTextField textfield;
    @FXML
    private AnchorPane root;
    private Parent parent;
    private JFXPopup instance;

    private final Consumer<String> onSelect;
    private final Function<String, Boolean> validate;

    public TextboxAlertViewController(Parent parent, String title, String body, String def, Consumer<String> onSelect, Function<String, Boolean> validate) {
        this.parent = parent;
        this.instance = new JFXPopup(this.getRegion());
        this.alertTitle.setText(title);
        this.alertBody.setText(body);
        this.onSelect = onSelect;
        this.validate = validate;
        this.textfield.setText(def);

        this.textfield.setOnAction(this::clickOk);
    }

    @FXML
    private void initialize() {
        // bind event handlers
        okButton.setOnAction(this::clickOk);
    }

    @FXML
    private void clickOk(ActionEvent event) {
        if(validate.apply(this.textfield.getText())) {
            this.onSelect.accept(this.textfield.getText());
            this.getInstance().hide();
        }
        else {
            this.okButton.requestFocus();
            Utils.showAlert(this.getParent(), "Invalid Timeout!", "Please enter a valid number!", this::regainFocus);
        }
    }

    private void regainFocus(Event e) {
        this.textfield.requestFocus();
        KioskMain.getUI().setPopup(this);
    }

    @Override
    public String getURL() {
        return "resources/views/PopupView/TextboxAlert.fxml";
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
