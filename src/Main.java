import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.db.DatabaseManager;
import models.dir.DirectoryManager;
import models.path.PathfindingManager;

import java.sql.SQLException;

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
        // Create the Database Manager and connect to the DB
        theDBManager = new DatabaseManager();
        try {
            getDB().connect();
        }
        catch (ClassNotFoundException e) {
            System.out.println("Java DB Driver not found. Add the classpath to your module.");
            e.printStackTrace();
            System.exit(1);
        }
        catch (SQLException e) {
            System.out.println("Failed to connect to database!");
            e.printStackTrace();
            return;
        }

        // Using the information from the Database, create the Pathfinding
        // and Directory Managers
        thePathManager = new PathfindingManager(getDB().getAllNodes());
        theDirManager = new DirectoryManager(getDB().getAllDirectories());

        // Launch the JavaFX application after initial setup
        launch(args);
    }

    public static DirectoryManager getDir() {
        return theDirManager;
    }

    public static PathfindingManager getPath() {
        return thePathManager;
    }

    public static DatabaseManager getDB() {
        return theDBManager;
    }
}
