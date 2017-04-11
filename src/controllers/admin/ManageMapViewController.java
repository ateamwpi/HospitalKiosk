package controllers.admin;

import controllers.AbstractController;
import controllers.IController;
import controllers.map.DraggableNode;
import controllers.map.MapController;
import core.KioskMain;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;
import models.path.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Pattern;

public class ManageMapViewController extends AbstractController {

    private AdminMapController adminMapController;
    private DraggableNode selectedNode;
    private StringProperty xTextProperty;
    private StringProperty yTextProperty;
    private StringProperty roomNameProperty;
    private StringConverter<Number> converter;

    private ArrayList<String> floorList;

    @FXML
    private ChoiceBox<String> floors;
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
    private Button saveNode;
    @FXML
    private TableView<Node> tableNeighbors;
    @FXML
    private TableColumn<Node, Integer> idColumn;
    @FXML
    private Button deleteNeighbor;
    @FXML
    private Button addNeighbor;
    @FXML
    private TextField newNeighbor;
    @FXML
    private Label id;

    @Override
    public String getURL() {
        return "views/ManageMapView.fxml";
    }

    @Override
    public void initData(Object... data) {
        xTextProperty = new SimpleStringProperty();
        yTextProperty = new SimpleStringProperty();
        roomNameProperty = new SimpleStringProperty();
        converter = new NumberStringConverter();
        floorList = new ArrayList<String>(Arrays.asList("1st Floor", "2nd Floor","3rd Floor", "4th Floor", "5th Floor", "6th Floor", "7th Floor"));
    }

    @FXML
    private void initialize() {
        floors.getItems().addAll(floorList);
        floors.getSelectionModel().selectFirst();
        floors.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (floors.getSelectionModel().getSelectedItem() != null) {
                String fl = floors.getSelectionModel().getSelectedItem();
                System.out.println("The current selected floor is " + fl);
                setFloor(fl);
            }
        });
        // load the admin map controller
        adminMapController = new AdminMapController(this);
        // add the map to the container
        mapContainer.getChildren().add(adminMapController.getRoot());
        // init input text properties
        xTextProperty = x.textProperty();
        yTextProperty = y.textProperty();
        roomNameProperty = room.textProperty();
        // format numeric text fields
        TextFormatter<Integer> numericX = new TextFormatter<>(
                new IntegerStringConverter(),
                0,
                c -> Pattern.matches("\\d*", c.getText()) ? c : null );
        TextFormatter<Integer> numericY = new TextFormatter<>(
                new IntegerStringConverter(),
                0,
                c -> Pattern.matches("\\d*", c.getText()) ? c : null );
        TextFormatter<Integer> numericNeighbor = new TextFormatter<>(
                new IntegerStringConverter(),
                0,
                c -> Pattern.matches("\\d*", c.getText()) ? c : null );
        x.setTextFormatter(numericX);
        y.setTextFormatter(numericY);
        newNeighbor.setTextFormatter(numericNeighbor);
        // reset edit view
        unselectNode();
        // set the connections table factories
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        // add listener to table item selection
        tableNeighbors.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (tableNeighbors.getSelectionModel().getSelectedItem() != null) {
                deleteNeighbor.setDisable(false);
            }
        });
    }

    void selectNode(DraggableNode draggableNode) {
        // set selected node
        selectedNode = draggableNode;
        Node node = draggableNode.getNode();
        // bind text fields to node properties
        Bindings.bindBidirectional(xTextProperty, selectedNode.previewXProperty(), converter);
        Bindings.bindBidirectional(yTextProperty, selectedNode.previewYProperty(), converter);
        Bindings.bindBidirectional(roomNameProperty, selectedNode.previewRoomNameProperty());
        // update node id
        id.setText(Integer.toString(node.getID()));
        // show save button
        saveNode.setVisible(true);
        // enable add connection button
        addNeighbor.setDisable(false);
        // show delete button
        nodeAction.setText("Delete");
        // update connections table
        setTableNeighbors(selectedNode.getNode().getConnections());
    }

    void unselectNode() {
        // unbind text fields with node properties
        if (selectedNode != null) {
            Bindings.unbindBidirectional(xTextProperty, selectedNode.previewXProperty());
            Bindings.unbindBidirectional(yTextProperty, selectedNode.previewYProperty());
            Bindings.unbindBidirectional(roomNameProperty, selectedNode.previewRoomNameProperty());
        }
        // unset selected node
        selectedNode = null;
        // update edit view
        id.setText(null);
        x.setText("");
        y.setText("");
        room.setText("");
        // remove save button
        saveNode.setVisible(false);
        // disable add and delete connection buttons
        addNeighbor.setDisable(true);
        deleteNeighbor.setDisable(true);
        // show add button
        nodeAction.setText("Add");
        // clear connections table
        tableNeighbors.getItems().clear();
    }

    private int getX() {
        return Integer.parseInt(x.getText());
    }

    private int getY() {
        return Integer.parseInt(y.getText());
    }

    private String getRoomName() {
        String roomName = room.getText();
        return roomName.equals("") ? "NONE" : roomName;
    }

    @FXML
    private void clickBack(ActionEvent event) {
        if (adminMapController.attemptUnselectNode()) {
            KioskMain.setScene(new AdminMenuController());
        }
    }

    @FXML
    private void clickAddNeighbor(ActionEvent event) {
        // get the node id
        int neighborID = Integer.parseInt(newNeighbor.getText());
        // get the node
        Node node = KioskMain.getPath().getNode(neighborID);
        // check if node exists and is not itself
        if (node == null || node.equals(selectedNode.getNode())) {
            alertAddConnectionError();
            return;
        }
        // add the preview connection
        selectedNode.previewConnection(node);
        //refreshScene();
        // update the table of connections with preview connections
        setTableNeighbors(selectedNode.getPreviewConnections());
    }

    private void alertAddConnectionError() {
        System.out.print("Error: Trying to add node connection to itself.");
        Alert nodeUsed = new Alert(Alert.AlertType.ERROR);
        nodeUsed.setHeaderText("Node Connection Failure");
        nodeUsed.setContentText("This node cannot be connected to itself.");
        nodeUsed.setTitle("Node Connection Error");
        nodeUsed.showAndWait();
    }

    private void setTableNeighbors(Collection<Node> nodes) {
        tableNeighbors.getItems().setAll(nodes);
    }

    @FXML
    private void clickDeleteNeighbor(ActionEvent event) {
        // get the node
        Node nodeToDelete = tableNeighbors.getSelectionModel().getSelectedItem();
        // remove the preview connection
        selectedNode.removePreviewConnection(nodeToDelete);
        // update the table
        setTableNeighbors(selectedNode.getPreviewConnections());
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
            adminMapController.addNode(getX(), getY(), getRoomName());
        }
    }

    private Boolean verifyNode() {
        // TODO fix hard coded values
        // check x and y exist
        if (x.getText().equals("") || y.getText().equals("")) {
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

    public void setFloor(String fl) {
        int floor = floorList.indexOf(fl) + 1;
        System.out.println("We found the floor to be: " + floor);
        adminMapController.setFloor(floor);

    }

    private void refreshScene() {
        KioskMain.setScene(new ManageMapViewController());
    }

}
