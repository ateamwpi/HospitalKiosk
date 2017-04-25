package controllers;

import com.jfoenix.controls.JFXPopup;
import core.KioskMain;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

/**
 * Created by mattm on 4/17/2017.
 */
public abstract class AbstractPopupController extends AbstractController {

    @FXML
    protected AnchorPane root;

    private JFXPopup instance;
    protected Parent parent;

    public AbstractPopupController(Parent parent) {
        this.parent = parent;
        this.instance = new JFXPopup(this.getRegion());
    }

    public JFXPopup getInstance() {
        return this.instance;
    }

    public Region getRegion() {
        return this.root;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public void show(double x, double y) {
        KioskMain.getUI().setPopup(this);
        this.instance.show(parent, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, x, y);
    }

    public void showCentered() {
        this.show(KioskMain.getUI().getScene().getWidth()/2-130, KioskMain.getUI().getScene().getHeight()/2-105);
    }

    public void hide() {
        KioskMain.getUI().setPopup(null);
        this.instance.hide();
    }

    public boolean hasFocus() {
        return KioskMain.getUI().getPopup().getClass().equals(this.getClass());
    }

    public Parent getParent() {
        return parent;
    }
}
