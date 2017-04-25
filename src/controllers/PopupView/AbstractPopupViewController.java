package controllers.PopupView;

import com.jfoenix.controls.JFXPopup;
import controllers.AbstractController;
import core.KioskMain;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

/**
 * Created by mattm on 4/17/2017.
 */
public abstract class AbstractPopupViewController extends AbstractController {

    @FXML
    protected AnchorPane root;

    private JFXPopup instance;
    protected Parent parent;

    public AbstractPopupViewController(Parent parent) {
        this.parent = parent;
        instance = new JFXPopup(getRegion());
    }

    public JFXPopup getInstance() {
        return instance;
    }

    public Region getRegion() {
        return root;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public void show(double x, double y) {
        KioskMain.getUI().setPopup(this);
        instance.show(parent, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, x, y);
    }

    public void showCentered() {
        show(KioskMain.getUI().getScene().getWidth()/2-130, KioskMain.getUI().getScene().getHeight()/2-105);
    }

    public void hide() {
        KioskMain.getUI().setPopup(null);
        instance.hide();
    }

    public Parent getParent() {
        return parent;
    }
}
