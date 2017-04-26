package core;

import controllers.AboutPageController;
import controllers.IController;
import controllers.WelcomeScreenController;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import models.db.DatabaseManager;
import models.dir.Directory;
import models.dir.DirectoryManager;
import models.dir.Location;
import models.dir.LocationType;
import models.path.PathfindingManager;
import models.path.Node;
import models.tts.TTSManager;
import models.ui.UIManager;

import java.sql.SQLException;
import java.sql.Time;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class KioskMain extends Application {

    private static DirectoryManager theDirManager;
    private static PathfindingManager thePathManager;
    private static DatabaseManager theDBManager;
    private static TTSManager theTTSManager;
    private static UIManager theUIManager;

    private static final boolean DEBUG = true;
    private static final String MAIN_ENTR_NAME = "Main Entrance";
    private static final String BELKIN_ENTR_NAME = "Belkin Entrance";


    @Override
    public void start(Stage stage) {
        initUIMg(stage);
        // load the main menu
        //getUI().setScene(new MapViewControllerOLD());
        getUI().setScene(new WelcomeScreenController());
    }

    public static void main(String[] args) {
        // setup the managers
        initManagers();
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

    public static TTSManager getTTS() { return theTTSManager; }

    public static UIManager getUI() { return theUIManager; }

    private static void initManagers() {
        initDBMg();
        initPathMg();
        initDirMg();
        initTTSMg();
    }

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

        // Find the kiosk
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

        // Find main entrance and belkin entrance
        Location mainEntr = null;
        Location belkinEntr = null;
        for(Location l : allDirectories.get(LocationType.Entrance).getLocations().values()) {
            if(l.getName().equals(MAIN_ENTR_NAME)) {
                mainEntr = l;
            }
            if(l.getName().equals(BELKIN_ENTR_NAME)) {
                belkinEntr = l;
            }
        }
        if(mainEntr == null) {
            System.out.println("Error initializing DirectoryManager: No location named " + MAIN_ENTR_NAME + " was found!");
            System.exit(1);
        }
        if(belkinEntr == null) {
            System.out.println("Error initializing DirectoryManager: No location named " + BELKIN_ENTR_NAME + " was found!");
            System.exit(1);
        }

        theDirManager = new DirectoryManager(allDirectories, theKiosk, mainEntr, belkinEntr);
        // Test code to print all directories/locations
        if (DEBUG) {
            for (Directory d : getDir().getDirectories().values()) {
                System.out.println(d);
            }
            System.out.println("The Kiosk: " + getDir().getTheKiosk());
        }
    }

    private static void initTTSMg() {
        theTTSManager = new TTSManager();
    }

    private static void initUIMg(Stage stage) {
        theUIManager = new UIManager(stage);
    }
}
