package controllers.mapView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controllers.AbstractController;
import controllers.map.MapController;
import core.KioskMain;
import core.exception.FloorNotReachableException;
import core.exception.NearestNotFoundException;
import core.exception.PathNotFoundException;
import javafx.fxml.FXML;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.transform.Scale;
import models.dir.Location;
import models.path.Path;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by dylan on 4/16/17.
 */
public class DrawerController extends AbstractController {

    @FXML
    private JFXTextField start;
    @FXML
    private TextFlow fromFlow;
    @FXML
    private TextFlow toFlow;
    @FXML
    private JFXTextField end;
    @FXML
    private Label drawerClose;
    @FXML
    private Label startLabel;
    @FXML
    private Label endLabel;
    @FXML
    private VBox searchResults;
    @FXML
    private Label optionsMenuButton;
    @FXML
    private VBox directions;
    @FXML
    private VBox directionsContainer;
    @FXML
    private VBox searchContainer;
    @FXML
    private VBox toggleContainer;
    @FXML
    private Pane root;
    private Location startLocation;
    private Location endLocation;
    private Path path;
    private MapController mapController;

    public DrawerController(MapController mapController) {
        super(mapController);
    }

    @Override
    public void initData(Object... data) {
        mapController = (MapController) data[0];
    }

    @FXML
    private void initialize() {
        // listen to search input
        start.textProperty().addListener(observable -> handleKeyPressStart());
        end.textProperty().addListener(observable -> handleKeyPressEnd());
        // show search container
        showSearch();
    }

    @FXML
    private void printDirections() {
        String dirs = "\n\nBrigham and Women's Faulkner Hospital Directions\n";
        dirs += "From: " + path.getStart().getRoomName() + "\n";
        dirs += "To: " + path.getEnd().getRoomName() + "\n";
        dirs += path.textPath();
        Text text = new Text();
        text.setFont(new Font(14));
        text.setText(dirs);
        print(text);

    }

    private void print(Node first) {
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.NA_LETTER, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(null)) {
            boolean success = false;
            job.printPage(first);
            int oldFloor = mapController.getFloor();
            for (String s : path.getFloorsSpanning()) {
                mapController.clearOverlay();
                mapController.setFloor(Integer.parseInt(s.substring(0,1)));
                mapController.drawPath(path);
                mapController.hideButtons();
                double scaleX = pageLayout.getPrintableWidth() / mapController.getRoot().getBoundsInParent().getWidth();
                Scale scale = new Scale(scaleX, scaleX);
                mapController.getRoot().getTransforms().add(scale);
                success = job.printPage(pageLayout, mapController.getRoot());
                mapController.getRoot().getTransforms().remove(scale);
                mapController.showButtons();
            }
            mapController.clearOverlay();
            mapController.setFloor(oldFloor);
            mapController.drawPath(path);
            if (success) {
                job.endJob();
            }
        }
    }

    @FXML
    private void speakDirections() {
        KioskMain.getTTS().speak(path.textPath());
    }

    private void handleKeyPressStart() {
        String startQuery = start.getText();
        if (!startQuery.isEmpty()) {
            // search if not empty
            search(startQuery, this::setStart);
        } else {
            clearSearchResults();
        }
    }

    private void handleKeyPressEnd() {
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
        Collection<Location> POIs = KioskMain.getDir().getPOI();
        searchResults.getChildren().clear();
        for (Location location : POIs) {
            searchResults.getChildren().add(new SearchResult(location, this::setEnd).getRoot());
        }
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
        List<Location> locations = KioskMain.getDir().search(query);
        searchResults.getChildren().clear();
        for (Location location : locations) {
            searchResults.getChildren().add(new SearchResult(location, handler).getRoot());
        }
    }

    private void findPath() {
        if (startLocation != null && endLocation != null) {
            try {
                // attempt to find the path
                path = KioskMain.getPath().findPath(startLocation.getNode(), endLocation.getNode());
                // draw the path on the map
                //drawPath.accept(path);
                // show the directions
                showDirections(path.getDirections());
                mapController.setFloor(path.getStart().getFloor());
                // bind actions
                for(JFXButton b: mapController.getFloorButtons()) {
                    b.setOnAction(event -> {
                        mapController.clearOverlay();
                        mapController.setFloor(Integer.parseInt(b.getText()));
                        mapController.drawPath(path);
                    });
                }
                mapController.drawPath(path);
                mapController.enableButtons(path.getFloorsSpanning());

            } catch (PathNotFoundException | NearestNotFoundException | FloorNotReachableException e) {
                e.printStackTrace();
            }
        }
    }

    private void showDirections(Collection<DirectionStep> directionSteps) {
        // set directions header
        setDirectionsHeader();
        // set start and end locations
        startLabel.setText(startLocation.getName());
        endLabel.setText(endLocation.getName());
        // set directions
        clearDirections();
        for (DirectionStep directionStep : directionSteps) {
            directions.getChildren().add(directionStep.getRoot());
        }
        // render
        root.getChildren().clear();
        root.getChildren().addAll(toggleContainer, directionsContainer);
    }

    private void setDirectionsHeader() {
        Text from = new Text("from ");
        from.setFill(Color.web("#97bcf9"));
        Text fromLocation = new Text(startLocation.getName());
        fromLocation.setFill(Color.web("#e7effe"));
        fromFlow.getChildren().clear();
        fromFlow.getChildren().addAll(from, fromLocation);
        Text to = new Text("to ");
        to.setFill(Color.web("#97bcf9"));
        Text toLocation = new Text(endLocation.getName());
        toLocation.setFill(Color.web("#e7effe"));
        toFlow.getChildren().clear();
        toFlow.getChildren().addAll(to, toLocation);
    }

    @FXML
    private void showSearch() {
        setEnd(null);
        setStart(KioskMain.getDir().getTheKiosk());
        mapController.enableAllButtons();
        mapController.clearOverlay();
        start.requestFocus();
        end.requestFocus();
        // render
        root.getChildren().clear();
        root.getChildren().addAll(toggleContainer, searchContainer);
    }

    private void clearDirections() {
        directions.getChildren().clear();
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
