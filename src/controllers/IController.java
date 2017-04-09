package controllers;

import javafx.scene.Parent;

/**
 * Created by dylan on 4/8/17.
 */
public interface IController {
    void initData(Object... data);
    String getURL();
    Parent getRoot();
}
