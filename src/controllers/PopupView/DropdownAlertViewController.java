package controllers.PopupView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Created by mattm on 3/29/2017.
 */
public class DropdownAlertViewController extends AbstractPopupViewController {

    @FXML
    private JFXButton okButton;
    @FXML
    private Label alertTitle;
    @FXML
    private Text alertBody;
    @FXML
    private JFXComboBox<String> alertBox;
    @FXML
    private AnchorPane root;

    private final Consumer<String> onSelect;

    public DropdownAlertViewController(Parent parent, String title, String body, Collection<String> options, String def, Consumer<String> onSelect) {
        super(parent);
        alertTitle.setText(title);
        alertBody.setText(body);
        alertBox.getItems().setAll(options);
        alertBox.getSelectionModel().select(def);
        this.onSelect = onSelect;
    }

    @FXML
    private void initialize() {
        // bind event handlers
        okButton.setOnAction(this::clickOk);
    }

    @FXML
    private void clickOk(ActionEvent event) {
        onSelect.accept(alertBox.getSelectionModel().getSelectedItem());
        getInstance().hide();
    }

    @Override
    public String getURL() {
        return "resources/views/PopupView/DropdownAlert.fxml";
    }
}
