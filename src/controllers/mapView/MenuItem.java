package controllers.mapView;

import controllers.AboutPageController;
import controllers.AbstractController;
import controllers.LoginController;
import core.KioskMain;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.util.function.*;

/**
 * Created by mattm on 4/24/2017
 */
public class MenuItem extends AbstractController {

    public enum EnumMenuItem {
        About("About This Application", "info", EnumMenuItem::aboutPressed),
        Login("Administrator Login", "login", EnumMenuItem::loginPressed);

        String path;
        String text;
        BiConsumer<MouseEvent, Parent> onClick;

        EnumMenuItem(String text, String path, BiConsumer<MouseEvent, Parent> onClick) {
            this.text = text;
            this.path = path;
            this.onClick = onClick;
        }

        private static void aboutPressed(MouseEvent e, Parent mainRoot) {
            KioskMain.getUI().setScene(new AboutPageController());
        }

        private static void loginPressed(MouseEvent e, Parent mainRoot) {
            LoginController login = new LoginController(mainRoot);
            login.showCentered();
        }
    }

    private EnumMenuItem item;
    private Parent mainRoot;

    @FXML
    private HBox root;
    @FXML
    private Label menuIcon;
    @FXML
    private Label menuLabel;

    public MenuItem(EnumMenuItem m, Parent mainRoot) {
        super(m, mainRoot);
    }

    @Override
    public void initData(Object... data) {
        this.item = (EnumMenuItem) data[0];
        this.mainRoot = (Parent) data[1];
    }

    @FXML
    private void initialize() {
        menuLabel.setText(item.text);
        // add direction icon
        menuIcon.getStyleClass().add(this.item.path);
    }

    @FXML
    private void onPressed(MouseEvent e) {
        this.item.onClick.accept(e, mainRoot);
    }

    public String getURL() {
        return "views/MenuItem.fxml";
    }

}
