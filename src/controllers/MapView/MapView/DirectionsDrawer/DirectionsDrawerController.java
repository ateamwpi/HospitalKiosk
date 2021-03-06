package controllers.MapView.MapView.DirectionsDrawer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controllers.AbstractController;
import controllers.DirectoryView.DirectoryView.DirectoryViewController;
import controllers.MapView.Map.MapController;
import core.KioskMain;
import core.Utils;
import core.exception.FloorNotReachableException;
import core.exception.NearestNotFoundException;
import core.exception.PathNotFoundException;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.transform.Scale;
import models.dir.Location;
import models.path.Path;
import models.qr.QRManager;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by dylan on 4/16/17.
 */
public class DirectionsDrawerController extends AbstractController {


    @FXML
    private Label directionsBackButton;
    @FXML
    private Label printDirectionsIcon;
    @FXML
    private Label speakDirectionsIcon;
    @FXML
    private Label qrCodeIcon;
    @FXML
    private JFXTextField start;
    @FXML
    private TextFlow fromFlow;
    @FXML
    private TextFlow toFlow;
    @FXML
    private Label timeLabel;
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
    private VBox optionsMenuButton;
    @FXML
    private VBox directions;
    @FXML
    private VBox directionsContainer;
    @FXML
    private VBox searchContainer;
    @FXML
    private VBox toggleContainer;
    @FXML
    private Label startDirectory;
    @FXML
    private Label endDirectory;
    @FXML
    private Pane root;
    @FXML
    private ScrollPane scroll;

    private Location startLocation;
    private Location endLocation;
    private Path path;
    private MapController mapController;
    private Parent mainRoot;

    public DirectionsDrawerController(MapController mapController, Parent mainRoot) {
        super(mapController, mainRoot);
    }

    @Override
    public void initData(Object... data) {
        mapController = (MapController) data[0];
        mainRoot = (Parent) data[1];
    }

    @FXML
    private void initialize() {
        // bind event handlers
        directionsBackButton.setOnMouseClicked(this::showSearch);
        printDirectionsIcon.setOnMouseClicked(this::printDirections);
        speakDirectionsIcon.setOnMouseClicked(this::speakDirections);
        qrCodeIcon.setOnMouseClicked(this::qrDirections);
        start.setOnKeyReleased(this::typeStart);
        start.focusedProperty().addListener(this::changeStartFocus);
        end.setOnKeyReleased(this::typeEnd);
        end.focusedProperty().addListener(this::changeEndFocus);
        startDirectory.setOnMouseClicked(this::selectStartFromDirectory);
        endDirectory.setOnMouseClicked(this::selectEndFromDirectory);
        scroll.prefViewportHeightProperty().bind(root.heightProperty().subtract(300));
        // show search container
        showSearch(null);
    }

    private void changeStartFocus(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (newValue) {
            clearSearchResults();
            typeStart(null);
        }
    }

    private void changeEndFocus(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (newValue) {
            clearSearchResults();
            typeEnd(null);
        }
    }

    private void typeStart(KeyEvent e) {
        String startQuery = start.getText();
        startLocation = null;
        if (!startQuery.isEmpty()) {
            // search if not empty
            search(startQuery, this::selectStart);
        } else {
            showPOI(this::selectStart);
        }
    }

    private void typeEnd(KeyEvent e) {
        String endQuery = end.getText();
        endLocation = null;
        if (!endQuery.isEmpty()) {
            // search if not empty
            search(endQuery, this::selectEnd);
        } else {
            showPOI(this::selectEnd);
        }
    }

    private void selectStartFromDirectory(MouseEvent event) {
        selectLocationFromDirectory(this::selectStart);
    }

    private void selectEndFromDirectory(MouseEvent event) {
        selectLocationFromDirectory(this::selectEnd);
    }

    private void selectLocationFromDirectory(Consumer<Location> selectLocation) {
        new DirectoryViewController(mainRoot, selectLocation, true).showCentered();
    }

    private void clearSearchResults() {
        searchResults.getChildren().clear();
    }

    private void showPOI(Consumer<Location> selectLocation) {
        clearSearchResults();
        Collection<Location> POIs = KioskMain.getDir().getPOI();
        for (Location location : POIs) {
            searchResults.getChildren().add(new SearchResult(location, selectLocation).getRoot());
        }
    }

    private void selectStart(Location location) {
        // set the location
        setStart(location);
        // try to find path
        findPath();
    }

    private void selectEnd(Location location) {
        // set the location
        setEnd(location);
        // try to find path
        findPath();
    }

    private void setEnd(Location location) {
        // set the location
        endLocation = location;
        // fill in field
        end.setText(location != null ? location.getName() : "");
        // focus on other field
        Platform.runLater(() -> {
            if (startLocation == null) {
                start.requestFocus();
            }
        });
    }

    private void setStart(Location location) {
        // set the location
        startLocation = location;
        // fill in field
        start.setText(location != null ? location.getName() : "");
        // focus on other field
        Platform.runLater(() -> {
            if (endLocation == null) {
                end.requestFocus();
            }
        });
    }

    private void search(String query, Consumer<Location> selectLocation) {
        clearSearchResults();
        List<Location> locations = KioskMain.getDir().search(query);
        for (Location location : locations) {
            searchResults.getChildren().add(new SearchResult(location, selectLocation).getRoot());
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
                for (JFXButton b : mapController.getFloorButtons()) {
                    b.setOnAction(event -> {
                        mapController.clearOverlay();
                        int floor = mapController.getAllFloors().indexOf(b.getText()) + 1;
                        mapController.setFloor(floor);
                        mapController.drawPath(path);
                    });
                }
                mapController.drawPath(path);
                mapController.enableButtons(path.getFloorsSpanning());

            } catch (PathNotFoundException e) {
                // Path not found
                // should only happen if an admin adds a dead end/unconnected node
                String body = "There is no known way to get from " + startLocation.getNode().getRoomName() + " to " + endLocation.getNode().getRoomName() + "!\nThis is most likely caused by an issue with the database. Please contact a hospital administrator to fix this problem!";
                Utils.showAlert(getRoot(),"Path Not Found!", body);
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
        // render drawer content
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
        timeLabel.setText(KioskMain.getPath().totalTimeTaken(path));

    }

    @FXML
    private void showSearch(MouseEvent event) {
        // reset TTS
        if(KioskMain.getTTS().isSpeaking())
            KioskMain.getTTS().cancel();
        speakDirectionsIcon.getStyleClass().setAll("volume-icon");
        // reset the locations
        setEnd(null);
        setStart(KioskMain.getDir().getTheKiosk());
        // show POI
        showPOI(this::selectStart);
        // reset map
        mapController.enableAllButtons();
        mapController.clearOverlay();
        // render drawer content
        root.getChildren().clear();
        root.getChildren().addAll(toggleContainer, searchContainer);
    }

    private void clearDirections() {
        directions.getChildren().clear();
    }

    public Label getDrawerClose() {
        return drawerClose;
    }

    public VBox getOptionsMenuButton() {
        return optionsMenuButton;
    }

    @Override
    public String getURL() {
        return "resources/views/MapView/MapView/DirectionsDrawer/DirectionsDrawer.fxml";
    }

    private void print(Node first) {
        try {
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
        } catch (NullPointerException e) {
            Utils.showAlert(root, "Error While Printing!", "There was an error when attempting to print the " +
                    "directions. Ensure you have a printer installed!");
        }
    }

    @FXML
    private void printDirections(MouseEvent event) {
        String dirs = "\n\nBrigham and Women's Faulkner Hospital Directions\n";
        dirs += "From: " + path.getStart().getRoomName() + "\n";
        dirs += "To: " + path.getEnd().getRoomName() + "\n";
        dirs += path.textPath();
        Text text = new Text();
        text.setFont(new Font(14));
        text.setText(dirs);
        print(text);
    }

    @FXML
    private void speakDirections(MouseEvent event) {
        if(!KioskMain.getTTS().isSpeaking()) {
            speakDirectionsIcon.getStyleClass().setAll("volume-off-icon");
            KioskMain.getTTS().speak(path.textPath());
        }
        else {
            speakDirectionsIcon.getStyleClass().setAll("volume-icon");
            KioskMain.getTTS().cancel();
        }
    }

    private void qrDirections(MouseEvent event) {
        QRManager qr = new QRManager(root, path.textPath());

    }
}
