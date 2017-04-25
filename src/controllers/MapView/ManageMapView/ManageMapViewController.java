package controllers.MapView.ManageMapView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import controllers.AbstractController;
import controllers.Map.ManageMapController;
import controllers.Map.DraggableNode;
import controllers.MapView.MapView.MapViewController;
import core.KioskMain;
import core.Utils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
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

    private ArrayList<String> floorList;

//    @FXML
//    private JFXComboBox<String> floors;
    @FXML
    private JFXTextField x;
    @FXML
    private JFXTextField y;
    @FXML
    private JFXTextField room;
    @FXML
    private AnchorPane mapContainer;
    @FXML
    private JFXButton nodeAction;
    @FXML
    private JFXButton saveNode;
    @FXML
    private TableView<Node> tableNeighbors;
    @FXML
    private TableColumn<Node, Integer> idColumn;
    @FXML
    private JFXButton deleteNeighbor;
    @FXML
    private JFXButton addNeighbor;
    @FXML
    private JFXTextField newNeighbor;
    @FXML
    private JFXCheckBox restrictedBox;
    @FXML
    private Label id;

    @Override
    public String getURL() {
        return "resources/views/ManageMapView.fxml";
    }

    @Override
    public void initData(Object... data) {
        xTextProperty = new SimpleStringProperty();
        yTextProperty = new SimpleStringProperty();
        restrictedProperty = new SimpleBooleanProperty();
        roomNameProperty = new SimpleStringProperty();
        converter = new NumberStringConverter();
        floorList = new ArrayList<String>(Arrays.asList("1st Floor", "2nd Floor","3rd Floor", "4th Floor", "5th Floor", "6th Floor", "7th Floor"));
    }

    @FXML
    private void initialize() {

        // load the admin map controller
        manageMapController = new ManageMapController(this);
        // add the map to the container
        mapContainer.getChildren().add(manageMapController.getRoot());

        for(JFXButton b: manageMapController.getMapController().getFloorButtons()) {
            b.setOnAction(event -> setFloor(Utils.strForNum(Integer.parseInt(b.getText())) + " Floor"));
        }

//        floors.getItems().addAll(floorList);
//        floors.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
//            if (floors.getSelectionModel().getSelectedItem() != null) {
//                if (manageMapController.attemptUnselectNode()) {
//                    String fl = floors.getSelectionModel().getSelectedItem();
//                    System.out.println("The current selected floor is " + fl);
//                    setFloor(fl);
//                } else {
//                }
//
//            }
//        });
//        floors.getSelectionModel().selectFirst();
        // init input text properties
        xTextProperty = x.textProperty();
        yTextProperty = y.textProperty();
        restrictedProperty = restrictedBox.selectedProperty();
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

    public void selectNode(DraggableNode draggableNode) {
        // set selected node
        selectedNode = draggableNode;
        Node node = draggableNode.getNode();
        // bind text fields to node properties
        Bindings.bindBidirectional(xTextProperty, selectedNode.previewXProperty(), converter);
        Bindings.bindBidirectional(yTextProperty, selectedNode.previewYProperty(), converter);
        Bindings.bindBidirectional(roomNameProperty, selectedNode.previewRoomNameProperty());
        Bindings.bindBidirectional(restrictedProperty, selectedNode.previewRestrictedProperty());
        // update node id
        id.setText("ID: " + Integer.toString(node.getID()));
        restrictedBox.setVisible(true);
        restrictedBox.setDisable(false);
        // show save button
        saveNode.setVisible(true);
        // enable add connection button
        addNeighbor.setDisable(false);
        // show delete button
        nodeAction.setText("Delete");
        // update connections table
        setTableNeighbors(selectedNode.getNode().getConnections());
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
        id.setText("");
        x.setText("");
        y.setText("");
        room.setText("");
        restrictedBox.setVisible(false);
        restrictedBox.setDisable(true);
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
        if (manageMapController.attemptUnselectNode()) {
            KioskMain.getUI().setScene(new MapViewController());
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

        newNeighbor.clear();
    }

    private void alertAddConnectionError() {
        Utils.showAlert(getRoot(), "Invalid Node Connection!", "This node cannot be connected to itself!");
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
            manageMapController.addNode(getX(), getY(), getRoomName());
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
        setFloor(floor);
    }

    public void setFloor(int floor) {
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
