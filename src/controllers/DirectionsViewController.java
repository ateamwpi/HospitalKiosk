package controllers;

import controllers.map.MapController;
import core.KioskMain;
import core.Utils;
import core.exception.FloorNotReachableException;
import core.exception.NearestNotFoundException;
import core.exception.PathNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import models.path.Node;
import models.path.Path;

/**
 * Created by mattm on 3/29/2017.
 */
public class DirectionsViewController extends AbstractController {

    private Path path;

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
        MapController mapController = new MapController();
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
        Text text = new Text();
        text.setFont(new Font(20));
        text.setText(directionsText.getText());
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(null)) {
            boolean success = job.printPage(text);
            if (success) {
                job.endJob();
            }
        }
    }

    @FXML
    private void clickDone(ActionEvent event) {
        KioskMain.getUI().setScene(new MainMenuController());
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
