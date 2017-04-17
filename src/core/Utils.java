package core;

import javafx.scene.control.Alert;

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
}
