package controllers.WelcomeView;

import controllers.AbstractController;
import controllers.MapView.MapView.MapViewController;
import core.KioskMain;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.awt.event.ActionEvent;

/**
 * Created by Jacob on 4/18/2017.
 */
public class WelcomeViewController extends AbstractController {

    @FXML
    private AnchorPane root;

    @Override
    public String getURL() {
        return "resources/views/WelcomeView/WelcomeScreen.fxml";
    }

    @FXML
    private void initialize() {
        // bind event handlers
        root.setOnMousePressed(this::clickGo);
    }

    @FXML
    private void clickGo(MouseEvent event){
        KioskMain.getUI().setScene(new MapViewController());
    }
}
