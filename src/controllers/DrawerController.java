package controllers;

import com.jfoenix.controls.JFXHamburger;
import javafx.fxml.FXML;

/**
 * Created by dylan on 4/16/17.
 */
public class DrawerController extends AbstractController {

    @FXML
    private JFXHamburger hamburger;

    public JFXHamburger getHamburger() {
        return hamburger;
    }

    @Override
    public String getURL() {
        return "views/Drawer.fxml";
    }
}
