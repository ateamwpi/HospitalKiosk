package controllers.MapView.Map;

import com.jfoenix.controls.JFXButton;
import controllers.AbstractController;
import controllers.IClickableController;
import core.ImageProxy;
import core.KioskMain;
import core.Utils;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import models.login.ILoginObserver;
import models.path.Node;
import models.path.Path;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by dylan on 4/2/17.
 */
public class MapController extends AbstractController implements IClickableController, ILoginObserver {

    private static final String[] USER_MAP_URLS = {
            "resources/floor1.png",
            "resources/floor2.png",
            "resources/floor3.png",
            "resources/floor4.png",
            "resources/floor5.png",
            "resources/floor6.png",
            "resources/floor7.png",
//            "resources/highResBelkin/Belkin_1_hi_res.png",
            "resources/highResBelkin/Belkin_2_hi_res.png",
            "resources/highResBelkin/Belkin_3_hi_res.png",
            "resources/highResBelkin/Belkin_4_hi_res.png"
    };

    private static final String[] PROF_MAP_URLS = {
            "resources/professionalMaps/floor1.png",
            "resources/professionalMaps/floor2.png",
            "resources/professionalMaps/floor3.png",
            "resources/professionalMaps/floor4.png",
            "resources/professionalMaps/floor5.png",
            "resources/professionalMaps/floor6.png",
            "resources/professionalMaps/floor7.png",
//            "resources/highResBelkin/Belkin_1_hi_res.png",
            "resources/highResBelkin/Belkin_2_hi_res.png",
            "resources/highResBelkin/Belkin_3_hi_res.png",
            "resources/highResBelkin/Belkin_4_hi_res.png"
    };

    private static ArrayList<String> allFloors;

    private ArrayList<ImageProxy> userMaps;
    private ArrayList<ImageProxy> profMaps;

    private ImageProxy map;
    private Group overlay;
    private int overlayIndex;

    @FXML
    private VBox floorVBox;

    public ArrayList<JFXButton> getFloorButtons() {
        return floorButtons;
    }

    private ArrayList<JFXButton> floorButtons;
    private JFXButton zoomIn;
    private JFXButton zoomOut;

    public int getFloor() {
        return floor;
    }

    private int floor;
    private PannableCanvas canvas;

    @FXML
    private AnchorPane root;
    @FXML
    private ImageView mapView;

    private SceneGestures sceneGestures;

    //// Public API ////

    public void handleMouseClick(MouseEvent event) {}

    @Override
    public String getURL() {
        return "resources/views/MapView/Map/Map.fxml";
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

    public void enableAllButtons() {
        enableButtons(getAllFloors());
    }

    public static ArrayList<String> getAllFloors() {
        if(allFloors == null) {
            allFloors = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "B2", "B3", "B4"));
        }
        return allFloors;
    }

    public void enableButtons(ArrayList<String> floors) {
        floorVBox.getChildren().clear();
        for(JFXButton b : this.floorButtons) {
            if(floors.contains(b.getText())) {
                floorVBox.getChildren().add(b);
            }
        }
        floorVBox.getChildren().add(zoomIn);
        floorVBox.getChildren().add(zoomOut);
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
        if(KioskMain.getLogin().getState().hasAccess())
            map = this.profMaps.get(this.floor-1);
        else
            map = this.userMaps.get(this.floor-1);
        mapView.setImage(map.getImage());
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
        canvas = new PannableCanvas();
        // add the canvas to overlay
        overlay.getChildren().add(canvas);

        floor = 1;
        this.userMaps = new ArrayList<>();
        this.profMaps = new ArrayList<>();

        // create the Proxies for all of the map images
        for(String url : USER_MAP_URLS) {
            this.userMaps.add(new ImageProxy(url));
        }
        for(String url : PROF_MAP_URLS) {
            this.profMaps.add(new ImageProxy(url));
        }

        // load the map into the map view

        if(KioskMain.getLogin().getState().hasAccess())
            map = this.profMaps.get(0);
        else
            map = this.userMaps.get(0);
        mapView.setImage(map.getImage());
        mapView.setPreserveRatio(true);
        // add the map to the canvas
        canvas.getChildren().add(mapView);
        // set base overlay index
        overlayIndex = overlay.getChildren().size();
        addFloorButtons();
        // create the scene gestures for zooming and panning
        sceneGestures = new SceneGestures(canvas, this);
        sceneGestures.zoomIn();
        // register handlers zooming and panning
        canvas.addEventHandler(MouseEvent.ANY, new ClickDragHandler(sceneGestures.getOnMouseClickedEventHandler(),
                sceneGestures.getOnMouseDraggedEventHandler(), sceneGestures.getOnMouseDraggedEventHandler()));
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        canvas.addEventHandler(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());

        KioskMain.getLogin().attachObserver(this);
    }

    private void drawConnection(Node nodeA, Node nodeB) {
        Line line = new Line(nodeA.getX(), nodeA.getY(), nodeB.getX(), nodeB.getY());
        addOverlay(0, line);
    }

    private void drawNode(Node node, Color c) {
        Circle circle = new Circle(node.getX(), node.getY(), 5, c);
        addOverlay(circle);
    }

    private void drawNode(Node node) {
        drawNode(node, Color.BLACK);
    }

    private void addFloorButtons() {
        int wid = 40;

        for(String s : getAllFloors()) {
            JFXButton floor = new JFXButton();
            floor.setText(s);
            floor.setOnAction(event -> setFloor(getAllFloors().indexOf(s) + 1));
            floor.setPrefWidth(wid);
            floor.getStylesheets().add(Utils.getResourceAsExternal("resources/styles/Main.css"));
            floor.getStyleClass().add("floor-button");
            floorVBox.getChildren().add(floor);
            floorButtons.add(floor);
        }

        this.zoomIn = new JFXButton();
        zoomIn.setText("+");
        zoomIn.setOnAction(event -> sceneGestures.zoomIn());
        zoomIn.setMinWidth(wid);
        zoomIn.getStylesheets().add(Utils.getResourceAsExternal("resources/styles/Main.css"));
        zoomIn.getStyleClass().add("floor-button");
        floorVBox.getChildren().add(zoomIn);
        floorVBox.toFront();

        this.zoomOut = new JFXButton();
        zoomOut.setText("-");
        zoomOut.setOnAction(event -> sceneGestures.zoomOut());
        zoomOut.setPrefWidth(wid);
        zoomOut.getStylesheets().add(Utils.getResourceAsExternal("resources/styles/Main.css"));
        zoomOut.getStyleClass().add("floor-button");
        floorVBox.getChildren().add(zoomOut);
    }

    public void hideButtons() {
        for(JFXButton b : floorButtons) {
            b.setVisible(false);
        }
        zoomIn.setVisible(false);
        zoomOut.setVisible(false);
    }

    public void showButtons() {
        for(JFXButton b : floorButtons) {
            b.setVisible(true);
        }
        zoomIn.setVisible(true);
        zoomOut.setVisible(true);
    }

    public SceneGestures getSceneGestures() {
        return this.sceneGestures;
    }

    @Override
    public void onAccountChanged() {
        this.setFloor(this.getFloor());
    }
}
