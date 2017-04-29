package controllers;


import javafx.beans.NamedArg;
import javafx.scene.control.Label;

/**
 * Created by dylan on 4/28/17.
 */
public class Icon extends Label {

    private static final Double DEF_PREF_HEIGHT = 20.0;
    private static final Double DEF_PREF_WIDTH = 20.0;
    private static final Double DEF_SIZE = 30.0;
    private static final String MAIN_CSS_URL = "/resources/styles/Main.css";

    private String path;
    private Double size;

    //// Public API ////

    public Icon(@NamedArg("path") String path, @NamedArg("size") Double size) {
        setPrefWidth(DEF_PREF_WIDTH);
        setPrefHeight(DEF_PREF_HEIGHT);
        getStylesheets().add(MAIN_CSS_URL);

        setPath(path);
        setSize(size);
    }

    public void setPath(String path) {
        this.path = path;
        updateStyle();
    }

    private void updateStyle() {
        setStyle(getBackgroundImage() + getBackgroundSize());
    }

    public String getPath() {
        return path;
    }

    public void setSize(Integer size) {
        setSize(Double.valueOf(size));
    }

    public void setSize(Double size) {
        this.size = size;
        updateStyle();
    }

    public Double getSize() {
        return size;
    }

    //// Private API ////

    private String getBackgroundImage() {
        return path != null ? "-fx-background-image: url(" + path + ");" : "";
    }

    private String getBackgroundSize() {
        return size != null ? "-fx-background-size: " + size + "px;" : String.valueOf(DEF_SIZE);
    }
}
