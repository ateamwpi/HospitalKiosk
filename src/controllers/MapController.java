package controllers;

import core.KioskMain;
import javafx.scene.shape.Line;
import models.path.Node;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
//import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.Collection;
import java.util.Observable;

/**
 * Created by dylan on 4/2/17.
 */
public class MapController implements IControllerWithParams {

    public static final String EDIT_NODE_VIEW_URL = "views/EditNodeView.fxml";

    private static final String MAP_URL = "resources/4_thefourthfloor.png";
    private static final int MIN_PIXELS = 10;
    private static final double ZOOM_SPEED = 1.005;
    private static final double SELECTED_NODE_RADIUS = 10.0;

    private Image map;
    private ObjectProperty<Point2D> mouseDown;
    private NodeGestures nodeGestures;
    private DraggableNode selectedNode;
    private Group overlay;
    private Collection<Node> nodes;
    private Boolean admin = false;
    private ManageMapViewController manageMapViewController;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ImageView mapView;

    //// Public API ////

    public MapController() {
        // get nodes from db
        nodes = KioskMain.getPath().getGraph().values();
    }

    @FXML
    public static AnchorPane getMap(Region container) {
        try {
            FXMLLoader loader = new FXMLLoader(MapController.class.getClassLoader().getResource("views/Map.fxml"));
            AnchorPane root = loader.load();
            loader.<MapController>getController().initData(container);
            return root;
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    @Override
    public void initData(Object... data) {
        manageMapViewController = (ManageMapViewController) data[0];
    }

    public void addNode(double x, double y, String room) {
        System.out.println("add node");
        // create new node
        Node node = new Node((int) x, (int) y, room);
        // draw node with gestures
        DraggableNode draggableNode = drawNode(node);
        // select the node
        selectNode(draggableNode);
        // create node in db
        //KioskMain.getDB().addNode(node);
    }

    public void deleteSelectedNode() {
        if (selectedNode != null) {
            System.out.println("delete node");
            // remove the visual node from the overlay
            overlay.getChildren().remove(selectedNode);
            // unselect node
            unselectNode();
            // delete the node from the db
            //KioskMain.getDB().removeNode(selectedNode.getNode());
        }
    }

    public void selectNode(DraggableNode node) {
        unselectNode();
        selectedNode = node;
        selectedNode.select();
        manageMapViewController.selectNode(selectedNode);
    }

    public void handleMousePress(MouseEvent e) {
        if (!admin)
            return;
        else if (nodeIsSelected()) {
            unselectNode();
        } else {
            // convert the mouse coordinates to map coordinates
            Point2D p = new Point2D(e.getX(), e.getY());//imageViewToImage(mapView, new Point2D(e.getX(), e.getY()));
            addNode(p.getX(), p.getY(), "NONE");
        }
    }

    public Boolean nodeIsSelected() {
        return selectedNode != null;
    }

    // returns true if some node is selected
    // returns false if no node is selected
    public void unselectNode() {
        if (selectedNode != null) {
            selectedNode.unselect();
            selectedNode = null;
            manageMapViewController.unselectNode();
        }
    }

    public void adminMode() {
        admin = true;
        // draw the nodes
        for (Node node : nodes) {
            drawNode(node);
            // draw the node connections
            drawNodeConnections(node);
        }
    }

    private void drawNodeConnections(Node node) {
        for (Node other : node.getConnections()) {
            // don't draw connection twice
            if(node.getID() < other.getID()) {
                // draw connection
                drawConnection(node, other);
            }
        }
    }

    private void drawConnection(Node nodeA, Node nodeB) {
        Line line = new Line(nodeA.getX(), nodeA.getY(), nodeB.getX(), nodeB.getY());
        this.overlay.getChildren().add(line);
    }

    //// Private API ////

    @FXML
    private void initialize() {
        // create overlay
        overlay = new Group();
        anchorPane.getChildren().add(overlay);
        // create canvas
        PannableCanvas canvas = new PannableCanvas(this);
        // add the canvas to overlay
        overlay.getChildren().add(canvas);
        // create the node gesture for dragging
        nodeGestures = new NodeGestures(canvas, this);
        // create the scene gestures for zooming and panning
        SceneGestures sceneGestures = new SceneGestures(canvas,this);

        // load the map into the map view
        map = new Image(getClass().getClassLoader().getResourceAsStream(MAP_URL));
        mapView.setImage(map);
        mapView.setPreserveRatio(true);
        // add the map to the canvas
        canvas.getChildren().addAll(mapView);

        // TODO uncomment for zoom/pan
        // register handlers zooming and panning
//        anchorPane.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
//        anchorPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
//        anchorPane.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());

        // test nodes
//        addNode(100, 300, "NONE");
    }

    private DraggableNode drawNode(Node node) {
        // create new visual node with gestures
        DraggableNode draggableNode = new DraggableNode(node, nodeGestures);
        // add visual node to overlay
        overlay.getChildren().add(draggableNode);
        // return the visual node
        return draggableNode;
    }

    // reset the map
    private void click(MouseEvent e) {
        double width = map.getWidth();
        double height = map.getHeight();
        if (e.getClickCount() == 2) {
            reset(mapView, width, height);
        }
    }

    // handle mouse press
    private void press(MouseEvent e) {
        Point2D mousePress = imageViewToImage(mapView, new Point2D(e.getX(), e.getY()));
        mouseDown.set(mousePress);
        System.out.println(mousePress);
    }

    // pan the map and overlay
    private void pan(MouseEvent e) {
        // TODO pan overlay

        //// OVERLAY ////



        //// MAP ////

        Point2D dragPoint = imageViewToImage(mapView, new Point2D(e.getX(), e.getY()));
        shift(mapView, dragPoint.subtract(mouseDown.get()));
        mouseDown.set(imageViewToImage(mapView, new Point2D(e.getX(), e.getY())));
    }

    // zoom the map and the overlay
    private void zoom(ScrollEvent e) {
        // TODO zoom overlay

        double width = map.getWidth();
        double height = map.getHeight();
        double delta = e.getDeltaY();
        Rectangle2D viewport = mapView.getViewport();

        double scale = clamp(Math.pow(ZOOM_SPEED, delta),  // altered the value from 1.01to zoom slower
                // don't scale so we're zoomed in to fewer than MIN_PIXELS in any direction:
                Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),
                // don't scale so that we're bigger than image dimensions:
                Math.max(width / viewport.getWidth(), height / viewport.getHeight())
        );
        if (scale != 1.0) {
            Point2D mouse = imageViewToImage(mapView, new Point2D(e.getX(), e.getY()));

            double newWidth = viewport.getWidth();
            double newHeight = viewport.getHeight();
            double mapViewRatio = (mapView.getFitWidth() / mapView.getFitHeight());
            double viewportRatio = (newWidth / newHeight);
            if (viewportRatio < mapViewRatio) {
                // adjust width to be proportional with height
                newHeight = newHeight * scale;
                newWidth = newHeight * mapViewRatio;
                if (newWidth > map.getWidth()) {
                    newWidth = map.getWidth();
                }
            } else {
                // adjust height to be proportional with width
                newWidth = newWidth * scale;
                newHeight = newWidth / mapViewRatio;
                if (newHeight > map.getHeight()) {
                    newHeight = map.getHeight();
                }
            }

            // To keep the visual point under the mouse from moving, we need
            // (x - newViewportMinX) / (x - currentViewportMinX) = scale
            // where x is the mouse X coordinate in the image
            // solving this for newViewportMinX gives
            // newViewportMinX = x - (x - currentViewportMinX) * scale
            // we then clamp this value so the image never scrolls out
            // of the imageview:
            double newMinX = 0;
            if (newWidth < map.getWidth()) {
                newMinX = clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale,
                        0, width - newWidth);
            }
            double newMinY = 0;
            if (newHeight < map.getHeight()) {
                newMinY = clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale,
                        0, height - newHeight);
            }

            mapView.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));

        }
    }

    // reset the map
    private void reset(ImageView imageView, double width, double height) {
        imageView.setViewport(new Rectangle2D(0, 0, width, height));
    }

    // shift the viewport of the imageView by the specified delta, clamping so
    // the viewport does not move off the actual image:
    private void shift(ImageView imageView, Point2D delta) {
        Rectangle2D viewport = imageView.getViewport();

        double width = imageView.getImage().getWidth() ;
        double height = imageView.getImage().getHeight() ;

        double maxX = width - viewport.getWidth();
        double maxY = height - viewport.getHeight();

        double minX = clamp(viewport.getMinX() - delta.getX(), 0, maxX);
        double minY = clamp(viewport.getMinY() - delta.getY(), 0, maxY);

        imageView.setViewport(new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));
    }

    private double clamp(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    // convert mouse coordinates in the imageView to coordinates in the actual image:
    private Point2D imageViewToImage(ImageView imageView, Point2D imageViewCoordinates) {
        double xProportion = imageViewCoordinates.getX() / imageView.getBoundsInLocal().getWidth();
        double yProportion = imageViewCoordinates.getY() / imageView.getBoundsInLocal().getHeight();

        Rectangle2D viewport = imageView.getViewport();
        return new Point2D(
                viewport.getMinX() + xProportion * viewport.getWidth(),
                viewport.getMinY() + yProportion * viewport.getHeight());
    }
}


class DraggableNode extends Circle {

    private static final double UNSELECTED_RADIUS = 3;
    private static final double SELECTED_RADIUS = 5;
    private static final Color UNSELECTED_COLOR = Color.BLACK;
    private static final Color SELECTED_COLOR = Color.RED;

    private Node node;
    private int previewX;
    private int previewY;
    private Collection<Node> previewConnections;
    private String previewRoomName;

    public DraggableNode(Node node, NodeGestures nodeGestures) {
        super(node.getX(), node.getY(), UNSELECTED_RADIUS, UNSELECTED_COLOR);
        this.node = node;
        // handlers for mouse click and drag
        addEventFilter(MouseEvent.ANY, new ClickDragHandler(nodeGestures.getOnMouseDraggedEventHandler(), nodeGestures.getOnMousePressedEventHandler()));
    }

    public void previewX(int x) {
        previewX = x;
    }

    public void previewY(int y) {
        previewY = y;
    }

    public void previewRoomName(String roomName) {
        previewRoomName = roomName;
    }

    public void previewConnections(Collection<Node> nodes) {
        previewConnections = nodes;
    }

    public void save() {
        Node node = getNode();
        // update the node
        node.setX(previewX);
        node.setY(previewY);
        node.setRoomName(previewRoomName);
        node.setConnections(previewConnections);
        // draw the node
        // TODO fix relocate bug
        relocate(node.getX(), node.getY());
//        translateXProperty().setValue(node.getX());
//        translateYProperty().setValue(node.getY());
    }

    public Node getNode() {
        return node;
    }

    public void unselect() {
        System.out.println("node unselected");
        setFill(UNSELECTED_COLOR);
        setRadius(UNSELECTED_RADIUS);
    }

    public void select() {
        System.out.println("node selected");
        setFill(SELECTED_COLOR);
        setRadius(SELECTED_RADIUS);
        System.out.println(node);
    }

}





//// Zoom, Pan, Click Gestures for Node and Canvas ////

/**
 * The canvas which holds all of the nodes of the application.
 */
class PannableCanvas extends Pane {

    private DoubleProperty myScale = new SimpleDoubleProperty(1.0);
    private MapController mapController;

    public PannableCanvas(MapController mapController) {
        this.mapController = mapController;

//        setPrefSize(600, 400);
//        setStyle("-fx-background-color: lightgrey; -fx-border-color: blue;");

        // add scale transform
        scaleXProperty().bind(myScale);
        scaleYProperty().bind(myScale);

        // logging
        addEventFilter(MouseEvent.MOUSE_PRESSED, event -> handleMousePress(event));

    }

    private void handleMousePress(MouseEvent event) {
//        System.out.println(
//                "canvas event: " + ( ((event.getSceneX() - getBoundsInParent().getMinX()) / getScale()) + ", scale: " + getScale())
//        );
//        System.out.println( "canvas bounds: " + getBoundsInParent());
        // unselect the current node
        mapController.handleMousePress(event);
    }

    /**
     * Add a grid to the canvas, send it to back
     */
    public void addGrid() {

        double w = getBoundsInLocal().getWidth();
        double h = getBoundsInLocal().getHeight();

        // add grid
        Canvas grid = new Canvas(w, h);

        // don't catch mouse events
        grid.setMouseTransparent(true);

        GraphicsContext gc = grid.getGraphicsContext2D();

        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);

        // draw grid lines
        double offset = 50;
        for( double i=offset; i < w; i+=offset) {
            // vertical
            gc.strokeLine( i, 0, i, h);
            // horizontal
            gc.strokeLine( 0, i, w, i);
        }

        getChildren().add( grid);

        grid.toBack();
    }

    public double getScale() {
        return myScale.get();
    }

    /**
     * Set x/y scale
     * @param scale
     */
    public void setScale( double scale) {
        System.out.println(myScale);
        System.out.println(scale);
        myScale.set(scale);
    }

    /**
     * Set x/y pivot points
     * @param x
     * @param y
     */
    public void setPivot( double x, double y) {
        setTranslateX(getTranslateX()-x);
        setTranslateY(getTranslateY()-y);
    }
}

/**
 * Mouse drag context used for scene and nodes.
 */
class DragContext {

    double mouseAnchorX;
    double mouseAnchorY;

    double translateAnchorX;
    double translateAnchorY;

}

/**
 * Listeners for making the nodes draggable via left mouse button. Considers if parent is zoomed.
 */
class NodeGestures {

    private DragContext nodeDragContext = new DragContext();

    PannableCanvas canvas;
    MapController mapController;

    public NodeGestures(PannableCanvas canvas, MapController mapController) {
        this.canvas = canvas;
        this.mapController = mapController;
    }

    public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }

    private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            // right mouse button => panning
            if( !event.isPrimaryButtonDown())
                return;

            System.out.println("node clicked");
            // get the node clicked on
            DraggableNode node = (DraggableNode) event.getSource();

            // show node selector
            if (mapController.nodeIsSelected())
                mapController.unselectNode();
            else
                mapController.selectNode(node);

            // update node drag context
            nodeDragContext.mouseAnchorX = event.getSceneX();
            nodeDragContext.mouseAnchorY = event.getSceneY();
            nodeDragContext.translateAnchorX = node.getTranslateX();
            nodeDragContext.translateAnchorY = node.getTranslateY();
        }
    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            // right mouse button => panning
            if( !event.isPrimaryButtonDown())
                return;

            System.out.println("node dragged");
            double scale = canvas.getScale();

            DraggableNode node = (DraggableNode) event.getSource();

            System.out.println("node dragged");
            // TODO drag bug
//            node.setTranslateX(nodeDragContext.translateAnchorX + (( event.getSceneX() - nodeDragContext.mouseAnchorX) / scale));
//            node.setTranslateY(nodeDragContext.translateAnchorY + (( event.getSceneY() - nodeDragContext.mouseAnchorY) / scale));

            event.consume();
        }
    };
}

class ClickDragHandler implements EventHandler<MouseEvent> {

    private final EventHandler<MouseEvent> onDraggedEventHandler;

    private final EventHandler<MouseEvent> onClickedEventHandler;

    private boolean dragging = false;

    public ClickDragHandler(EventHandler<MouseEvent> onDraggedEventHandler, EventHandler<MouseEvent> onClickedEventHandler) {
        this.onDraggedEventHandler = onDraggedEventHandler;
        this.onClickedEventHandler = onClickedEventHandler;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            dragging = false;
        }
        else if (event.getEventType() == MouseEvent.DRAG_DETECTED) {
            dragging = true;
        }
        else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            //maybe filter on dragging (== true)
            onDraggedEventHandler.handle(event);
        }
        else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            if (!dragging) {
                onClickedEventHandler.handle(event);
            }
        }

    }
}

/**
 * Listeners for making the scene's canvas draggable and zoomable
 */
class SceneGestures {

    private static final double MAX_SCALE = 10.0d;
    private static final double MIN_SCALE = .1d;

    private DragContext sceneDragContext = new DragContext();

    PannableCanvas canvas;
    MapController mapController;

    public SceneGestures(PannableCanvas canvas, MapController mapController) {
        this.canvas = canvas;
        this.mapController = mapController;
    }

    public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
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
            if( !event.isSecondaryButtonDown())
                return;

            // unselect the current node
            mapController.handleMousePress(event);

            // update scene drag context
            sceneDragContext.mouseAnchorX = event.getSceneX();
            sceneDragContext.mouseAnchorY = event.getSceneY();
            sceneDragContext.translateAnchorX = canvas.getTranslateX();
            sceneDragContext.translateAnchorY = canvas.getTranslateY();

        }

    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {

            // right mouse button => panning
            if( !event.isSecondaryButtonDown())
                return;

            canvas.setTranslateX(sceneDragContext.translateAnchorX + event.getSceneX() - sceneDragContext.mouseAnchorX);
            canvas.setTranslateY(sceneDragContext.translateAnchorY + event.getSceneY() - sceneDragContext.mouseAnchorY);

            event.consume();
        }
    };

    /**
     * Mouse wheel handler: zoom to pivot point
     */
    private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {

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


    public static double clamp( double value, double min, double max) {

        if( Double.compare(value, min) < 0)
            return min;

        if( Double.compare(value, max) > 0)
            return max;

        return value;
    }
}
