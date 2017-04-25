package controllers;

import controllers.mapView.MapViewController;
import core.KioskMain;
import core.Utils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
        KioskMain.getUI().setScene(new MapViewController());
    }
}
