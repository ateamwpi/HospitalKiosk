package controllers.MapView.Map;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;

/**
 * The canvas which holds all of the nodes of the application.
 */
public class PannableCanvas extends Pane {

    private final DoubleProperty myScale = new SimpleDoubleProperty(1.0);

    PannableCanvas() {
        // add scale transform
        scaleXProperty().bind(myScale);
        scaleYProperty().bind(myScale);
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
        setTranslateX(getTranslateX()*scale);
        setTranslateY(getTranslateY()*scale);
    }

    /**
     * Set x/y pivot points
     * @param x X
     * @param y Y
     */
    void setPivot(double x, double y) {
        setTranslateX(getTranslateX()-x);
        setTranslateY(getTranslateY()-y);
    }
}
