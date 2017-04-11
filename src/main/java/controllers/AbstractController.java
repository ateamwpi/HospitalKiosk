package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * Created by dylan on 4/9/17.
 *
 * NOTE:
 *      Non-static attribute declaration assignments are executed after the child constructor and initialize(),
 *      meaning all object attributes used in initialize() must be initialized in initData().
 * NOTE:
 *      The root parent node is assumed to have an id of root. To change this, override getRoot().
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

    public void initData(Object... data) {}

    public Parent getRoot() {
        return root;
    }

}
