package controllers.map;

import controllers.IClickableController;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 * Listeners for making the scene's canvas draggable and zoomable
 */
public class SceneGestures {
    //Minimum and maximum zoom levels
    private static final double MAX_SCALE = 5.0d;
    private static final double MIN_SCALE = 1.0d;

    private DragContext sceneDragContext = new DragContext();

    PannableCanvas canvas;
    IClickableController mapController;

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

    private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            // right mouse button => panning
            if (event.getButton().name() != "SECONDARY")//if( !event.isSecondaryButtonDown())
                return;
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

    private EventHandler<MouseEvent> onMouseClickedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            if(event.getButton().name().equals("PRIMARY"))
                mapController.handleMouseClick(event);// unselect the current node
            // right mouse button => panning
            if (event.getButton().name() != "SECONDARY")//if( !event.isSecondaryButtonDown())
                return;
            System.out.println("scene click");
            // update scene drag context
            sceneDragContext.mouseAnchorX = event.getSceneX();
            sceneDragContext.mouseAnchorY = event.getSceneY();
            sceneDragContext.translateAnchorX = mapController.getOverlay().getTranslateX();
            sceneDragContext.translateAnchorY = mapController.getOverlay().getTranslateY();
        }

    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            // right mouse button => panning
            if(!event.isSecondaryButtonDown())
                return;
            // update the canvas
            //canvas.setTranslateX(sceneDragContext.translateAnchorX + event.getSceneX() - sceneDragContext.mouseAnchorX);
            //canvas.setTranslateY(sceneDragContext.translateAnchorY + event.getSceneY() - sceneDragContext.mouseAnchorY);

            mapController.getOverlay().setTranslateX(sceneDragContext.translateAnchorX + event.getSceneX() - sceneDragContext.mouseAnchorX);
            mapController.getOverlay().setTranslateY(sceneDragContext.translateAnchorY + event.getSceneY() - sceneDragContext.mouseAnchorY);


            //Clamp view as not to go off the map, even when zoomed
            if(mapController.getOverlay().getTranslateX() < -canvas.getWidth()/2 * mapController.getOverlay().getScaleX())
                mapController.getOverlay().setTranslateX(-canvas.getWidth()/2 * mapController.getOverlay().getScaleX());
            if(mapController.getOverlay().getTranslateX() > canvas.getWidth()/2 * mapController.getOverlay().getScaleX())
                mapController.getOverlay().setTranslateX(canvas.getWidth()/2 * mapController.getOverlay().getScaleX());

            if(mapController.getOverlay().getTranslateY() < -canvas.getHeight()/2 * mapController.getOverlay().getScaleY())
                mapController.getOverlay().setTranslateY(-canvas.getHeight()/2 * mapController.getOverlay().getScaleY());
            if(mapController.getOverlay().getTranslateY() > canvas.getHeight()/2 * mapController.getOverlay().getScaleY())
                mapController.getOverlay().setTranslateY(canvas.getHeight()/2 * mapController.getOverlay().getScaleY());

            // cancel event bubbling
            event.consume();
        }
    };

    /**
     * Mouse wheel handler: zoom on center of view
     */
    private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {

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

            double f = (scale / oldScale)-1;

            //double dx = (event.getSceneX() - (canvas.getBoundsInParent().getWidth()/2 + canvas.getBoundsInParent().getMinX()/2));
            //double dy = (event.getSceneY() - (canvas.getBoundsInParent().getHeight()/2 + canvas.getBoundsInParent().getMinY()/2));

            //canvas.setScale(scale);

            mapController.getOverlay().setScaleX(scale);
            mapController.getOverlay().setTranslateX(
                    mapController.getOverlay().getTranslateX()*scale/oldScale);//mapController.getOverlay().getScaleX());
            mapController.getOverlay().setScaleY(scale);

            mapController.getOverlay().setTranslateY(
                    mapController.getOverlay().getTranslateY()*scale/oldScale);//mapController.getOverlay().getScaleX());

            //System.out.println(mapController.getOverlay().getTranslateX());
            //canvas.setTranslateX(4 * mapController.getOverlay().getScaleX() * mapController.getOverlay().getTranslateX());
            //canvas.setTranslateY(2 * mapController.getOverlay().getScaleY() * mapController.getOverlay().getTranslateY());

            // note: pivot value must be untransformed, i. e. without scaling
            canvas.setPivot(f,f);//*dx, f*dy);
            //canvas.setPivot(f/**dx*/, f/**dy*/);

            event.consume();

        }

    };


    public static double clamp( double value, double min, double max) {

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
    public void zoomIn(){
        zoomToScale(mapController.getOverlay().getScaleX()+0.5);
    }
    public void zoomOut(){
        zoomToScale(mapController.getOverlay().getScaleX()-0.5);
    }

    public void panToPos(double x, double y){
        mapController.getOverlay().setTranslateX(x);
        mapController.getOverlay().setTranslateY(y);


        //Clamp view as not to go off the map, even when zoomed
        if(mapController.getOverlay().getTranslateX() < -canvas.getWidth()/2 * mapController.getOverlay().getScaleX())
            mapController.getOverlay().setTranslateX(-canvas.getWidth()/2 * mapController.getOverlay().getScaleX());
        if(mapController.getOverlay().getTranslateX() > canvas.getWidth()/2 * mapController.getOverlay().getScaleX())
            mapController.getOverlay().setTranslateX(canvas.getWidth()/2 * mapController.getOverlay().getScaleX());

        if(mapController.getOverlay().getTranslateY() < -canvas.getHeight()/2 * mapController.getOverlay().getScaleY())
            mapController.getOverlay().setTranslateY(-canvas.getHeight()/2 * mapController.getOverlay().getScaleY());
        if(mapController.getOverlay().getTranslateY() > canvas.getHeight()/2 * mapController.getOverlay().getScaleY())
            mapController.getOverlay().setTranslateY(canvas.getHeight()/2 * mapController.getOverlay().getScaleY());
    }



}
