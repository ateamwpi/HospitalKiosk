package core;

import controllers.IController;
import controllers.MainMenuController;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.stage.Stage;
import models.db.DatabaseManager;
import models.dir.Directory;
import models.dir.DirectoryManager;
import models.dir.Location;
import models.dir.LocationType;
import models.path.PathfindingManager;
import models.path.Node;

import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;

public class KioskMain extends Application {

    private static DirectoryManager theDirManager;
    private static PathfindingManager thePathManager;
    private static DatabaseManager theDBManager;
    private static DoubleProperty fontSize = new SimpleDoubleProperty(10);
    private static Stage stage;

    private static final boolean DEBUG = true;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.show();
        // load main menu controller
        MainMenuController mainMenuController = new MainMenuController();
        // set the scene
        setScene(mainMenuController);
    }

    public static void main(String[] args) {
        // setup the managers
        initDBMg();
        initPathMg();
        initDirMg();

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

    public static void setScene(IController controller) {
        Scene scene = new Scene(controller.getRoot());
        fontSize.bind(scene.widthProperty().add(scene.heightProperty()).divide(100));
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        controller.getRoot().styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), ";"
                ,"-fx-base: rgb(255,255,255);"));

        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());

        stage.setScene(scene);

    }

//    public static void setScene(String path) {
//        try {
//            Parent root = FXMLLoader.load(KioskMain.class.getClassLoader().getResource(path));
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//        } catch (IOException e) {
//            // TODO
//            e.printStackTrace();
//        }
//    }
//
//    // set the scene and return the controller
//    public static IControllerWithParams setScene(String path, Object... data) {
//        try {
//            FXMLLoader loader = new FXMLLoader(KioskMain.class.getClassLoader().getResource(path));
//            Scene scene = new Scene(loader.load());
//            IControllerWithParams controller = loader.<IControllerWithParams>getController();
//            controller.initData(data);
//            stage.setScene(scene);
//            return controller;
//        } catch (IOException e) {
//            // TODO fix this
//            e.printStackTrace();
//            return null;
//        }
//    }

    private static void initDBMg() {
        // create the database manager
        theDBManager = new DatabaseManager();
        try {
            // connect to the db
            getDB().connect();
        } catch (ClassNotFoundException e) {
            System.out.println("Java DB Driver not found. Add the classpath to your module.");
            e.printStackTrace();
            System.exit(1);
        } catch (SQLException e) {
            System.out.println("Failed to connect to database!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void initPathMg() {
        // create the pathfinding manager with nodes from the db
        thePathManager = new PathfindingManager(getDB().getAllNodes());
        // Test code to print all nodes
        if (DEBUG) {
            for (Node n : getDB().getAllNodes().values()) {
                System.out.println(n);
            }
        }
    }

    private static void initDirMg() {
        // create the directory manager with directories from the db
        HashMap<LocationType, Directory> allDirectories = getDB().getAllDirectories();
        HashMap<Integer, Location> kiosks = allDirectories.get(LocationType.Kiosk).getLocations();
        if(kiosks.size() > 1) {
            System.out.println("Error initializing DirectoryManager: More than one Kiosk was found in the database!");
            System.exit(1);
        }
        else if(kiosks.size() < 1) {
            System.out.println("Error initializing DirectoryManager: No Kiosk was found in the database!");
            System.exit(1);
        }
        Location theKiosk = kiosks.values().iterator().next();
        allDirectories.remove(LocationType.Kiosk);
        theDirManager = new DirectoryManager(allDirectories, theKiosk);
        // Test code to print all directories/locations
        if (DEBUG) {
            for (Directory d : getDir().getDirectories().values()) {
                System.out.println(d);
            }
            System.out.println("The Kiosk: " + getDir().getTheKiosk());
        }
    }
}
