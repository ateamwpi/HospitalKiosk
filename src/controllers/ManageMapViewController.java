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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.converter.IntegerStringConverter;
import models.path.Node;

import java.io.IOException;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class ManageMapViewController {

    private static final String MAP_URL = "views/Map.fxml";

    private MapController mapController;
    private Node selectedNode;

    @FXML
    private TextField x;
    @FXML
    private TextField y;
    @FXML
    private TextField room;
    @FXML
    private AnchorPane mapContainer;
    @FXML
    private Button nodeAction;
    @FXML
    private Button saveAction;


    @FXML
    private void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(KioskMain.class.getClassLoader().getResource(MAP_URL));
            mapContainer.getChildren().add(loader.load());
            mapController = loader.<MapController>getController();
            mapController.adminMode();
            mapController.initData(this);
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

    public void selectNode(Node node) {
        // set selected node
        selectedNode = node;
        // update edit view
        x.setText(new Integer(node.getX()).toString());
        y.setText(new Integer(node.getY()).toString());
        room.setText(node.getRoomName());
        // show delete node button
        //saveAction.
        nodeAction.setText("Delete node");
    }

    public void unselectNode() {
        // unset selected node
        selectedNode = null;
        // update edit view
        x.setText("0");
        y.setText("0");
        room.setText(null);
        // show add node button
        nodeAction.setText("Add node");
    }

    @FXML
    private void clickBack(ActionEvent event) {
        KioskMain.setScene("views/AdminMenu.fxml");
    }

    @FXML
    private void clickSave(ActionEvent event) {
        System.out.println("save node");
        selectedNode.setX(Integer.parseInt(x.getText()));
        selectedNode.setY(Integer.parseInt(y.getText()));
        selectedNode.setRoomName(room.getText());
        //selectedNode.setConnections(connections);
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
