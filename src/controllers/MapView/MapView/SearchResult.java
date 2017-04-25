package controllers.MapView.MapView;

import controllers.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.dir.Location;

import java.util.function.Consumer;

/**
 * Created by dylan on 4/18/17.
 */
public class SearchResult extends AbstractController {

    private static final String PLACE_ICON = "place-icon";

    private Location location;

    @FXML
    private Label locationLabel;
    @FXML
    private Label locationIcon;
    private Consumer<Location> selectLocation;

    public SearchResult(Location location, Consumer<Location> selectLocation) {
        super(location, selectLocation);
    }

    @Override
    public void initData(Object... data) {
        location = (Location) data[0];
        selectLocation = (Consumer<Location>) data[1];
    }

    @FXML
    private void initialize() {
        locationLabel.setText(location.getName() + " - " + location.getNode().getRoomName());
        locationIcon.getStyleClass().add(PLACE_ICON);
        // TODO add icons for POI
        // add click handler
        getRoot().setOnMouseClicked(event -> selectLocation.accept(location));
    }

    @Override
    public String getURL() {
        return "resources/views/MapView/MapView/SearchResult.fxml";
    }
}
