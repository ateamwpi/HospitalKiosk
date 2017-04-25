package controllers.AboutView;

import controllers.AbstractController;
import controllers.MapView.MapViewController;
import core.KioskMain;
import javafx.fxml.FXML;

/**
 * Created by Madeline on 4/18/2017.
 */
public class AboutViewController extends AbstractController {

    @Override
    public String getURL() {
        return "resources/views/AboutPage.fxml";
    }

    @FXML
    private void clickBack() {
        KioskMain.getUI().setScene(new MapViewController());
    }



}
