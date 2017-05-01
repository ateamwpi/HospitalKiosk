package core;

import controllers.PopupView.AlertViewController;
import controllers.PopupView.DropdownAlertViewController;
import controllers.PopupView.OptionAlertViewController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Parent;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

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

    public static void showAlert(Parent root, String title, String body) {
        new AlertViewController(root, title, body);
    }

    public static void showAlert(Parent root, String title, String body, Consumer<Event> onClose) {
        new AlertViewController(root, title, body, onClose);
    }

    public static void showOption(Parent root, String title, String body, String cancelText, String confirmText, Consumer<Boolean> setConfirm) {
        new OptionAlertViewController(root, title, body, cancelText, confirmText, setConfirm);
    }
    public static void showOption(Parent root, String title, String body, String cancelText, String confirmText, Consumer<Boolean> setConfirm, Runnable onCancel) {
        new OptionAlertViewController(root, title, body, cancelText, confirmText, setConfirm, onCancel);
    }

    public static void showDropdown(Parent root, String title, String body, Collection<String> items, String def, Consumer<String> fcn) {
        new DropdownAlertViewController(root, title, body, items, def, fcn);
    }

    public static void hidePopup() {
        if(KioskMain.getUI().getPopup() != null) {
            KioskMain.getUI().getPopup().hide();
        }
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

    private static URL getResource(String resource) {
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

    public static<R> Consumer<R> applyAndAccept(Function<? super R,? extends R> f, Consumer<R> c){
        return t -> c.accept(f.apply(t));
    }
}
