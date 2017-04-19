package controllers;

import core.KioskMain;
import javafx.fxml.FXML;

/**
 * Created by Jacob on 4/18/2017.
 */
public class WelcomeScreenController extends AbstractController {
    @Override
    public String getURL() {
        return "views/WelcomeScreen.fxml";
    }

    @FXML
    private void clickGo(){
        KioskMain.getUI().setScene(new MainMenuController());
    }
}
