package controllers;

import core.KioskMain;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;

public class ManageMapViewController {

    @FXML
    private Button backBtn;
    @FXML
    private Pane mapContainer;


    @FXML
    private void initialize() {
        Node map = MapController.getMap();
        mapContainer.getChildren().add(map);
    }

    @FXML
    private void clickBack(ActionEvent event) throws IOException {
        KioskMain.setScene("views/AdminMenu.fxml");
    }

}
