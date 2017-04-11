package controllers.map;

import controllers.IClickableController;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.util.Objects;

/**
 * Listeners for making the scene's canvas draggable and zoomable
 */
public class SceneGestures {

    private static final double MAX_SCALE = 10.0d;
    private static final double MIN_SCALE = .1d;

    private final DragContext sceneDragContext = new DragContext();

    private final PannableCanvas canvas;
    private final IClickableController controller;

    public SceneGestures(PannableCanvas canvas, IClickableController controller) {
        this.canvas = canvas;
        this.controller = controller;
    }

    public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseClickedEventHandler() {
        return onMouseClickedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }

    public EventHandler<ScrollEvent> getOnScrollEventHandler() {
        return onScrollEventHandler;
    }

    private final EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            // right mouse button => panning
            if (!Objects.equals(event.getButton().name(), "PRIMARY"))//if( !event.isSecondaryButtonDown())
                return;
            System.out.println("scene press");
            // update scene drag context
            sceneDragContext.mouseAnchorX = event.getSceneX();
            sceneDragContext.mouseAnchorY = event.getSceneY();
            sceneDragContext.translateAnchorX = canvas.getTranslateX();
            sceneDragContext.translateAnchorY = canvas.getTranslateY();

        }

    };

    private final EventHandler<MouseEvent> onMouseClickedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            // right mouse button => panning
            if (!Objects.equals(event.getButton().name(), "PRIMARY"))//if( !event.isSecondaryButtonDown())
                return;
            System.out.println("scene click");
            // unselect the current node
            controller.handleMouseClick(event);
            // update scene drag context
            sceneDragContext.mouseAnchorX = event.getSceneX();
            sceneDragContext.mouseAnchorY = event.getSceneY();
            sceneDragContext.translateAnchorX = canvas.getTranslateX();
            sceneDragContext.translateAnchorY = canvas.getTranslateY();
        }

    };

    private final EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            // right mouse button => panning
            if(!event.isSecondaryButtonDown())
                return;
            // update the canvas
            canvas.setTranslateX(sceneDragContext.translateAnchorX + event.getSceneX() - sceneDragContext.mouseAnchorX);
            canvas.setTranslateY(sceneDragContext.translateAnchorY + event.getSceneY() - sceneDragContext.mouseAnchorY);
            // cancel event bubbling
            event.consume();
        }
    };

    /**
     * Mouse wheel handler: zoom to pivot point
     */
    private final EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {

        @Override
        public void handle(ScrollEvent event) {
            double delta = 1.2;

            double scale = canvas.getScale(); // currently we only use Y, same value is used for X
            double oldScale = scale;

            if (event.getDeltaY() < 0)
                scale /= delta;
            else
                scale *= delta;

            scale = clamp( scale, MIN_SCALE, MAX_SCALE);

            double f = (scale / oldScale)-1;

            double dx = (event.getSceneX() - (canvas.getBoundsInParent().getWidth()/2 + canvas.getBoundsInParent().getMinX()));
            double dy = (event.getSceneY() - (canvas.getBoundsInParent().getHeight()/2 + canvas.getBoundsInParent().getMinY()));

            canvas.setScale( scale);

            // note: pivot value must be untransformed, i. e. without scaling
            canvas.setPivot(f*dx, f*dy);

            event.consume();

        }

    };


    private static double clamp(double value, double min, double max) {

        if( Double.compare(value, min) < 0)
            return min;

        if( Double.compare(value, max) > 0)
            return max;

        return value;
    }
}
