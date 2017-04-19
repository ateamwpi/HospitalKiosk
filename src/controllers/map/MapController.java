package controllers.map;

import com.jfoenix.controls.JFXButton;
import controllers.AbstractController;
import controllers.IClickableController;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import models.path.Node;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import models.path.Path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by dylan on 4/2/17.
 */
public class MapController extends AbstractController implements IClickableController {

    private static final String[] MAP_URLS = {
            "resources/floor1.png",
            "resources/floor2.png",
            "resources/floor3.png",
            "resources/floor4.png",
            "resources/floor5.png",
            "resources/floor6.png",
            "resources/floor7.png"
    };

    private Image map;
    private Group overlay;
    private int overlayIndex;

    @FXML
    private VBox floorVBox;

    public ArrayList<JFXButton> getFloorButtons() {
        return floorButtons;
    }

    private ArrayList<JFXButton> floorButtons;

    public int getFloor() {
        return floor;
    }

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

    public Group getOverlay() {
        return this.overlay;
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

    public void clearOverlay() {
        overlay.getChildren().clear();
        overlay.getChildren().add(canvas);
    }

    public void drawPath(Path p) {
        if(p.getStart().getFloor() == this.floor) drawNode(p.getStart(), Color.BLUE);
        for (int i = 1; i < p.getPath().size(); i++) {
            if(p.getStep(i-1).getFloor() == this.floor && p.getStep(i).getFloor() == this.floor)
                drawConnection(p.getStep(i - 1), p.getStep(i));
            else if(p.getStep(i-1).getFloor() == this.floor && p.getStep(i).getFloor() != this.floor)
                drawMidpoint(p.getStep(i-1));
            else if(p.getStep(i).getFloor() == this.floor && p.getStep(i-1).getFloor() != this.floor)
                drawMidpoint(p.getStep(i));
        }
        if(p.getEnd().getFloor() == this.floor) drawNode(p.getEnd(), Color.RED);
    }

    private void drawMidpoint(Node n) {
        Rectangle r = new Rectangle(n.getX()-5, n.getY()-5, 10, 10);
        r.setFill(Color.GREEN);
        addOverlay(r);
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
        floorButtons = new ArrayList<>();
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
        addFloorButtons();
        // create the scene gestures for zooming and panning
        SceneGestures sceneGestures = new SceneGestures(canvas, this);
        // register handlers zooming and panning
        canvas.addEventHandler(MouseEvent.ANY, new ClickDragHandler(sceneGestures.getOnMouseClickedEventHandler(), sceneGestures.getOnMouseDraggedEventHandler()));
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        canvas.addEventHandler(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
    }

    private void drawConnection(Node nodeA, Node nodeB) {
        Line line = new Line(nodeA.getX(), nodeA.getY(), nodeB.getX(), nodeB.getY());
        addOverlay(0, line);
    }

    public void drawNode(Node node, Color c) {
        Circle circle = new Circle(node.getX(), node.getY(), 5, c);
        addOverlay(circle);
    }

    public void drawNode(Node node) {
        drawNode(node, Color.BLACK);
    }

    private void replaceAllNodes(Collection<Node> nodes){
        overlay = new Group();
        root.getChildren().add(overlay);
        // add the canvas to overlay
        overlay.getChildren().add(canvas);
        mapView.setImage(map);
        mapView.setPreserveRatio(true);
        // set base overlay index
        overlayIndex = overlay.getChildren().size();
        for(Node n: nodes){
            drawNode(n);

        }
    }

    private void addFloorButtons() {
        ArrayList<String> floorList = new ArrayList<String>(Arrays.asList("1st Floor", "2nd Floor","3rd Floor", "4th Floor", "5th Floor", "6th Floor", "7th Floor"));

        for(String s : floorList) {
            JFXButton floor = new JFXButton();
            floor.setText(s);
            floor.setOnAction(event -> setFloor(floorList.indexOf(s) + 1));
            floor.setPrefWidth(115);
            floor.getStylesheets().add("@../../views/style.css");
            floor.getStyleClass().add("content-button");
            floorVBox.getChildren().add(floor);
            floorButtons.add(floor);
        }
        floorVBox.toFront();
    }
}
