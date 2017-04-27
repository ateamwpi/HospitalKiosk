package controllers.MapView.Map;

import core.Utils;
import core.exception.NameInUseException;
import core.exception.WrongFloorException;
import javafx.beans.property.*;
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

    private final Node node;
    private final IntegerProperty previewXProperty = new SimpleIntegerProperty();
    private final IntegerProperty previewYProperty = new SimpleIntegerProperty();
    private final StringProperty previewRoomNameProperty = new SimpleStringProperty();
    private final BooleanProperty previewRestrictedProperty = new SimpleBooleanProperty();
    private Collection<Node> previewConnections = new ArrayList<>();

    private final ManageMapController manageMapController;

    public DraggableNode(Node node, NodeGestures nodeGestures, ManageMapController manageMapController) {
        super(node.getX(), node.getY(), UNSELECTED_RADIUS, node.getColor());
        // bind the circle x and y to the preview x and y
        centerXProperty().bind(previewXProperty());
        centerYProperty().bind(previewYProperty());
        this.node = node;
        this.manageMapController = manageMapController;
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

    private void previewRoomName(String roomName) {
        previewRoomNameProperty.set(roomName);
    }

    private void previewRestricted(boolean value) {
        previewRestrictedProperty.set(value);
    }

    public void resetPreviewConnections() {
        this.previewConnections = node.getConnections();
    }

    private void previewConnections(Collection<Node> nodes) {
        // redraw connections

        // Add anything new
        for (Node n : nodes) {
            if(!previewConnections.contains(n)) {
                previewConnection(n);
            }
        }
        // Remove anything old
         ArrayList<Node> toRemove = new ArrayList<>();
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
            manageMapController.drawDraggableConnection(this, manageMapController.getDraggableNode(node));
        }
        else {
            Utils.showAlert(manageMapController.getManageMapViewController().getRoot(), "Invalid Node Connection!", "You cannot add this connection as the nodes are on different floors!");
        }
    }

    public void removePreviewConnection(Node node) {
        previewConnections.remove(node);
        manageMapController.removeDraggableConnection(this, manageMapController.getDraggableNode(node));
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

    private String getPreviewRoomName() {
        return previewRoomNameProperty.get();
    }

    private boolean getPreviewRestricted() {
        return previewRestrictedProperty.get();
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

    public BooleanProperty previewRestrictedProperty() {
        return previewRestrictedProperty;
    }

    private void cancelPreview() {
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
            Utils.showAlert(manageMapController.getManageMapViewController().getRoot(), "Room Name in Use!", "The room name " + getPreviewRoomName() + " is already in use on a different node! Please choose a new name.");
            return;
        }

        node.setX(getPreviewX());
        node.setY(getPreviewY());
        node.setRestricted(getPreviewRestricted());
        try {
            node.setConnections(previewConnections);
        }
        catch(WrongFloorException e) {
            //TODO handle this
        }
        node.save();
    }

    public Boolean delete() {
        return manageMapController.attemptDeleteSelectedNode();
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
        previewRestricted(node.isRestricted());
    }

    public Boolean hasUnsavedChanges() {
        return getPreviewX() != node.getX()
                || getPreviewY() != node.getY()
                || !getPreviewRoomName().equals(node.getRoomName())
                || !getPreviewConnections().equals(node.getConnections())
                || getPreviewRestricted() != node.isRestricted();
    }
}
