package controllers;

import core.KioskMain;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import java.io.IOException;

/**
 * Created by dylan on 4/2/17.
 */
public class MapController implements IControllerWithParams {


    private static final String MAP_URL = "resources/4_thefourthfloor.png";
    private static final int MIN_PIXELS = 10;
    private static final double ZOOM_SPEED = 1.005;


    @FXML
    private Canvas canvas;

    @FXML
    public static Canvas getMap(AnchorPane container) {
        try {
            FXMLLoader loader = new FXMLLoader(MapController.class.getClassLoader().getResource("views/Map.fxml"));
            Canvas canvas = loader.load();
            loader.<MapController>getController().initData(container);
            return canvas;
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    @FXML
    private void initialize() {
        System.out.println(canvas.getWidth());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(new Image(getClass().getClassLoader().getResourceAsStream(MAP_URL)), 0, 0);
//        gc.setFill(Color.BLUE);
//        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

//        Image map = new Image(getClass().getClassLoader().getResourceAsStream(MAP_URL));
//        double width = map.getWidth();
//        double height = map.getHeight();
//        ImageView mapView = new ImageView(map);
//
//        mapView.setPreserveRatio(true);
//        mapView.fitWidthProperty().bind(canvas.widthProperty());
//        mapView.fitHeightProperty().bind(canvas.heightProperty());
//
//        ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();
//
//        mapView.setOnMousePressed(e -> {
//
//            Point2D mousePress = imageViewToImage(mapView, new Point2D(e.getX(), e.getY()));
//            mouseDown.set(mousePress);
//        });
//
//        mapView.setOnMouseDragged(e -> {
//            Point2D dragPoint = imageViewToImage(mapView, new Point2D(e.getX(), e.getY()));
//            shift(mapView, dragPoint.subtract(mouseDown.get()));
//            mouseDown.set(imageViewToImage(mapView, new Point2D(e.getX(), e.getY())));
//        });
//
//        mapView.setOnScroll(e -> {
//            double delta = e.getDeltaY();
//            Rectangle2D viewport = mapView.getViewport();
//
//            double scale = clamp(Math.pow(ZOOM_SPEED, delta),  // altered the value from 1.01to zoom slower
//                    // don't scale so we're zoomed in to fewer than MIN_PIXELS in any direction:
//                    Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),
//                    // don't scale so that we're bigger than image dimensions:
//                    Math.max(width / viewport.getWidth(), height / viewport.getHeight())
//            );
//            if (scale != 1.0) {
//                Point2D mouse = imageViewToImage(mapView, new Point2D(e.getX(), e.getY()));
//
//                double newWidth = viewport.getWidth();
//                double newHeight = viewport.getHeight();
//                double mapViewRatio = (mapView.getFitWidth() / mapView.getFitHeight());
//                double viewportRatio = (newWidth / newHeight);
//                if (viewportRatio < mapViewRatio) {
//                    // adjust width to be proportional with height
//                    newHeight = newHeight * scale;
//                    newWidth = newHeight * mapViewRatio;
//                    if (newWidth > map.getWidth()) {
//                        newWidth = map.getWidth();
//                    }
//                } else {
//                    // adjust height to be proportional with width
//                    newWidth = newWidth * scale;
//                    newHeight = newWidth / mapViewRatio;
//                    if (newHeight > map.getHeight()) {
//                        newHeight = map.getHeight();
//                    }
//                }
//
//                // To keep the visual point under the mouse from moving, we need
//                // (x - newViewportMinX) / (x - currentViewportMinX) = scale
//                // where x is the mouse X coordinate in the image
//                // solving this for newViewportMinX gives
//                // newViewportMinX = x - (x - currentViewportMinX) * scale
//                // we then clamp this value so the image never scrolls out
//                // of the imageview:
//                double newMinX = 0;
//                if (newWidth < map.getWidth()) {
//                    newMinX = clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale,
//                            0, width - newWidth);
//                }
//                double newMinY = 0;
//                if (newHeight < map.getHeight()) {
//                    newMinY = clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale,
//                            0, height - newHeight);
//                }
//
//                mapView.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
//            }
//        });
//
//        mapView.setOnMouseClicked(e -> {
//            if (e.getClickCount() == 2) {
//                reset(mapView, width, height);
//            }
//        });
//
//        reset(mapView, width / 2, height / 2);
//    }

    // reset to the top left:
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

    @Override
    public void initData(Object... data) {
        Region container = (Region) data[0];
        canvas.widthProperty().bind(container.widthProperty());
        canvas.heightProperty().bind(container.heightProperty());
    }
}
