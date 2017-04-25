package controllers.MapView.MapView;

import controllers.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Created by dylan on 4/18/17.
 */
public class DirectionStep extends AbstractController {

    public enum DirectionIcon {
        LEFT("turn-left-icon"),
        RIGHT("turn-right-icon"),
        STRAIGHT("go-straight-icon");

        public String path;

        DirectionIcon(String path) {
            this.path = path;
        }

        public static DirectionIcon forString(String name) {
            switch (name.toUpperCase()) {
                case "LEFT": return LEFT;
                case "RIGHT": return RIGHT;
                default: return STRAIGHT;
            }
        }
    }

    private DirectionIcon direction;
    private String message;

    @FXML
    private Label directionLabel;
    @FXML
    private Label directionIcon;

    public DirectionStep (DirectionIcon direction, String message) {
        super(direction, message);
    }

    @Override
    public void initData(Object... data) {
        direction = (DirectionIcon) data[0];
        message = (String) data[1];
    }

    @FXML
    private void initialize() {
        directionLabel.setText(message);
        // add direction icon
        directionIcon.getStyleClass().add(direction.path);
    }

    public String toString() {
        return direction.name() + " " + message;
    }

    @Override
    public String getURL() {
        return "resources/views/MapView/MapView/DirectionStep.fxml";
    }
}
