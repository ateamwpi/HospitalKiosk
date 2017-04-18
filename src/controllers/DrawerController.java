package controllers;

import com.jfoenix.controls.JFXHamburger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * Created by dylan on 4/16/17.
 */
public class DrawerController extends AbstractController {

    @FXML
    private Label drawerClose;

    public Label getDrawerClose() {
        return drawerClose;
    }

    @Override
    public String getURL() {
        return "views/Drawer.fxml";
    }
}
