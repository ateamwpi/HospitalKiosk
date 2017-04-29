package controllers.MapView.ManageMapView;

import com.jfoenix.controls.*;
import controllers.AbstractController;
import controllers.MapView.Map.ManageMapController;
import controllers.MapView.Map.DraggableNode;
import controllers.MapView.MapView.MapViewController;
import core.KioskMain;
import core.Utils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;
import models.path.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

public class ManageMapViewController extends AbstractController {

    private ManageMapController manageMapController;
    private DraggableNode selectedNode;
    private StringProperty xTextProperty;
    private StringProperty yTextProperty;
    private StringProperty roomNameProperty;
    private BooleanProperty restrictedProperty;
    private StringConverter<Number> converter;
    private ArrayList<String> nodeTypeList;

    private ArrayList<String> floorList;

    @FXML
    private StackPane mapContainer;
    @FXML
    private JFXDrawer snackbar;

    ManageMapSnackbarController manageMapSnackbarController;

    @Override
    public String getURL() {
        return "resources/views/MapView/ManageMapView/ManageMapView.fxml";
    }

    @Override
    public void initData(Object... data) {
        xTextProperty = new SimpleStringProperty();
        yTextProperty = new SimpleStringProperty();
        restrictedProperty = new SimpleBooleanProperty();
        roomNameProperty = new SimpleStringProperty();
        converter = new NumberStringConverter();
        floorList = new ArrayList<>(Arrays.asList("1st Floor", "2nd Floor", "3rd Floor", "4th Floor", "5th Floor", "6th Floor", "7th Floor"));
        nodeTypeList = new ArrayList<>(Arrays.asList("Room", "Elevator", "Stairwell", "Outside"));
    }

    @FXML
    private void initialize() {

        snackbar.open();

        // setup drawer
        manageMapSnackbarController = new ManageMapSnackbarController(getRoot(), manageMapController);
        snackbar.setSidePane(manageMapSnackbarController.getRoot());

        // bind event handlers
        manageMapSnackbarController.saveNode.setOnAction(this::clickSave);
        manageMapSnackbarController.nodeAction.setOnAction(this::clickNodeAction);


        // load the admin map controller
        manageMapController = new ManageMapController(this);
        // add the map to the container
        mapContainer.getChildren().add(manageMapController.getRoot());

        for(JFXButton b: manageMapController.getMapController().getFloorButtons()) {
            b.setOnAction(event -> setFloor(Utils.strForNum(Integer.parseInt(b.getText())) + " Floor"));
        }

        // init input text properties
        //TODO: set employeeOnly and nodeType of selected node
        xTextProperty = manageMapSnackbarController.x.textProperty();
        yTextProperty = manageMapSnackbarController.y.textProperty();
        restrictedProperty = manageMapSnackbarController.employeeOnly.selectedProperty();
        // format numeric text fields
        TextFormatter<Integer> numericX = new TextFormatter<>(
                new IntegerStringConverter(),
                0,
                c -> Pattern.matches("\\d*", c.getText()) ? c : null);
        TextFormatter<Integer> numericY = new TextFormatter<>(
                new IntegerStringConverter(),
                0,
                c -> Pattern.matches("\\d*", c.getText()) ? c : null);
        TextFormatter<Integer> numericNeighbor = new TextFormatter<>(
                new IntegerStringConverter(),
                0,
                c -> Pattern.matches("\\d*", c.getText()) ? c : null);
        manageMapSnackbarController.x.setTextFormatter(numericX);
        manageMapSnackbarController.y.setTextFormatter(numericY);
        // reset edit view
        unselectNode();

    }

    public void selectNode(DraggableNode draggableNode) {
        // set selected node
        selectedNode = draggableNode;
        Node node = draggableNode.getNode();
        // bind text fields to node properties
        Bindings.bindBidirectional(xTextProperty, selectedNode.previewXProperty(), converter);
        Bindings.bindBidirectional(yTextProperty, selectedNode.previewYProperty(), converter);
        Bindings.bindBidirectional(roomNameProperty, selectedNode.previewRoomNameProperty());
        Bindings.bindBidirectional(restrictedProperty, selectedNode.previewRestrictedProperty());
        manageMapSnackbarController.employeeOnly.setVisible(true);
        manageMapSnackbarController.employeeOnly.setDisable(false);
        manageMapSnackbarController.nodeAction.setDisable(false);
        // show save button
        // show delete button
        manageMapSnackbarController.nodeAction.setText("Delete");


    }

    public void unselectNode() {
        // unbind text fields with node properties
        if (selectedNode != null) {
            Bindings.unbindBidirectional(xTextProperty, selectedNode.previewXProperty());
            Bindings.unbindBidirectional(yTextProperty, selectedNode.previewYProperty());
            Bindings.unbindBidirectional(roomNameProperty, selectedNode.previewRoomNameProperty());
            Bindings.unbindBidirectional(restrictedProperty, selectedNode.previewRestrictedProperty());
        }
        // unset selected node
        selectedNode = null;
        // update edit view
        manageMapSnackbarController.x.setText("");
        manageMapSnackbarController.y.setText("");
        //manageMapSnackbarController.room.setText("");
        manageMapSnackbarController.employeeOnly.setVisible(false);
        manageMapSnackbarController.employeeOnly.setDisable(true);
        // remove save button
        manageMapSnackbarController.saveNode.setVisible(false);

    }

    private int getX() {
        return Integer.parseInt(manageMapSnackbarController.x.getText());
    }

    private int getY() {
        return Integer.parseInt(manageMapSnackbarController.y.getText());
    }

    private String getRoomName() {
        String roomName = manageMapSnackbarController.room.getText();
        return roomName.equals("") ? "NONE" : roomName;
    }

    @FXML
    private void clickBack(ActionEvent event) {
        manageMapController.attemptUnselectNode( (result) -> {
            if(result)
                KioskMain.getUI().setScene(new MapViewController());
        });
    }


    @FXML
    private void clickSave(ActionEvent event) {
        selectedNode.save();
        refreshScene();
    }

    @FXML
    private void clickNodeAction(ActionEvent event) {
        if (selectedNode == null) {
            clickAdd();
        } else {
            clickDelete();
        }
    }

    private void clickDelete() {
        // delete the node
        if (selectedNode.delete()) {
            refreshScene();
        }
    }

    private void clickAdd() {
        // verify the node properties
        if (verifyNode()) {
            // add the node
            manageMapController.addNode(getX(), getY(), getRoomName());
        }
    }

    private Boolean verifyNode() {
        // TODO fix hard coded values
        // check x and y exist
        if (manageMapSnackbarController.x.getText().equals("") || manageMapSnackbarController.y.getText().equals("")) {
            return false;
        }
        // check x and y within bounds
        int x = getX();
        int y = getY();
        if (0 > x || x > 560 || 0 > y || y > 500) {
            return false;
        }
        // check connections

        return true;
    }

    private void setFloor(String fl) {
        int floor = floorList.indexOf(fl) + 1;
        setFloor(floor);
    }

    private void setFloor(int floor) {
        System.out.println("We found the floor to be: " + floor);
        manageMapController.setFloor(floor);
    }

    private void refreshScene() {
        int floor = manageMapController.getMapController().getFloor();
        ManageMapViewController con = new ManageMapViewController();
        //con.floors.getSelectionModel().select(floor - 1);
        KioskMain.getUI().setScene(con);
    }

}
