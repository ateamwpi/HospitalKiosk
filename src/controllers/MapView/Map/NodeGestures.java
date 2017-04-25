package controllers.MapView.Map;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Listeners for making the nodes draggable via left mouse button. Considers if parent is zoomed.
 */
public class NodeGestures {

    private DragContext nodeDragContext = new DragContext();

    private PannableCanvas canvas;
    private ManageMapController manageMapController;

    public NodeGestures(PannableCanvas canvas, ManageMapController manageMapController) {
        this.canvas = canvas;
        this.manageMapController = manageMapController;
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
            // update node drag context
            System.out.println(manageMapController.getOverlay().getTranslateX());
            nodeDragContext.mouseAnchorX = event.getSceneX();
            nodeDragContext.mouseAnchorY = event.getSceneY();
            double scale = manageMapController.getOverlay().getScaleX();

            nodeDragContext.translateAnchorX = (manageMapController.getOverlay().getBoundsInParent().getWidth()/2) +
                                                            (node.getPreviewX() - manageMapController.getOverlay().getBoundsInParent().getWidth()/2) * scale
                                                    + manageMapController.getOverlay().getTranslateX()
                                                    + (scale-1)*(scale-1)/(scale) * manageMapController.getMapController().getOverlay().getBoundsInParent().getWidth()*0.5;
            nodeDragContext.translateAnchorY = (manageMapController.getOverlay().getBoundsInParent().getHeight()/2) +
                                                            (node.getPreviewY() - manageMapController.getOverlay().getBoundsInParent().getHeight()/2) * scale
                                                    + manageMapController.getOverlay().getTranslateY()
                                                    + (scale-1)*(scale-1)/(scale) * manageMapController.getMapController().getOverlay().getBoundsInParent().getHeight()*0.5;
            // cancel event bubbling
            event.consume();
            // select node if not already selected
            if (!node.equals(manageMapController.getSelectedNode())) {
                // select the node
                manageMapController.selectNode(node);
            }
        }
    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            // right mouse button => panning
            if (event.getButton().name() != "PRIMARY")// !event.isPrimaryButtonDown())
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
}
