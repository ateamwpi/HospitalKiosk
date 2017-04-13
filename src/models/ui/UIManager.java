package models.ui;

import controllers.IController;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by dylan on 4/11/17.
 */
public class UIManager {

    private Stage stage;
    private Scene scene;

    public UIManager(Stage stage) {
        this.stage = stage;
        this.stage.show();
    }

    public void setScene(IController controller) {
        scene = new Scene(controller.getRoot());
        stage.setScene(scene);
    }

    public Node lookup(String query) {
        return scene.lookup(query);
    }
}
