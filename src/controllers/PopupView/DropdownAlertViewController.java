package controllers.PopupView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPopup;
import controllers.AbstractController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Created by mattm on 3/29/2017.
 */
public class DropdownAlertViewController extends AbstractController implements IPopup {

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
    private Parent parent;
    private JFXPopup instance;

    private final Consumer<String> setSelected;

    public DropdownAlertViewController(Parent parent, String title, String body, Collection<String> options, String def, Consumer<String> setSelected) {
        this.parent = parent;
        this.instance = new JFXPopup(this.getRegion());
        this.alertTitle.setText(title);
        this.alertBody.setText(body);
        this.alertBox.getItems().setAll(options);
        this.alertBox.getSelectionModel().select(def);
        this.setSelected = setSelected;

        this.root.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                this.setSelected.accept((String)this.alertBox.getSelectionModel().getSelectedItem());
                this.getInstance().hide();
            }
        });

        showCentered();
    }

    @FXML
    private void initialize() {
        // bind event handlers
        okButton.setOnAction(this::clickOk);
    }

    @FXML
    private void clickOk(ActionEvent event) {
        this.setSelected.accept((String)this.alertBox.getSelectionModel().getSelectedItem());
        this.getInstance().hide();
    }

    @Override
    public String getURL() {
        return "resources/views/PopupView/DropdownAlert.fxml";
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
