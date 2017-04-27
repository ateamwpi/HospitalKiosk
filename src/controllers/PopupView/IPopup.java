package controllers.PopupView;

import com.jfoenix.controls.JFXPopup;
import core.KioskMain;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

/**
 * Created by mattm on 4/17/2017.
 */
//@FunctionalInterface
public interface IPopup {

//    @FXML
//    protected AnchorPane root;
//
//    JFXPopup instance;
//    Parent parent;
//    default IPopupController(Parent parent) {
//        this.parent = parent;
//        this.instance = new JFXPopup(this.getRegion());
//    }
//    default void initData(Object... data) {
//        parent = (Parent) data[0];
//        this.instance = new JFXPopup(getRegion());
//    }

    JFXPopup getInstance();

    Region getRegion();

    Parent getParent();

    default void show(double x, double y) {
        KioskMain.getUI().setPopup(this);
        this.getInstance().show(this.getParent(), JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, x, y);
    }

    default void showCentered() {
        // please nobody read this ever this is so gross I hate myself for writing it -matt
        this.show(0, 0);
        double xOffset = getRegion().getBoundsInParent().getWidth()/2;
        double yOffset = getRegion().getBoundsInParent().getHeight()/2;
        this.hide();
        this.show(KioskMain.getUI().getScene().getWidth()/2-xOffset, KioskMain.getUI().getScene().getHeight()/2-yOffset);
    }

    default void hide() {
        KioskMain.getUI().setPopup(null);
        this.getInstance().hide();
    }

    default boolean hasFocus() {
        return KioskMain.getUI().getPopup().getClass().equals(this.getClass());
    }
}
