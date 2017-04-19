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
            System.out.println(adminMapController.getOverlay().getTranslateX());
            nodeDragContext.mouseAnchorX = event.getSceneX();
            nodeDragContext.mouseAnchorY = event.getSceneY();
            double scale = adminMapController.getOverlay().getScaleX();
            //1->0
            //1.5->1/6
            //2->1/2
            //2.5->0.9?
            //3

            //(s-1)/s
            //(s-1)(s-1)/(2s)
            //0/1->0
            //1/3->1/6
            //2/4->1/2
            //3/5->9/10
            //4/6->

            nodeDragContext.translateAnchorX = (adminMapController.getOverlay().getBoundsInParent().getWidth()/2) +
                                                            (node.getPreviewX() - adminMapController.getOverlay().getBoundsInParent().getWidth()/2) * scale
                                                    + adminMapController.getOverlay().getTranslateX()
                                                    + (scale-1)*(scale-1)/(scale) * adminMapController.getMapController().getOverlay().getBoundsInParent().getWidth()*0.5;
            nodeDragContext.translateAnchorY = (adminMapController.getOverlay().getBoundsInParent().getHeight()/2) +
                                                            (node.getPreviewY() - adminMapController.getOverlay().getBoundsInParent().getHeight()/2) * scale
                                                    + adminMapController.getOverlay().getTranslateY()
                                                    + (scale-1)*(scale-1)/(scale) * adminMapController.getMapController().getOverlay().getBoundsInParent().getHeight()*0.5;
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
