package controllers.mapView;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import controllers.AbstractController;
import core.KioskMain;
import core.exception.FloorNotReachableException;
import core.exception.NearestNotFoundException;
import core.exception.PathNotFoundException;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import models.dir.Location;
import models.path.Direction;
import models.path.Path;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by dylan on 4/16/17.
 */
public class DrawerController extends AbstractController {

    @FXML
    private JFXTextField start;
    @FXML
    private JFXTextField end;
    @FXML
    private Label drawerClose;
    @FXML
    private VBox searchResults;
    @FXML
    private Label optionsMenuButton;
    @FXML
    private VBox directions;
    private Location startLocation;
    private Location endLocation;
    private Consumer<Path> drawPath;

    public DrawerController(Consumer<Path> drawPath) {
        super(drawPath);
    }

    @Override
    public void initData(Object... data) {
        drawPath = (Consumer<Path>) data[0];
    }

    @FXML
    private void initialize() {

    }

    @FXML
    private void handleKeyPressStart(KeyEvent event) {
        String startQuery = start.getText();
        if (!startQuery.isEmpty()) {
            // search if not empty
            search(startQuery, this::setStart);
        } else {
            clearSearchResults();
        }
    }

    @FXML
    private void handleKeyPressEnd(KeyEvent event) {
        String endQuery = end.getText();
        if (!endQuery.isEmpty()) {
            // search if not empty
            search(endQuery, this::setEnd);
        } else {
            clearSearchResults();
        }
    }

    private void clearSearchResults() {
        searchResults.getChildren().clear();
    }

    private void setStart(Location location) {
        startLocation = location;
        // fill in field
        start.setText(location != null ? location.getName() : "");
        // focus on other field
        if (endLocation == null) {
            end.requestFocus();
        }
        // clear results
        clearSearchResults();
        // try to find path
        findPath();
    }

    private void setEnd(Location location) {
        endLocation = location;
        // fill in field
        end.setText(location != null ? location.getName() : "");
        // focus on other field
        if (startLocation == null) {
            start.requestFocus();
        }
        // clear results
        clearSearchResults();
        // try to find path
        findPath();
    }

    private void search(String query, Consumer<Location> handler) {
        List<Location> locations = KioskMain.getDir().search(query, 10);
        searchResults.getChildren().clear();
        for (Location location : locations) {
            searchResults.getChildren().add(new SearchResult(location, handler).getRoot());
        }
    }

    private void findPath() {
        if (startLocation != null && endLocation != null) {
            try {
                // attempt to find the path
                Path path = KioskMain.getPath().findPath(startLocation.getNode(), endLocation.getNode());
                // draw the path on the map
                drawPath.accept(path);
                // show the directions
                showDirections(path.getDirections());
            } catch (PathNotFoundException | NearestNotFoundException | FloorNotReachableException e) {
                e.printStackTrace();
            }
        }
    }

    private void showDirections(Collection<DirectionStep> directionSteps) {

    }

    public Label getDrawerClose() {
        return drawerClose;
    }

    public Label getOptionsMenuButton() {
        return optionsMenuButton;
    }

    @Override
    public String getURL() {
        return "views/Drawer.fxml";
    }
}
