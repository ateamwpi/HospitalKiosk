package controllers.WelcomeView;

import controllers.AbstractController;
import controllers.MapView.MapView.MapViewController;
import core.KioskMain;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.lang.management.PlatformLoggingMXBean;

/**
 * Created by Jacob on 4/18/2017.
 *
 */
public class WelcomeViewController extends AbstractController {

    @FXML
    private AnchorPane root;
    @FXML
    private ImageView image;

    @Override
    public String getURL() {
        return "resources/views/WelcomeView/WelcomeScreen.fxml";
    }

    @FXML
    private void initialize() {
        // bind event handlers
        root.setOnMousePressed(this::clickGo);

        image.fitWidthProperty().bind(root.widthProperty());
        image.fitHeightProperty().bind(root.heightProperty());

        Platform.runLater(() -> {
            root.maxWidthProperty().bind(KioskMain.getUI().getScene().widthProperty());
            root.minWidthProperty().bind(KioskMain.getUI().getScene().widthProperty());
            root.maxHeightProperty().bind(KioskMain.getUI().getScene().heightProperty());
            root.maxHeightProperty().bind(KioskMain.getUI().getScene().heightProperty());
        });
    }

    @FXML
    private void clickGo(MouseEvent event){
        KioskMain.getUI().setScene(new MapViewController());
    }
}
