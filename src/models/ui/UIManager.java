package models.ui;

import controllers.AbstractPopupController;
import controllers.IController;
import controllers.WelcomeScreenController;
import core.KioskMain;
import core.Timeout;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dylan on 4/11/17.
 */
public class UIManager {

    private Stage stage;
    private Parent root;
    private Scene scene;

    private AbstractPopupController popup;
    private static DoubleProperty fontSize = new SimpleDoubleProperty(15);

    public UIManager(Stage stage) {
        this.stage = stage;
        this.stage.show();
    }

    public void setScene(IController controller) {
        Timeout timeout = new Timeout();
        scene = new Scene(controller.getRoot());

        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("mouse click detected! " + mouseEvent.getSource());
            }
        });

        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                System.out.println("key press detected! " + keyEvent.getSource());
                timeout.resetTimer();
            }
        });

//        fontSize.bind(scene.widthProperty().add(scene.heightProperty()).divide(100));
//        Screen screen = Screen.getPrimary();
//        Rectangle2D bounds = screen.getVisualBounds();
//
//        controller.getRoot().styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), ";"
//                ,"-fx-base: rgb(135, 138, 150);"));
//
//        stage.setX(bounds.getMinX());
//        stage.setY(bounds.getMinY());
//        stage.setWidth(bounds.getWidth());
//        stage.setHeight(bounds.getHeight());

        stage.setScene(scene);

        timeout.startTimer();
    }

    public Node lookup(String query) {
        return scene.lookup(query);
    }

    public Scene getScene() {
        return this.scene;
    }

    public Stage getStage() {
        return stage;
    }

    public Parent getRoot() {
        return root;
    }

    public AbstractPopupController getPopup() {
        return popup;
    }

    public void setPopup(AbstractPopupController popup) {
        this.popup = popup;
    }
}
