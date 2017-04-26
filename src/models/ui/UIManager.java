package models.ui;

import controllers.IPopup;
import controllers.IController;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by dylan on 4/11/17.
 */
public class UIManager {

    private Stage stage;
    private Parent root;
    private Scene scene;

    private IPopup popup;
    private static DoubleProperty fontSize = new SimpleDoubleProperty(15);

    public UIManager(Stage stage) {
        this.stage = stage;
        this.stage.show();
    }

    public void setScene(IController controller) {
        scene = new Scene(controller.getRoot());
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

    public IPopup getPopup() {
        return popup;
    }

    public void setPopup(IPopup popup) {
        this.popup = popup;
    }
}
