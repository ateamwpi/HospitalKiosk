package controllers;

import core.KioskMain;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dylan on 4/9/17.
 *
 * NOTE:
 *      Non-static attribute declaration assignments are executed after the child constructor and initialize(),
 *      meaning all object attributes used in initialize() must be initialized in initData().
 * NOTE:
 *      The root parent node is assumed to have an id of root. To change this, override getRoot().
 * NOTE:
 *      Data needed in initialize() must be passed to super() in the constructor and assigned to attributes in initData()
 *
 */
public abstract class AbstractController implements IController {

    @FXML
    private Parent root;

    protected AbstractController(Object... data) {
        // initialize data
        initData(data);
        // get the loader
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(getURL()));
        // set the controller
        loader.setController(this);
        // load the view
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void initData(Object... data) {}

    @Override
    public Parent getRoot() {
        return root;
    }

}
