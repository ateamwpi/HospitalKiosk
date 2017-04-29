package controllers.MapView.Map;

import controllers.AbstractController;
import controllers.IClickableController;
import controllers.MapView.ManageMapView.ManageMapViewController;
import core.KioskMain;
import core.Utils;
import core.exception.NodeInUseException;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.util.Pair;
import models.path.Node;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by dylan on 4/8/17.
 */
public class ManageMapController extends AbstractController implements IClickableController {

    private Collection<Node> nodes;

    private ManageMapViewController manageMapViewController;
    private DraggableNode selectedNode;
    private Map<Node, DraggableNode> draggableNodes;
    private Map<Pair<DraggableNode, DraggableNode>, Line> draggableNodeConnections;
    private NodeGestures nodeGestures;

    public MapController getMapController() {
        return mapController;
    }

    private MapController mapController;

    @FXML
    private AnchorPane mapContainer;

    //// Public API ////

    public ManageMapController(ManageMapViewController manageMapViewController) {
        super(manageMapViewController);
    }

    @Override
    public void initData(Object... data) {
        // set map manager
        manageMapViewController = (ManageMapViewController) data[0];
        // load all the nodes
        nodes = KioskMain.getPath().getGraph().values();
        // init props
        draggableNodes = new HashMap<>();
        draggableNodeConnections = new HashMap<>();
    }

    @Override
    public String getURL() {
        return "resources/views/MapView/Map/ManageMap.fxml";
    }

    @Override
    public Parent getRoot() {
        return mapContainer;
    }

    public void setFloor(int floor) {
        attemptUnselectNode(isUnselected -> {
            if(isUnselected) {
                mapController.setFloor(floor);
                removeAllNodes();
                drawAllNodes();
            }
        }, false);
    }

    private void removeAllNodes() {
        for(DraggableNode node : draggableNodes.values()) {
            removeDraggableNode(node);
        }
    }

    private void removeDraggableNode(DraggableNode node) {
        // remove node connections
        for (Node connection : node.getPreviewConnections()) {
            node.removePreviewConnection(connection);
        }
        // remove node from map
        draggableNodes.remove(node);
        // remove node from overlay
        mapController.removeOverlay(node);
    }

    public void handleMouseClick(MouseEvent e) {
        // unselect if already selecting node
        if (nodeIsSelected()) {
            attemptUnselectNode(isUnselected -> {}, false);
            // add new node if not selecting node
        } else {
            // convert the mouse coordinates to map coordinates
            Point2D p = new Point2D(e.getX(), e.getY());
            addNode(p.getX(), p.getY(), "NONE");
            // pass along mouse press to the node
            selectedNode.fireEvent(e);
        }
    }

    @Override
    public Group getOverlay() {
        return this.mapController.getOverlay();
    }

    public NodeGestures getNodeGestures() {
        return nodeGestures;
    }

    public void addNode(double x, double y, String room) {
        System.out.println("add node");
        //Update without weird offset
        double scale = getOverlay().getScaleX();
        x = (x / scale
                - getOverlay().getTranslateX()/scale
                +(scale-1)/(scale*scale)*getOverlay().getBoundsInParent().getWidth()*0.5
        );
        y = (y / scale
                - getOverlay().getTranslateY()/scale
                +(scale-1)/(scale*scale)*getOverlay().getBoundsInParent().getHeight()*0.5
        );

        // create new node
        Node node = new Node(   (int) x,
                                (int) y, mapController.getFloor(), false, room);
        // create new visual node with gestures
        DraggableNode draggableNode = getDraggableNode(node);
        // draw node with gestures
        drawDraggableNode(draggableNode);
        // select the node
        selectNode(draggableNode);
        // add node to path manager
        KioskMain.getPath().addNode(node);
    }

    public void attemptDeleteSelectedNode(Consumer<Boolean> setDeleted) {
        if (selectedNode == null) {
            setDeleted.accept(false);
        } else {
            warnDeleteNode(Utils.applyAndAccept(isDeleted -> {
                if (isDeleted) {
                    try {
                        deleteSelectedNode();
                    } catch (NodeInUseException e) {
                        showNodeInUseAlert();
                        return false;
                    }
                }
                return isDeleted;
            }, setDeleted));
        }
    }

    private void warnDeleteNode(Consumer<Boolean> setDeleted) {
        Utils.showOption(getManageMapViewController().getRoot(),
                "Delete Node",
                "Are you sure you want to delete? This cannot be undone!",
                "Cancel",
                "Delete",
                setDeleted);
    }

    private void deleteSelectedNode() throws NodeInUseException {
        // try to delete the node
        System.out.println("delete node");
        // keep reference to selected node
        DraggableNode nodeToDelete = selectedNode;
        // delete the node from the db
        KioskMain.getPath().removeNode(nodeToDelete.getNode());
        // remove the visual node from the overlay
        removeDraggableNode(nodeToDelete);
        // unselect the node
        unselectNode(false);
    }

    void selectNode(DraggableNode node) {
        attemptUnselectNode(isUnselected -> {
            if (isUnselected) {
                selectedNode = node;
                selectedNode.select();
                manageMapViewController.selectNode(selectedNode);
            }
        }, true);
    }

    ManageMapViewController getManageMapViewController() {
        return manageMapViewController;
    }

    private Boolean nodeIsSelected() {
        return selectedNode != null;
    }

    // return true if unselected
    public void attemptUnselectNode(Consumer<Boolean> setUnselectNode, boolean reselect) {
        if (selectedNode == null) {
            setUnselectNode.accept(true);
        } else {
            if (selectedNode.hasUnsavedChanges()) {
                warnDiscardChanges(setUnselectNode.andThen(isUnselected -> {
                    if (isUnselected) {
                        unselectNode(reselect);
                    }
                }));
            } else {
                unselectNode(reselect);
                setUnselectNode.accept(true);
            }
        }
    }


    // returns true if admin chooses to discard changes
    private void warnDiscardChanges(Consumer<Boolean> setDiscard) {
        Utils.showOption(getManageMapViewController().getRoot(),
                "Unsaved Changes",
                "All unsaved changed will be lost. Are you sure you want to continue?",
                "Cancel",
                "Discard Changes",
                setDiscard);
    }

    private void unselectNode(boolean reselect) {
        selectedNode.unselect();
        selectedNode = null;
        manageMapViewController.unselectNode(reselect);

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
        mapController.addOverlay(0, line);
    }

    public void removeDraggableConnection(DraggableNode nodeA, DraggableNode nodeB) {
        Pair<DraggableNode, DraggableNode> pair = getNodePair(nodeA, nodeB);
        // get the line corresponding to the nodes
        Line line = draggableNodeConnections.get(pair);
        // remove the line from the overlay
        mapController.removeOverlay(line);
        // remove the pair from the connections
        draggableNodeConnections.remove(pair);
    }

    //// Private API ////

    @FXML
    private void initialize() {
        // load the map controller
        mapController = new MapController();
        // add the map to the container
        mapContainer.getChildren().add(mapController.getRoot());
        // get the map canvas
        PannableCanvas canvas = mapController.getCanvas();
        // create the node gesture for dragging
        nodeGestures = new NodeGestures(this);
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
            if (node.getFloor() == mapController.getFloor()) {
                // create new visual node with gestures
                DraggableNode draggableNode = getDraggableNode(node);
                // draw the node
                drawDraggableNode(draggableNode);
                // draw the node connections
                drawDraggableNodeConnections(draggableNode);
            }
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
        return (nodeA.getNode().getID() > nodeB.getNode().getID()) ? new Pair<>(nodeA, nodeB) : new Pair<>(nodeB, nodeA);
    }

    private void showNodeInUseAlert() {
        Utils.showAlert(getManageMapViewController().getRoot(), "Node In Use", "A location in the directory currently refers to this node.");
    }

    private void drawDraggableNode(DraggableNode draggableNode) {
        // add visual node to overlay
        mapController.addOverlay(draggableNode);
    }
}
