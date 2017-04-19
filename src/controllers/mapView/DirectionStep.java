package controllers.mapView;

import controllers.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.dir.Directory;
import models.path.Direction;

/**
 * Created by dylan on 4/18/17.
 */
public class DirectionStep extends AbstractController {

    private static final String TURN_LEFT_ICON = "turn-left-icon";
    private static final String TURN_RIGHT_ICON = "turn-right-icon";
    private static final String GO_STRAIGHT_ICON = "go-straight-icon";

    private Direction direction;
    private String message;

    @FXML
    private Label directionLabel;
    @FXML
    private Label directionIcon;

    public DirectionStep (Direction direction, String message) {
        super(direction, message);
    }

    @Override
    public void initData(Object... data) {
        direction = (Direction) data[0];
        message = (String) data[1];
    }

    @FXML
    private void initialize() {
        directionLabel.setText(message);
        // add direction icon
        directionIcon.getStyleClass().add(TURN_LEFT_ICON);
    }

    @Override
    public String getURL() {
        return "views/directionStep.fxml";
    }
}
