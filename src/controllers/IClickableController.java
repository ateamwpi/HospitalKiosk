package controllers;

import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

/**
 * Created by dylan on 4/8/17.
 */
public interface IClickableController extends IController {
    void handleMouseClick(MouseEvent event);
    Group getOverlay();
}
