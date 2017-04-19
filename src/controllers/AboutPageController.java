package controllers;

import core.KioskMain;
import javafx.fxml.FXML;

/**
 * Created by Madeline on 4/18/2017.
 */
public class AboutPageController extends AbstractController {

    @Override
    public String getURL() {
        return "views/AboutPage.fxml";
    }

    @FXML
    private void clickBack() {
        KioskMain.getUI().setScene(new MainMenuController());
    }



}
