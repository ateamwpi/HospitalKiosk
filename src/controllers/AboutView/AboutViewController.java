package controllers.AboutView;

import com.jfoenix.controls.JFXPopup;
import controllers.AbstractController;
import controllers.MapView.MapView.MapViewController;
import controllers.PopupView.IPopup;
import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

/**
 * Created by Madeline on 4/18/2017.
 */
public class AboutViewController extends AbstractController implements IPopup {

    @FXML
    private Button backButton;

    private Parent parent;
    private JFXPopup instance;

    public AboutViewController(Parent parent) {
        this.parent = parent;
        this.instance = new JFXPopup(this.getRegion());
    }

    @Override
    public String getURL() {
        return "resources/views/AboutView/AboutPage.fxml";
    }

    @FXML
    private void initialize() {
        backButton.setOnAction(this::clickBack);
    }

    @FXML
    private void clickBack(ActionEvent event) {
        this.getInstance().hide();
    }

    @Override
    public JFXPopup getInstance() {
        return instance;
    }

    @Override
    public Region getRegion() {
        return (Region) this.getRoot();
    }

    @Override
    public Parent getParent() {
        return parent;
    }
}
