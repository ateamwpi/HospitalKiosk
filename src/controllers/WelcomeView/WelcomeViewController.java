package controllers.WelcomeView;

import controllers.AbstractController;
import controllers.MapView.MapView.MapViewController;
import core.KioskMain;
import javafx.fxml.FXML;

/**
 * Created by Jacob on 4/18/2017.
 */
public class WelcomeViewController extends AbstractController {
    @Override
    public String getURL() {
        return "resources/views/WelcomeView/WelcomeScreen.fxml";
    }

    @FXML
    private void clickGo(){
        KioskMain.getUI().setScene(new MapViewController());
    }
}
