package controllers.map;

import controllers.admin.AdminMapController;
import core.Utils;
import core.exception.WrongFloorException;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import models.path.Node;

import java.util.Collection;

/**
 * Listeners for making the nodes draggable via left mouse button. Considers if parent is zoomed.
 */
public class NodeGestures {

    private DragContext nodeDragContext = new DragContext();

    private PannableCanvas canvas;
    private AdminMapController adminMapController;

    public NodeGestures(PannableCanvas canvas, AdminMapController adminMapController) {
        this.canvas = canvas;
        this.adminMapController = adminMapController;
    }

    public EventHandler<MouseEvent> getOnMouseClickedEventHandler() {
        return onMouseClickedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }

    private EventHandler<MouseEvent> onMouseClickedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            // right mouse button => panning
            if (event.getButton().name() != "PRIMARY") {// !event.isPrimaryButtonDown())
                return;
            }
            System.out.println("node clicked");
            // get the node clicked on
            DraggableNode node = (DraggableNode) event.getSource();
            //Shift click is used only for node connections
            if(event.isShiftDown() && adminMapController.getSelectedNode() != null){
                //Given that a node is selected and shift is held down
                //toggle connection

                //If already connected
                if(adminMapController.getSelectedNode().getNode().getConnections().contains(node)){
                    //Disconnect
                    deleteNeighbor(event, node);
                } else {
                    addNeighbor(event, node);
                }
            }
            else {
                // update node drag context
                System.out.println(adminMapController.getOverlay().getTranslateX());
                nodeDragContext.mouseAnchorX = event.getSceneX();
                nodeDragContext.mouseAnchorY = event.getSceneY();
                double scale = adminMapController.getOverlay().getScaleX();

                nodeDragContext.translateAnchorX = (adminMapController.getOverlay().getBoundsInParent().getWidth() / 2) +
                        (node.getPreviewX() - adminMapController.getOverlay().getBoundsInParent().getWidth() / 2) * scale
                        + adminMapController.getOverlay().getTranslateX()
                        + (scale - 1) * (scale - 1) / (scale) * adminMapController.getMapController().getOverlay().getBoundsInParent().getWidth() * 0.5;
                nodeDragContext.translateAnchorY = (adminMapController.getOverlay().getBoundsInParent().getHeight() / 2) +
                        (node.getPreviewY() - adminMapController.getOverlay().getBoundsInParent().getHeight() / 2) * scale
                        + adminMapController.getOverlay().getTranslateY()
                        + (scale - 1) * (scale - 1) / (scale) * adminMapController.getMapController().getOverlay().getBoundsInParent().getHeight() * 0.5;
                // cancel event bubbling
                event.consume();
                // select node if not already selected
                if (!node.equals(adminMapController.getSelectedNode())) {
                    // select the node
                    adminMapController.selectNode(node);
                }
            }
        }
    };

    //functions for node connection gestures
    //Based on those in Manage Map View Controller
    private void addNeighbor(MouseEvent event, DraggableNode other) {
        // get the node
        Node node = other.getNode();
        // check if node exists and is not itself
        if (node == null || node.equals(adminMapController.getSelectedNode())) {
            alertAddConnectionError();
            return;
        }
        // add the preview connection
        adminMapController.getSelectedNode().previewConnection(node);
        //refreshScene();
        //Connect the two nodes if they are on the same floor
        try {
            adminMapController.getSelectedNode().getNode().addConnection(node);
        }
        catch(WrongFloorException wfe){
            Utils.showAlert(adminMapController.getManageMapViewController().getRoot(), "Invalid Node Connection!", "Cannot connect nodes on multiple floors!");
        }
    }

    private void alertAddConnectionError() {
        Utils.showAlert(adminMapController.getManageMapViewController().getRoot(), "Invalid Node Connection!", "This node cannot be connected to itself!");
    }

    @FXML
    private void deleteNeighbor(MouseEvent event, DraggableNode other) {
        // get the node
        Node node = other.getNode();
        // check if node exists and is not itself
        if (node == null || node.equals(adminMapController.getSelectedNode())) {
            alertAddConnectionError();
            return;
        }
        // remove the preview connection
        adminMapController.getSelectedNode().removePreviewConnection(node);
        // update the table
        adminMapController.getSelectedNode().getNode().removeConnection(node);
    }

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            // right mouse button => panning
            if (event.getButton().name() != "PRIMARY")// !event.isPrimaryButtonDown())
                return;
            // get the map zoom scale
            double scale = adminMapController.getOverlay().getScaleX();//Should be same as scaleY
            // get the node being dragged
            DraggableNode node = (DraggableNode) event.getSource();
            // drag the node only if selected
            if (node.equals(adminMapController.getSelectedNode())) {
                // calculate the new coordinates
                int newX = (int) ((nodeDragContext.translateAnchorX + event.getSceneX() - nodeDragContext.mouseAnchorX) / scale
                - adminMapController.getMapController().getOverlay().getTranslateX()/scale
                        +(scale-1)/(scale*scale)*adminMapController.getMapController().getOverlay().getBoundsInParent().getWidth()*0.5
                );
                int newY = (int) ((nodeDragContext.translateAnchorY + event.getSceneY() - nodeDragContext.mouseAnchorY) / scale
                - adminMapController.getMapController().getOverlay().getTranslateY()/scale
                        +(scale-1)/(scale*scale)*adminMapController.getMapController().getOverlay().getBoundsInParent().getHeight()*0.5
                );
                // preview the node coordinates
                node.previewX(newX);
                node.previewY(newY);
            }
            // cancel event bubbling
            event.consume();
        }
    };
}
