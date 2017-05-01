package controllers.MapView.Map;

import controllers.IClickableController;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.util.Objects;

/**
 * Listeners for making the scene's canvas draggable and zoomable
 */
public class SceneGestures {
    //Minimum and maximum zoom levels
    private static final double MAX_SCALE = 5.0d;
    private static final double MIN_SCALE = 1.0d;

    private final DragContext sceneDragContext = new DragContext();

    private final PannableCanvas canvas;
    private final IClickableController mapController;

    public SceneGestures(PannableCanvas canvas, IClickableController mapController) {
        this.canvas = canvas;
        this.mapController = mapController;
        //this.mapController.getOverlay().scaleXProperty().bind(canvas.scaleXProperty());
        //this.mapController.getOverlay().scaleYProperty().bind(canvas.scaleYProperty());
        //this.mapController.getOverlay().translateXProperty().bind(canvas.translateXProperty());
        //this.mapController.getOverlay().translateYProperty().bind(canvas.translateYProperty());
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

            System.out.println("scene press");
            // update scene drag context
            sceneDragContext.mouseAnchorX = event.getSceneX();
            sceneDragContext.mouseAnchorY = event.getSceneY();
            sceneDragContext.translateAnchorX = mapController.getOverlay().getTranslateX();
            sceneDragContext.translateAnchorY = mapController.getOverlay().getTranslateY();
            //Debugging
            //System.out.println("LMB: " + sceneDragContext.mouseAnchorX + ", " + sceneDragContext.translateAnchorY);
        }

    };

    private final EventHandler<MouseEvent> onMouseClickedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            System.out.println("scene click");
            // update scene drag context
            sceneDragContext.mouseAnchorX = event.getSceneX();
            sceneDragContext.mouseAnchorY = event.getSceneY();
            sceneDragContext.translateAnchorX = mapController.getOverlay().getTranslateX();
            sceneDragContext.translateAnchorY = mapController.getOverlay().getTranslateY();

            if(event.getButton().name().equals("PRIMARY"))
                mapController.handleMouseClick(event);// unselect the current node
            // right mouse button => panning
            //if (event.getButton().name() != "SECONDARY")//if( !event.isSecondaryButtonDown())
            //    return;

        }

    };

    private final EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            // right mouse button => panning
//            System.out.println("mouse dragged");
            //if(!event.isSecondaryButtonDown())
                //return;
            // update the canvas
            //canvas.setTranslateX(sceneDragContext.translateAnchorX + event.getSceneX() - sceneDragContext.mouseAnchorX);
            //canvas.setTranslateY(sceneDragContext.translateAnchorY + event.getSceneY() - sceneDragContext.mouseAnchorY);

            mapController.getOverlay().setTranslateX(sceneDragContext.translateAnchorX + event.getSceneX() - sceneDragContext.mouseAnchorX);
            mapController.getOverlay().setTranslateY(sceneDragContext.translateAnchorY + event.getSceneY() - sceneDragContext.mouseAnchorY);


            //Clamp view as not to go off the map, even when zoomed
            if(mapController.getOverlay().getTranslateX() < -canvas.getBoundsInParent().getWidth()/2 * mapController.getOverlay().getScaleX())
                mapController.getOverlay().setTranslateX(-canvas.getBoundsInParent().getWidth()/2 * mapController.getOverlay().getScaleX());
            if(mapController.getOverlay().getTranslateX() > canvas.getBoundsInParent().getWidth()/2 * mapController.getOverlay().getScaleX())
                mapController.getOverlay().setTranslateX(canvas.getBoundsInParent().getWidth()/2 * mapController.getOverlay().getScaleX());

            if(mapController.getOverlay().getTranslateY() < -canvas.getBoundsInParent().getHeight()/2 * mapController.getOverlay().getScaleY())
                mapController.getOverlay().setTranslateY(-canvas.getBoundsInParent().getHeight()/2 * mapController.getOverlay().getScaleY());
            if(mapController.getOverlay().getTranslateY() > canvas.getBoundsInParent().getHeight()/2 * mapController.getOverlay().getScaleY())
                mapController.getOverlay().setTranslateY(canvas.getBoundsInParent().getHeight()/2 * mapController.getOverlay().getScaleY());

            // cancel event bubbling
            event.consume();
        }
    };

    /**
     * Mouse wheel handler: zoom on center of view
     */
    private final EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {

        @Override
        public void handle(ScrollEvent event) {
            double delta = 0.5;

            double scale = mapController.getOverlay().getScaleX(); // currently we only use Y, same value is used for X
            double oldScale = scale;

            if (event.getDeltaY() < 0)
                scale -= delta;
            else
                scale += delta;

            scale = clamp( scale, MIN_SCALE, MAX_SCALE);

            mapController.getOverlay().setScaleX(scale);
            mapController.getOverlay().setTranslateX(
                    mapController.getOverlay().getTranslateX()*scale/oldScale);//mapController.getOverlay().getScaleX());
            mapController.getOverlay().setScaleY(scale);

            mapController.getOverlay().setTranslateY(
                    mapController.getOverlay().getTranslateY()*scale/oldScale);//mapController.getOverlay().getScaleX());

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

    public void zoomToScale(double scale){
        scale = clamp( scale, MIN_SCALE, MAX_SCALE);
        double oldScale = mapController.getOverlay().getScaleX();

        mapController.getOverlay().setScaleX(scale);
        mapController.getOverlay().setTranslateX(
                mapController.getOverlay().getTranslateX()*scale/oldScale);//mapController.getOverlay().getScaleX());
        mapController.getOverlay().setScaleY(scale);
        mapController.getOverlay().setTranslateY(
                mapController.getOverlay().getTranslateY()*scale/oldScale);//mapController.getOverlay().getScaleX());
    }
    void zoomIn(){
        zoomToScale(mapController.getOverlay().getScaleX()+0.5);
    }
    void zoomOut(){
        zoomToScale(mapController.getOverlay().getScaleX()-0.5);
    }

    public void panToPos(double x, double y){
        mapController.getOverlay().setTranslateX(x);
        mapController.getOverlay().setTranslateY(y);


        //Clamp view as not to go off the map, even when zoomed
        if(mapController.getOverlay().getTranslateX() < -canvas.getBoundsInParent().getWidth()/2 * mapController.getOverlay().getScaleX())
            mapController.getOverlay().setTranslateX(-canvas.getBoundsInParent().getWidth()/2 * mapController.getOverlay().getScaleX());
        if(mapController.getOverlay().getTranslateX() > canvas.getBoundsInParent().getWidth()/2 * mapController.getOverlay().getScaleX())
            mapController.getOverlay().setTranslateX(canvas.getBoundsInParent().getWidth()/2 * mapController.getOverlay().getScaleX());

        if(mapController.getOverlay().getTranslateY() < -canvas.getBoundsInParent().getHeight()/2 * mapController.getOverlay().getScaleY())
            mapController.getOverlay().setTranslateY(-canvas.getBoundsInParent().getHeight()/2 * mapController.getOverlay().getScaleY());
        if(mapController.getOverlay().getTranslateY() > canvas.getBoundsInParent().getHeight()/2 * mapController.getOverlay().getScaleY())
            mapController.getOverlay().setTranslateY(canvas.getBoundsInParent().getHeight()/2 * mapController.getOverlay().getScaleY());
    }



}
