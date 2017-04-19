package controllers;

import controllers.map.MapController;
import controllers.mapView.MapViewController;
import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import models.path.Path;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mattm on 3/29/2017.
 */
public class DirectionsViewController extends AbstractController {

    private Path path;

    private MapController mapController;

    @FXML
    private Button backBtn;
    @FXML
    private Button doneButton;
    @FXML
    private AnchorPane mapContainer;
    @FXML
    private TextArea directionsText;
    @FXML
    private ChoiceBox<String> floors;

    DirectionsViewController(Path path) {
        super(path);
    }

    @FXML
    private void initialize() {
        // load the map controller
        mapController = new MapController();
        // add the map to the container
        mapContainer.getChildren().add(mapController.getRoot());
        // draw the path on the map
        mapController.setFloor(path.getStart().getFloor());
        mapController.drawPath(path);
        floors.getItems().addAll(path.getFloorsSpanning());
        floors.getSelectionModel().selectFirst();
        if(path.getFloorsSpanning().size() == 1) floors.setDisable(true);
        floors.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (floors.getSelectionModel().getSelectedItem() != null) {
                String fl = floors.getSelectionModel().getSelectedItem();
                mapController.clearOverlay();
                mapController.setFloor(Integer.parseInt(fl.substring(0,1)));
                mapController.drawPath(path);
            }
        });

        // show the text directions
        directionsText.setText("Directions:\n" + path.textPath());
    }

    @FXML
    private void clickBack(ActionEvent event) {
        KioskMain.getUI().setScene(new DirectoryViewController());
    }

    @FXML
    private void clickSpeak(ActionEvent event) {
        KioskMain.getTTS().speak(directionsText.getText());
    }

    @FXML
    private void clickPrint(ActionEvent event) {
        String dirs = "\n\nBrigham and Women's Faulkner Hospital Directions\n";
        dirs += "From: " + path.getStart().getRoomName() + "\n";
        dirs += "To: " + path.getEnd().getRoomName() + "\n";
        dirs += directionsText.getText();
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
            String orig = floors.getSelectionModel().getSelectedItem();
            for (String s : path.getFloorsSpanning()) {
                floors.getSelectionModel().select(s);
                double scaleX = pageLayout.getPrintableWidth() / mapContainer.getBoundsInParent().getWidth();
                Scale scale = new Scale(scaleX, scaleX);
                mapContainer.getTransforms().add(scale);
                success = job.printPage(mapContainer);
                mapContainer.getTransforms().remove(scale);
            }
            floors.getSelectionModel().select(orig);
            if (success) {
                job.endJob();
            }
        }
    }

    @FXML
    private void clickDone(ActionEvent event) {
        KioskMain.getUI().setScene(new MapViewController());
    }

    @Override
    public void initData(Object... data) {
        this.path = (Path)data[0];
    }

    @Override
    public String getURL() {
        return "views/DirectionsView.fxml";
    }
}
