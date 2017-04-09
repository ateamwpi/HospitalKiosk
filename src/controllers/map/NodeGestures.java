package controllers.map;

import controllers.admin.AdminMapController;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

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
            // update node drag context
            nodeDragContext.mouseAnchorX = event.getSceneX();
            nodeDragContext.mouseAnchorY = event.getSceneY();
            nodeDragContext.translateAnchorX = node.getPreviewX();
            nodeDragContext.translateAnchorY = node.getPreviewY();
            // cancel event bubbling
            event.consume();
            // select node if not already selected
            if (!node.equals(adminMapController.getSelectedNode())) {
                // select the node
                adminMapController.selectNode(node);
            }
        }
    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            // right mouse button => panning
            if (event.getButton().name() != "PRIMARY")// !event.isPrimaryButtonDown())
                return;
            // get the map zoom scale
            double scale = canvas.getScale();
            // get the node being dragged
            DraggableNode node = (DraggableNode) event.getSource();
            // drag the node only if selected
            if (node.equals(adminMapController.getSelectedNode())) {
                // calculate the new coordinates
                int newX = (int) (nodeDragContext.translateAnchorX + ((event.getSceneX() - nodeDragContext.mouseAnchorX) / scale));
                int newY = (int) (nodeDragContext.translateAnchorY + ((event.getSceneY() - nodeDragContext.mouseAnchorY) / scale));
                // preview the node coordinates
                node.previewX(newX);
                node.previewY(newY);
            }
            // cancel event bubbling
            event.consume();
        }
    };
}
