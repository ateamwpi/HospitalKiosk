package controllers;

import core.KioskMain;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;

public class ManageMapViewController {

    private static final String MAP_URL = "resources/4_thefourthfloor.png";
    private static final int MIN_PIXELS = 10;

    @FXML
    private Button backBtn;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private void initialize() {
        Image map = new Image(getClass().getClassLoader().getResourceAsStream(MAP_URL));
        double width = map.getWidth();
        double height = map.getHeight();
        ImageView mapView = new ImageView(map);

        mapView.setPreserveRatio(true);
        mapView.fitWidthProperty().bind(anchorPane.widthProperty());
        mapView.fitHeightProperty().bind(anchorPane.heightProperty());

        ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();

        mapView.setOnMousePressed(e -> {

            Point2D mousePress = imageViewToImage(mapView, new Point2D(e.getX(), e.getY()));
            mouseDown.set(mousePress);
        });

        mapView.setOnMouseDragged(e -> {
            Point2D dragPoint = imageViewToImage(mapView, new Point2D(e.getX(), e.getY()));
            shift(mapView, dragPoint.subtract(mouseDown.get()));
            mouseDown.set(imageViewToImage(mapView, new Point2D(e.getX(), e.getY())));
        });

        mapView.setOnScroll(e -> {
            double delta = e.getDeltaY();
            Rectangle2D viewport = mapView.getViewport();

            double scale = clamp(Math.pow(1.01, delta),

                    // don't scale so we're zoomed in to fewer than MIN_PIXELS in any direction:
                    Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),

                    // don't scale so that we're bigger than image dimensions:
                    Math.max(width / viewport.getWidth(), height / viewport.getHeight())

            );

            Point2D mouse = imageViewToImage(mapView, new Point2D(e.getX(), e.getY()));

            double newWidth = viewport.getWidth() * scale;
            double newHeight = viewport.getHeight() * scale;

            // To keep the visual point under the mouse from moving, we need
            // (x - newViewportMinX) / (x - currentViewportMinX) = scale
            // where x is the mouse X coordinate in the image

            // solving this for newViewportMinX gives

            // newViewportMinX = x - (x - currentViewportMinX) * scale

            // we then clamp this value so the image never scrolls out
            // of the imageview:

            double newMinX = clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale,
                    0, width - newWidth);
            double newMinY = clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale,
                    0, height - newHeight);

            mapView.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
        });

        mapView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                reset(mapView, width, height);
            }
        });

        reset(mapView, width / 2, height / 2);

//        anchorPane.setFillWidth(true);
//        VBox.setVgrow(anchorPane, Priority.ALWAYS);

        scrollPane.setContent(mapView);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    @FXML
    private void clickBack(ActionEvent event) throws IOException {
        KioskMain.setScene("views/AdminMenu.fxml");
    }



    public void start2() {


//        imageView.setOnScroll(e -> {
//            double delta = e.getDeltaY();
//            Rectangle2D viewport = imageView.getViewport();
//
//            double scale = clamp(Math.pow(1.005, delta),  // altered the value from 1.01to zoom slower
//                    // don't scale so we're zoomed in to fewer than MIN_PIXELS in any direction:
//                    Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),
//                    // don't scale so that we're bigger than image dimensions:
//                    Math.max(width / viewport.getWidth(), height / viewport.getHeight())
//            );
//            if (scale != 1.0) {
//                Point2D mouse = imageViewToImage(imageView, new Point2D(e.getX(), e.getY()));
//
//                double newWidth = viewport.getWidth();
//                double newHeight = viewport.getHeight();
//                double imageViewRatio = (imageView.getFitWidth() / imageView.getFitHeight());
//                double viewportRatio = (newWidth / newHeight);
//                if (viewportRatio < imageViewRatio) {
//                    // adjust width to be proportional with height
//                    newHeight = newHeight * scale;
//                    newWidth = newHeight * imageViewRatio;
//                    if (newWidth > image.getWidth()) {
//                        newWidth = image.getWidth();
//                    }
//                } else {
//                    // adjust height to be proportional with width
//                    newWidth = newWidth * scale;
//                    newHeight = newWidth / imageViewRatio;
//                    if (newHeight > image.getHeight()) {
//                        newHeight = image.getHeight();
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
//                if (newWidth < image.getWidth()) {
//                    newMinX = clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale,
//                            0, width - newWidth);
//                }
//                double newMinY = 0;
//                if (newHeight < image.getHeight()) {
//                    newMinY = clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale,
//                            0, height - newHeight);
//                }
//
//                imageView.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
//            }
//        });


    }

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



}
