package core;

import javafx.scene.control.Alert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by mattm on 4/14/2017.
 */
public class Utils {

    public static String strForNum(int i) {
        // Assumes there will never be more than 20 turns options.
        switch(i) {
            case 1: return "1st";
            case 2: return "2nd";
            case 3: return "3rd";
            default: return i + "th";
        }
    }

    public static void showError(String title, String body) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(title);
        a.setTitle("Try Again!");
        a.setContentText(body);
        a.showAndWait();
    }

    /* Returns a instance of InputStream for the resource */
    public static InputStream getResourceAsStream(String resource) {
        try {
            return getResource(resource).openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getResourceAsExternal(String resource) {
        return getResource(resource).toExternalForm();
    }

    public static URL getResource(String resource) {
        String stripped = resource.startsWith("/")?resource.substring(1):resource;
        URL path = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader != null) {
            path = classLoader.getResource(stripped);
        }
        if (path == null) {
            path = Utils.class.getResource(resource);
        }
        if (path == null) {
            path = Utils.class.getClassLoader().getResource(stripped);
        }
        if (path == null) {
            throw new RuntimeException("Resource not found: " + resource);
        }
        return path;
    }
}
