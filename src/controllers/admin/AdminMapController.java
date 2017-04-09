package controllers.admin;

import controllers.IClickableController;
import controllers.map.*;
import core.KioskMain;
import core.NodeInUseException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;
import javafx.util.Pair;
import models.path.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dylan on 4/8/17.
 */
public class AdminMapController implements IClickableController {

    private Collection<Node> nodes = new ArrayList<>();
    private ManageMapViewController manageMapViewController;
    private DraggableNode selectedNode;
    private Map<Node, DraggableNode> draggableNodes = new HashMap<>();
    private Map<Pair<DraggableNode, DraggableNode>, Line> draggableNodeConnections = new HashMap<>();
    private NodeGestures nodeGestures;
    private MapController mapController;

    @FXML
    private AnchorPane mapContainer;

    //// Public API ////

    public AdminMapController(ManageMapViewController manageMapViewController) {
        // load all the nodes
        nodes = KioskMain.getPath().getGraph().values();
        // set map manager
        this.manageMapViewController = manageMapViewController;
        // get the loader
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/AdminMap.fxml"));
        // set the controller
        loader.setController(this);
        // load the view
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public Region getRoot() {
        return mapContainer;
    }

    public void handleMouseClick(MouseEvent e) {
        // unselect if already selecting node
        if (nodeIsSelected()) {
            unselectNode();
            // add new node if not selecting node
        } else {
            // convert the mouse coordinates to map coordinates
            Point2D p = new Point2D(e.getX(), e.getY());
            addNode(p.getX(), p.getY(), "NONE");
            // pass along mouse press to the node
            selectedNode.fireEvent(e);
        }
    }

    public NodeGestures getNodeGestures() {
        return nodeGestures;
    }

    public void addNode(double x, double y, String room) {
        System.out.println("add node");
        // create new node
        Node node = new Node((int) x, (int) y, 4, room);
        // create new visual node with gestures
        DraggableNode draggableNode = getDraggableNode(node);
        // draw node with gestures
        drawDraggableNode(draggableNode);
        // select the node
        selectNode(draggableNode);
        // add node to path manager
        KioskMain.getPath().addNode(node);
    }

    public void deleteSelectedNode() {
        if (selectedNode != null) {
            System.out.println("delete node");
            // try to delete the node
            try {
                // delete the node from the db
                KioskMain.getPath().removeNode(selectedNode.getNode());
                // remove the visual node from the overlay
                mapController.removeOverlay(selectedNode);
                // unselect node
                unselectNode();
            } catch (NodeInUseException e) {
                showNodeInUseAlert();
            }
        }
    }

    public void selectNode(DraggableNode node) {
        unselectNode();
        selectedNode = node;
        selectedNode.select();
        manageMapViewController.selectNode(selectedNode);
    }

    public Boolean nodeIsSelected() {
        return selectedNode != null;
    }

    public void unselectNode() {
        if (selectedNode != null) {
            selectedNode.unselect();
            selectedNode = null;
            manageMapViewController.unselectNode();
        }
    }

    public DraggableNode getSelectedNode() {
        return selectedNode;
    }

    public DraggableNode getDraggableNode(Node node) {
        DraggableNode draggableNode = draggableNodes.get(node);
        if (draggableNode == null) {
            draggableNode = new DraggableNode(node, nodeGestures, this);
            draggableNodes.put(node, draggableNode);
            return draggableNode;
        } else {
            return draggableNode;
        }
    }

    public void drawDraggableConnection(DraggableNode nodeA, DraggableNode nodeB) {
        Line line = new Line();
        // bind line coordinates with the node coordinates
        line.startXProperty().bind(nodeA.previewXProperty());
        line.startYProperty().bind(nodeA.previewYProperty());
        line.endXProperty().bind(nodeB.previewXProperty());
        line.endYProperty().bind(nodeB.previewYProperty());
        // add the line to the map of connections
        draggableNodeConnections.put(getNodePair(nodeA, nodeB), line);
        // add the line to the overlay
        mapController.addOverlay(line);
    }

    public void removeDraggableConnection(DraggableNode nodeA, DraggableNode nodeB) {
        // get the line corresponding to the nodes
        Line line = draggableNodeConnections.get(getNodePair(nodeA, nodeB));
        // remove the line from the overlay
        mapController.removeOverlay(line);
    }

    //// Private API ////

    @FXML
    private void initialize() {
        // load the map controller
        mapController = new MapController();
        // get the map
        Region map = mapController.getRoot();
        // add the map to the container
        mapContainer.getChildren().add(map);
        // get the map canvas
        PannableCanvas canvas = mapController.getCanvas();
        // create the node gesture for dragging
        nodeGestures = new NodeGestures(canvas, this);
        // create the scene gestures for zooming and panning
        SceneGestures sceneGestures = new SceneGestures(canvas, this);
        // register handlers zooming and panning
        mapContainer.addEventHandler(MouseEvent.ANY, new ClickDragHandler(sceneGestures.getOnMouseClickedEventHandler(), sceneGestures.getOnMouseDraggedEventHandler()));
        mapContainer.addEventHandler(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        // draw all of the nodes
        drawAllNodes();
    }

    private void drawAllNodes() {
        for (Node node : nodes) {
            // create new visual node with gestures
            DraggableNode draggableNode = getDraggableNode(node);
            // draw the node
            drawDraggableNode(draggableNode);
            // draw the node connections
            drawDraggableNodeConnections(draggableNode);
        }
    }

    private void drawDraggableNodeConnections(DraggableNode draggableNode) {
        Node node = draggableNode.getNode();
        for (Node other : node.getConnections()) {
            // don't draw connection twice
            if (node.getID() < other.getID()) {
                // draw connection
                drawDraggableConnection(draggableNode, getDraggableNode(other));
            }
        }
    }

    private Pair<DraggableNode, DraggableNode> getNodePair(DraggableNode nodeA, DraggableNode nodeB) {
        return (nodeA.getNode().getID() > nodeB.getNode().getID()) ? new Pair(nodeA, nodeB) : new Pair(nodeB, nodeA);
    }

    private void showNodeInUseAlert() {
        Alert nodeUsed = new Alert(Alert.AlertType.ERROR);
        nodeUsed.setHeaderText("This Node is in Use");
        nodeUsed.setContentText("A location in the directory currently refers to this node.");
        nodeUsed.setTitle("Node In Use");
        nodeUsed.showAndWait();
    }

    private void drawDraggableNode(DraggableNode draggableNode) {
        // add visual node to overlay
        mapController.addOverlay(draggableNode);
    }
}