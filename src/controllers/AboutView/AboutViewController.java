package controllers.AboutView;

import com.sun.xml.internal.bind.XmlAccessorFactory;
import controllers.AbstractController;
import controllers.MapView.MapView.MapViewController;
import core.KioskMain;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Madeline on 4/18/2017.
 */
public class AboutViewController extends AbstractController {

    @FXML
    private Button backButton;

    @Override
    public String getURL() {
        return "resources/views/AboutView/AboutPage.fxml";
    }

    @FXML
    private void initialize() {
        backButton.setOnAction(this::clickBack);
    }

    @FXML
    private void clickBack(ActionEvent event) {
        KioskMain.getUI().setScene(new MapViewController());
    }

}
