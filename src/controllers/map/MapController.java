package controllers.map;

import controllers.AbstractController;
import controllers.IClickableController;
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
public class MapController extends AbstractController implements IClickableController {

    private static final String[] MAP_URLS = {
            "resources/1floor.png",
            "resources/2floor.png",
            "resources/3floor.png",
            "resources/4_thefourthfloor.png",
            "resources/5floor.png",
            "resources/6floor.png",
            "resources/7floor.png"
    };

    private Image map;
    private Group overlay;
    private int overlayIndex;
    private int floor;
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

    public void setFloor(int floor){
        this.floor = floor;

        map = new Image(getClass().getClassLoader().getResourceAsStream(MAP_URLS[this.floor - 1]));
        mapView.setImage(map);
        mapView.setPreserveRatio(true);
    }

    //// Private API ////

    @FXML
    private void initialize() {
        // create overlay
        overlay = new Group();
        root.getChildren().add(overlay);
        // create canvas
        canvas = new PannableCanvas(this);
        // add the canvas to overlay
        overlay.getChildren().add(canvas);
        floor = 4;
        // load the map into the map view
        map = new Image(getClass().getClassLoader().getResourceAsStream(MAP_URLS[floor -1] ));
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
        addOverlay(0, line);
    }

    private void drawNode(Node node) {
        Circle circle = new Circle(node.getX(), node.getY(), 5);
        addOverlay(circle);
    }
}
