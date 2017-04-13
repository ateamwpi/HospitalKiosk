package controllers.map;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * The canvas which holds all of the nodes of the application.
 */
public class PannableCanvas extends Pane {

    private DoubleProperty myScale = new SimpleDoubleProperty(1.0);
    private MapController mapController;

    public PannableCanvas(MapController mapController) {
        this.mapController = mapController;

        // add scale transform
        scaleXProperty().bind(myScale);
        scaleYProperty().bind(myScale);

        // logging
        //addEventFilter(MouseEvent.MOUSE_PRESSED, event -> mapController.handleMousePress(event));

    }

    public double getScale() {
        return myScale.get();
    }

    /**
     * Set x/y scale
     * @param scale Scale
     */
    public void setScale( double scale) {
        myScale.set(scale);
    }

    /**
     * Set x/y pivot points
     * @param x X
     * @param y Y
     */
    public void setPivot( double x, double y) {
        setTranslateX(getTranslateX()-x);
        setTranslateY(getTranslateY()-y);
    }
}
