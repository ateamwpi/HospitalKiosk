package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * Created by mattm on 3/29/2017.
 */
public class DropdownAlertController extends AbstractPopupController {

    @FXML
    private JFXButton okButton;
    @FXML
    private Label alertTitle;
    @FXML
    private Text alertBody;
    @FXML
    private JFXComboBox alertBox;
    @FXML
    private AnchorPane root;

    private Consumer<String> onSelect;

    public DropdownAlertController(Parent parent, String title, String body, Collection<String> options, String def, Consumer<String> onSelect) {
        super(parent);
        this.alertTitle.setText(title);
        this.alertBody.setText(body);
        this.alertBox.getItems().setAll(options);
        this.alertBox.getSelectionModel().select(def);
        this.onSelect = onSelect;
    }

    @FXML
    private void initialize() {
    }

    @FXML
    private void clickOk(ActionEvent event) {
        this.onSelect.accept((String)this.alertBox.getSelectionModel().getSelectedItem());
        this.getInstance().hide();
    }

    @Override
    public String getURL() {
        return "views/DropdownAlert.fxml";
    }
}
