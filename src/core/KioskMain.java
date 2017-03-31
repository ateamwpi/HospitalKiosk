package core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.db.DatabaseManager;
import models.dir.Directory;
import models.dir.DirectoryManager;
import models.path.PathfindingManager;
import models.path.Node;

import java.io.IOException;
import java.sql.SQLException;

public class KioskMain extends Application {

    private static DirectoryManager theDirManager;
    private static PathfindingManager thePathManager;
    private static DatabaseManager theDBManager;

    private static Stage stage;

    public static final boolean DEBUG = true;

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
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
            System.exit(1);
        }

        try {
            // Using the information from the Database, create the Pathfinding
            // and Directory Managers

            // Test code to print all nodes
            if (DEBUG) {
                for (Node n : getDB().getAllNodes().values()) {
                    System.out.println(n);
                }
            }
            thePathManager = new PathfindingManager(getDB().getAllNodes());

            // Test code to print all directories/locations
            if (DEBUG) {
                for (Directory d : getDB().getAllDirectories().values()) {
                    System.out.println(d);
                }
            }
            theDirManager = new DirectoryManager(getDB().getAllDirectories());
        }
        catch (SQLException e) {
            System.out.println("Failed to load information from the database!");
            e.printStackTrace();
            System.exit(1);
        }
        // Launch the JavaFX application after initial setup
        launch(args);
    }

    public static DirectoryManager getDir() {
        return theDirManager;
    }

    public static PathfindingManager getPath() {
        return thePathManager;
    }

    public static DatabaseManager getDB() { return theDBManager; }

    public static void setScene(String path) throws IOException {
        Parent root = FXMLLoader.load(KioskMain.class.getClassLoader().getResource(path));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
