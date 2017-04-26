package controllers;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.util.Map;

/**
 * Created by dylan on 4/8/17.
 */
public interface IController {
    void initData(Object... data);
    String getURL();
    Parent getRoot();
}
