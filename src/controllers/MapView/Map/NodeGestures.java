package controllers.MapView.Map;

import core.Utils;
import core.exception.WrongFloorException;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import models.path.Node;

import java.util.Objects;

/**
 * Listeners for making the nodes draggable via left mouse button. Considers if parent is zoomed.
 */
class NodeGestures {

    private final DragContext nodeDragContext = new DragContext();

    private final ManageMapController manageMapController;

    NodeGestures(ManageMapController manageMapController) {
        this.manageMapController = manageMapController;
    }

    EventHandler<MouseEvent> getOnMouseClickedEventHandler() {
        return onMouseClickedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }

    public EventHandler<ScrollEvent> getOnScrollEventHandler() {return onScrollEventHandler; }

    private final EventHandler<MouseEvent> onMouseClickedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            // right mouse button => panning
            if (!Objects.equals(event.getButton().name(), "PRIMARY")) {// !event.isPrimaryButtonDown())
                return;
            }
            System.out.println("node clicked");
            // get the node clicked on
            DraggableNode node = (DraggableNode) event.getSource();

            if (event.isShiftDown() && manageMapController.getSelectedNode() != null) {
                //Given that a node is selected and shift is held down
                //toggle connection

                //If already connected
                if (manageMapController.getSelectedNode().getNode().getConnections().contains(node.getNode())) {
                    //Disconnect
                    deleteNeighbor(event, node);
                } else {
                    addNeighbor(event, node);
                }
            } else {
                // update node drag context
                System.out.println(manageMapController.getOverlay().getTranslateX());
                nodeDragContext.mouseAnchorX = event.getSceneX();
                nodeDragContext.mouseAnchorY = event.getSceneY();
                double scale = manageMapController.getOverlay().getScaleX();

                nodeDragContext.translateAnchorX = (manageMapController.getOverlay().getBoundsInParent().getWidth() / 2) +
                        (node.getPreviewX() - manageMapController.getOverlay().getBoundsInParent().getWidth() / 2) * scale
                        + manageMapController.getOverlay().getTranslateX()
                        + (scale - 1) * (scale - 1) / (scale) * manageMapController.getMapController().getOverlay().getBoundsInParent().getWidth() * 0.5;
                nodeDragContext.translateAnchorY = (manageMapController.getOverlay().getBoundsInParent().getHeight() / 2) +
                        (node.getPreviewY() - manageMapController.getOverlay().getBoundsInParent().getHeight() / 2) * scale
                        + manageMapController.getOverlay().getTranslateY()
                        + (scale - 1) * (scale - 1) / (scale) * manageMapController.getMapController().getOverlay().getBoundsInParent().getHeight() * 0.5;
                // cancel event bubbling
                event.consume();
                // select node if not already selected
                if (!node.equals(manageMapController.getSelectedNode())) {
                    // select the node
                    manageMapController.selectNode(node);
                }
            }
        }
    };

    //functions for node connection gestures
    //Based on those in Manage Map View Controller
    public void addNeighbor(MouseEvent event, DraggableNode other) {
        // get the node
        Node node = other.getNode();
        // check if node exists and is not itself
        if (node == null || node.equals(manageMapController.getSelectedNode())) {
            alertAddConnectionError();
            return;
        }
        // add the preview connection
        manageMapController.getSelectedNode().previewConnection(node);
        //Connect the two nodes if they are on the same floor
        try {
            manageMapController.getSelectedNode().getNode().addConnection(node);
        } catch (WrongFloorException wfe) {
            Utils.showAlert(manageMapController.getManageMapViewController().getRoot(), "Invalid Node Connection!", "Cannot connect nodes on multiple floors!");
        }
    }

    private void alertAddConnectionError() {
        Utils.showAlert(manageMapController.getManageMapViewController().getRoot(), "Invalid Node Connection!", "This node cannot be connected to itself!");
    }

    private void deleteNeighbor(MouseEvent event, DraggableNode other) {
        // get the node
        Node node = other.getNode();
        // check if node exists and is not itself
        if (node == null || node.equals(manageMapController.getSelectedNode())) {
            alertAddConnectionError();
            return;
        }
        // remove the preview connection
        manageMapController.getSelectedNode().removePreviewConnection(node);
        // update the table
        //manageMapController.getSelectedNode().Connection(node);
    }

    private final EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            // right mouse button => panning
            if (!Objects.equals(event.getButton().name(), "PRIMARY"))// !event.isPrimaryButtonDown())
                return;
            // get the map zoom scale
            double scale = manageMapController.getOverlay().getScaleX();//Should be same as scaleY
            // get the node being dragged
            DraggableNode node = (DraggableNode) event.getSource();
            // drag the node only if selected
            if (node.equals(manageMapController.getSelectedNode())) {
                // calculate the new coordinates
                int newX = (int) ((nodeDragContext.translateAnchorX + event.getSceneX() - nodeDragContext.mouseAnchorX) / scale
                - manageMapController.getMapController().getOverlay().getTranslateX()/scale
                        +(scale-1)/(scale*scale)* manageMapController.getMapController().getOverlay().getBoundsInParent().getWidth()*0.5
                );
                int newY = (int) ((nodeDragContext.translateAnchorY + event.getSceneY() - nodeDragContext.mouseAnchorY) / scale
                - manageMapController.getMapController().getOverlay().getTranslateY()/scale
                        +(scale-1)/(scale*scale)* manageMapController.getMapController().getOverlay().getBoundsInParent().getHeight()*0.5
                );
                // preview the node coordinates
                node.previewX(newX);
                node.previewY(newY);
            }
            // cancel event bubbling
            event.consume();
        }
    };


    //Must be able to scroll when hovering over nodes, too
    private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {

        @Override
        public void handle(ScrollEvent event) {
            double delta = 0.5;

            double scale = manageMapController.getMapController().getOverlay().getScaleX(); // currently we only use Y, same value is used for X
            double oldScale = scale;

            if (event.getDeltaY() < 0)
                scale -= delta;
            else
                scale += delta;

            if (scale < 1.0)
                scale = 1.0;
            else if (scale > 5.0)
                scale = 5.0;

            manageMapController.getMapController().getOverlay().setScaleX(scale);
            manageMapController.getMapController().getOverlay().setTranslateX(
                    manageMapController.getMapController().getOverlay().getTranslateX() * scale / oldScale);//mapController.getOverlay().getScaleX());
            manageMapController.getMapController().getOverlay().setScaleY(scale);
            manageMapController.getMapController().getOverlay().setTranslateY(
                    manageMapController.getMapController().getOverlay().getTranslateY() * scale / oldScale);//mapController.getOverlay().getScaleX());

            event.consume();

        }

    };
}
