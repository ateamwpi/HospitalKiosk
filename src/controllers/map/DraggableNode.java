package controllers.map;

import controllers.admin.AdminMapController;
import core.Utils;
import core.exception.NameInUseException;
import core.exception.WrongFloorException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import models.path.Node;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by dylan on 4/6/17.
 */
public class DraggableNode extends Circle {

    private static final double UNSELECTED_RADIUS = 3;
    private static final double SELECTED_RADIUS = 5;
    //private static final Color UNSELECTED_COLOR = Color.BLACK;
    private static final Color SELECTED_COLOR = Color.RED;

    private Node node;
    private IntegerProperty previewXProperty = new SimpleIntegerProperty();
    private IntegerProperty previewYProperty = new SimpleIntegerProperty();
    private StringProperty previewRoomNameProperty = new SimpleStringProperty();
    private Collection<Node> previewConnections = new ArrayList<>();

    private AdminMapController adminMapController;

    public DraggableNode(Node node, NodeGestures nodeGestures, AdminMapController adminMapController) {
        super(node.getX(), node.getY(), UNSELECTED_RADIUS, node.getColor());
        // bind the circle x and y to the preview x and y
        centerXProperty().bind(previewXProperty());
        centerYProperty().bind(previewYProperty());
        this.node = node;
        this.adminMapController = adminMapController;
        setDefaultPreview();
        // handlers for mouse click and drag
        addEventHandler(MouseEvent.ANY, new ClickDragHandler(nodeGestures.getOnMouseClickedEventHandler(), nodeGestures.getOnMouseDraggedEventHandler()));
    }

    public void previewX(int x) {
        previewXProperty.set(x);
    }

    public void previewY(int y) {
        previewYProperty.set(y);
    }

    public void previewRoomName(String roomName) {
        previewRoomNameProperty.set(roomName);
    }

    public void previewConnections(Collection<Node> nodes) {
        // redraw connections

        // Add anything new
        for (Node n : nodes) {
            if(!previewConnections.contains(n)) {
                previewConnection(n);
            }
        }
        // Remove anything old
         ArrayList<Node> toRemove = new ArrayList<Node>();
        for (Node n : previewConnections) {
            if(!nodes.contains(n)) {
                toRemove.add(n);
            }
        }
        for (Node n : toRemove) {
            removePreviewConnection(n);
        }
    }

    public void previewConnection(Node node) {
        if(node.getFloor() == this.node.getFloor()) {
            previewConnections.add(node);
            adminMapController.drawDraggableConnection(this, adminMapController.getDraggableNode(node));
        }
        else {
            Utils.showAlert(adminMapController.getManageMapViewController().getRoot(), "Invalid Node Connection!", "You cannot add this connection as the nodes are on different floors!");
        }
    }

    public void removePreviewConnection(Node node) {
        previewConnections.remove(node);
        adminMapController.removeDraggableConnection(this, adminMapController.getDraggableNode(node));
    }

    public Collection<Node> getPreviewConnections() {
        return previewConnections;
    }

    public final int getPreviewX(){
        return previewXProperty.get();
    }

    public final int getPreviewY(){
        return previewYProperty.get();
    }

    public final String getPreviewRoomName() {
        return previewRoomNameProperty.get();
    }

    public IntegerProperty previewXProperty() {
        return previewXProperty;
    }

    public IntegerProperty previewYProperty() {
        return previewYProperty;
    }

    public StringProperty previewRoomNameProperty() {
        return previewRoomNameProperty;
    }

    public void cancelPreview() {
        System.out.println("cancel");
        previewConnections(node.getConnections());
        setDefaultPreview();
    }

    public void save() {
        // update the node
        try {
            node.setRoomName(getPreviewRoomName());
        }
        catch(NameInUseException e) {
            Utils.showAlert(adminMapController.getManageMapViewController().getRoot(), "Room Name in Use!", "The room name " + getPreviewRoomName() + " is already in use on a different node! Please choose a new name.");
            return;
        }

        node.setX(getPreviewX());
        node.setY(getPreviewY());
        try {
            node.setConnections(previewConnections);
        }
        catch(WrongFloorException e) {
            //TODO handle this
        }
        node.save();
    }

    public Boolean delete() {
        return adminMapController.attemptDeleteSelectedNode();
    }

    public Node getNode() {
        return node;
    }

    public void unselect() {
        System.out.println("node unselected");
        cancelPreview();
        setFill(node.getColor());
        setRadius(UNSELECTED_RADIUS);
    }

    public void select() {
        System.out.println("node selected");
        System.out.println(node);
        setFill(SELECTED_COLOR);
        setRadius(SELECTED_RADIUS);
    }

    private void setDefaultPreview() {
        previewX(node.getX());
        previewY(node.getY());
        previewRoomName(node.getRoomName());
        previewConnections = node.getConnections();
    }

    public Boolean hasUnsavedChanges() {
        return getPreviewX() != node.getX()
                || getPreviewY() != node.getY()
                || !getPreviewRoomName().equals(node.getRoomName())
                || !getPreviewConnections().equals(node.getConnections());
    }
}
