package models.db;

import core.KioskMain;
import models.dir.Directory;
import models.dir.Location;
import models.dir.LocationType;
import models.path.Node;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mattm on 3/29/2017.
 */
public class DatabaseManager {

    private Connection conn;

    public DatabaseManager() {}

    public void connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        System.out.println("Successfully located database drivers.");
        this.conn = DriverManager.getConnection("jdbc:derby:hospitalDB;create=false");
        System.out.println("Successfully connected to database.");
    }

    /* LOCATIONS AND DIRECTORIES */

    public HashMap<LocationType, Directory> getAllDirectories() {
        // Run SQL query to get all LOCATIONS from the database
        HashMap<LocationType, Directory> allDirectories = new HashMap<LocationType, Directory>();
        Statement stmt = null;
        ResultSet rset = null;
        try {
            stmt = conn.createStatement();
            rset = stmt.executeQuery("SELECT * FROM LOCATION ORDER BY ID ASC");        } catch (SQLException e) {
            System.out.println("Failed to load information from the database!");
            e.printStackTrace();
        }

        int id = 0, nodeid;
        String name;
        LocationType locType;
        Location theLoc;
        Node theNode;

        for (LocationType l : LocationType.values()) {
            allDirectories.put(l, new Directory(l.name()));
        }

        try {
            // Go through each entry and create a new Location object
            while (rset.next()) {
                id = rset.getInt("ID");
                nodeid = rset.getInt("NODEID");
                name = rset.getString("NAME");
                locType = LocationType.getType(rset.getString("LOCTYPE"));
                theNode = KioskMain.getPath().getNode(nodeid);
                theLoc = new Location(id, name, locType, theNode);
                allDirectories.get(locType).addLocation(theLoc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Location.setNextLocID(id+1);

        // Return all the Directories
        return allDirectories;
    }

    public void addLocation(Location l) {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("INSERT INTO Location VALUES (" + l.getID() + ", '" + l.getName() + "', '" + l.getLocType().name() + "', " + l.getNode().getID() + ")");
        }
        catch (SQLException e) {
            System.out.println("Failed to add " + l.toString() + " to the database.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void removeLocation(Location l) {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("DELETE FROM LOCATION WHERE ID=" + l.getID());
        }
        catch (SQLException e) {
            System.out.println("Failed to remove " + l.toString() + " from the database.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /* NODES */

    public HashMap<Integer, Node> getAllNodes() {
        // Run SQL query to get all NODEs from the database
        HashMap<Integer, Node> allNodes = new HashMap<Integer, Node>();
        Statement stmt = null;
        ResultSet rset = null;
        try {
            stmt = conn.createStatement();
            rset = stmt.executeQuery("SELECT * FROM NODE ORDER BY ID ASC");
        } catch (SQLException e) {
            System.out.println("Failed to load information from the database!");
            e.printStackTrace();
        }

        int x, y, id = 0;

        try {
            // Go through each entry one at a time and make a new Node object
            while (rset.next()) {
                id = rset.getInt("ID");
                x = rset.getInt("X");
                y = rset.getInt("Y");
                allNodes.put(id, new Node(id, x, y));
            }

            // Run SQL query to get all EDGES from the database
            rset = stmt.executeQuery("SELECT * FROM EDGE");
            Node.setNextNodeID(id+1);

            Node n1, n2;

            // Go through each entry and connect the two nodes mentioned
            while (rset.next()) {
                n1 = allNodes.get(rset.getInt("ANODEID"));
                n2 = allNodes.get(rset.getInt("BNODEID"));

                n1.addConnection(n2);
            }

            rset.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return the completed list of all nodes
        return allNodes;
    }

    public void addNode(Node n) {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("INSERT INTO Node VALUES (" + n.getID() + ", " + n.getX() + ", " + n.getY() + ")");
        }
        catch (SQLException e) {
            System.out.println("Failed to add " + n.toString() + " to the database.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void removeNode(Node n) {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("DELETE FROM NODE WHERE ID=" + n.getID());
        }
        catch (SQLException e) {
            System.out.println("Failed to remove " + n.toString() + " from the database.");
            e.printStackTrace();
            System.exit(1);
        }
    }

}
