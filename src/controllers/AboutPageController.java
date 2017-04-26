package controllers;

import controllers.mapView.MapViewController;
import core.KioskMain;
import javafx.fxml.FXML;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Madeline on 4/18/2017.
 */
public class AboutPageController extends AbstractController {
    @Override
    public String getURL() { return "views/AboutPage.fxml"; }

    @FXML
    private void clickBack() {
        KioskMain.getUI().setScene(new MapViewController());
    }



}
