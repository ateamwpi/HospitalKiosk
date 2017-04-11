package controllers.map;

import controllers.AbstractController;
import controllers.IClickableController;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import models.path.Node;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import models.path.Path;

import java.io.InputStream;

/**
 * Created by dylan on 4/2/17.
 */
public class MapController extends AbstractController implements IClickableController {

    private static final String MAP_URL = "4floor.png";

    private Group overlay;
    private int overlayIndex;
    private PannableCanvas canvas;

    @FXML
    private AnchorPane root;
    @FXML
    private ImageView mapView;

    //// Public API ////

    public void handleMouseClick(MouseEvent event) {}

    @Override
    public String getURL() {
        return "views/Map.fxml";
    }

    public PannableCanvas getCanvas() {
        return canvas;
    }

    public void addOverlay(int index, javafx.scene.Node node) {
        overlay.getChildren().add(overlayIndex + index, node);
    }

    public void addOverlay(javafx.scene.Node node) {
        overlay.getChildren().add(node);
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
        root.getChildren().add(overlay);
        // create canvas
        canvas = new PannableCanvas();
        // add the canvas to overlay
        overlay.getChildren().add(canvas);
        // load the map into the map view
        Image map = new Image(getResourceAsStream(MAP_URL));
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

    private static InputStream getResourceAsStream(String resource) {
        String stripped = resource.startsWith("/")?resource.substring(1):resource;
        InputStream stream = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader != null) {
            stream = classLoader.getResourceAsStream(stripped);
        }
        if (stream == null) {
            stream = MapController.class.getResourceAsStream(resource);
        }
        if (stream == null) {
            stream = MapController.class.getClassLoader().getResourceAsStream(stripped);
        }
        if (stream == null) {
            throw new RuntimeException("Resource not found: " + resource);
        }
        return stream;
    }

    private void drawConnection(Node nodeA, Node nodeB) {
        Line line = new Line(nodeA.getX(), nodeA.getY(), nodeB.getX(), nodeB.getY());
        addOverlay(0, line);
    }

    private void drawNode(Node node) {
        Circle circle = new Circle(node.getX(), node.getY(), 5);
        addOverlay(circle);
    }
}
