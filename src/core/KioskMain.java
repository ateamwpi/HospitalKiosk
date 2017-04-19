package core;

import controllers.mapView.MapViewController;
import javafx.application.Application;
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
import java.util.HashMap;

public class KioskMain extends Application {

    private static DirectoryManager theDirManager;
    private static PathfindingManager thePathManager;
    private static DatabaseManager theDBManager;
    private static TTSManager theTTSManager;
    private static UIManager theUIManager;

    private static final boolean DEBUG = true;

    @Override
    public void start(Stage stage) {
        initUIMg(stage);
        // load the main menu
        getUI().setScene(new MapViewController());
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

    private static void initTTSMg() {
        theTTSManager = new TTSManager();
    }

    private static void initUIMg(Stage stage) {
        theUIManager = new UIManager(stage);
    }
}
