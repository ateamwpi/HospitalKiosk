import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.db.DatabaseManager;
import models.dir.DirectoryManager;
import models.path.PathfindingManager;

public class Main extends Application {

    private static DirectoryManager theDirManager;
    private static PathfindingManager thePathManager;
    private static DatabaseManager theDBManager;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/MainMenu.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static DirectoryManager getTheDirManager() {
        return theDirManager;
    }

    public static PathfindingManager getThePathManager() {
        return thePathManager;
    }

    public static DatabaseManager getTheDBManager() {
        return theDBManager;
    }
}
