package controllers.map;

import controllers.IClickableController;
import controllers.IControllerWithParams;
import controllers.admin.ManageMapViewController;
import core.KioskMain;
import core.NodeInUseException;
import javafx.scene.control.Alert;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Pair;
import models.path.Node;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import models.path.Path;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dylan on 4/2/17.
 */
public class MapController implements IClickableController {

    private static final String MAP_URL = "resources/4_thefourthfloor.png";

    private Image map;
    private Group overlay;
    private int overlayIndex;
    private PannableCanvas canvas;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ImageView mapView;

    //// Public API ////

    public MapController() {
        // get loader
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/Map.fxml"));
        // set the controller
        loader.setController(this);
        // load the view
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void handleMouseClick(MouseEvent event) {}

    public Region getRoot() {
        return anchorPane;
    }

    public PannableCanvas getCanvas() {
        return canvas;
    }

    public void addOverlay(javafx.scene.Node node) {
        overlay.getChildren().add(overlayIndex, node);
    }

    public void removeOverlay(javafx.scene.Node node) {
        overlay.getChildren().remove(node);
    }

    public void drawPath(Path p) {
        drawNode(p.getStart());
        for (int i = 1; i < p.getPath().size(); i++) {
            drawConnection(p.getStep(i - 1), p.getStep(i));
        }
        drawNode(p.getEnd());
    }

    //// Private API ////

    @FXML
    private void initialize() {
        // create overlay
        overlay = new Group();
        anchorPane.getChildren().add(overlay);
        // create canvas
        canvas = new PannableCanvas(this);
        // add the canvas to overlay
        overlay.getChildren().add(canvas);
        // load the map into the map view
        map = new Image(getClass().getClassLoader().getResourceAsStream(MAP_URL));
        mapView.setImage(map);
        mapView.setPreserveRatio(true);
        // add the map to the canvas
        canvas.getChildren().add(mapView);
        // set base overlay index
        overlayIndex = overlay.getChildren().size();
        // create the scene gestures for zooming and panning
        SceneGestures sceneGestures = new SceneGestures(canvas, this);
        // register handlers zooming and panning
//        anchorPane.addEventHandler(MouseEvent.ANY, new ClickDragHandler(sceneGestures.getOnMouseClickedEventHandler(), sceneGestures.getOnMouseDraggedEventHandler()));
//        anchorPane.addEventHandler(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
//        anchorPane.addEventHandler(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
    }

    private void drawConnection(Node nodeA, Node nodeB) {
        Line line = new Line(nodeA.getX(), nodeA.getY(), nodeB.getX(), nodeB.getY());
        overlay.getChildren().add(line);
    }

    private void drawNode(Node node) {
        Circle circle = new Circle(node.getX(), node.getY(), 5);
        overlay.getChildren().add(circle);
    }
}
