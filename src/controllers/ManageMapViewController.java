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
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class ManageMapViewController {

    private static final String MAP_URL = "views/Map.fxml";

    private MapController mapController;

    @FXML
    private TextField x;
    @FXML
    private TextField y;
    @FXML
    private TextField room;
    @FXML
    private FlowPane flowPane;


    @FXML
    private void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(KioskMain.class.getClassLoader().getResource(MAP_URL));
            flowPane.getChildren().add(loader.load());
            mapController = loader.<MapController>getController();
            mapController.adminMode();
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }

        // format numeric text fields
        TextFormatter<Integer> numericX = new TextFormatter<>(
                new IntegerStringConverter(),
                0,
                c -> Pattern.matches("\\d*", c.getText()) ? c : null );
        TextFormatter<Integer> numericY = new TextFormatter<>(
                new IntegerStringConverter(),
                0,
                c -> Pattern.matches("\\d*", c.getText()) ? c : null );
        x.setTextFormatter(numericX);
        y.setTextFormatter(numericY);
    }

    @FXML
    private void clickBack(ActionEvent event) {
        KioskMain.setScene("views/AdminMenu.fxml");
    }


    @FXML
    private void clickNodeAction(ActionEvent event) {
        if (mapController.nodeIsSelected()) {
            // delete the node
            mapController.deleteSelectedNode();
        } else {
            // add the node
            mapController.addNode(Integer.parseInt(x.getText()), Integer.parseInt(y.getText()), room.getText());
        }
    }

    @FXML
    private void xAction(ActionEvent event) {
        System.out.println(event);
    }

    @FXML
    private void yAction(ActionEvent event) {
        System.out.println(event);
    }

    @FXML
    private void roomAction(ActionEvent event) {
        System.out.println(event);
    }

}
