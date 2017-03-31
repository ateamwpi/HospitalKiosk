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

    public DatabaseManager() {

    }

    public void connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        System.out.println("Successfully located database drivers.");
        this.conn = DriverManager.getConnection("jdbc:derby:hospitalDB;create=false");
        System.out.println("Successfully connected to database.");
    }

    public HashMap<LocationType, Directory> getAllDirectories() {
        // Run SQL query to get all LOCATIONS from the database
        HashMap<LocationType, Directory> allDirectories = new HashMap<LocationType, Directory>();
        Statement stmt = null;
        ResultSet rset = null;
        try {
            stmt = conn.createStatement();
            rset = stmt.executeQuery("SELECT * FROM LOCATION");
        } catch (SQLException e) {
            System.out.println("Failed to load information from the database!");
            e.printStackTrace();
        }

        int id, nodeid;
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
                allDirectories.get(locType).addEntry(theLoc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return all the Directories
        return allDirectories;
    }

    public HashMap<Integer, Node> getAllNodes() {
        // Run SQL query to get all NODEs from the database
        HashMap<Integer, Node> allNodes = new HashMap<Integer, Node>();
        Statement stmt = null;
        ResultSet rset = null;
        try {
            stmt = conn.createStatement();
            rset = stmt.executeQuery("SELECT * FROM NODE");
        } catch (SQLException e) {
            System.out.println("Failed to load information from the database!");
            e.printStackTrace();
        }

        int x, y, id;

        try {
            // Go through each entry one at a time and make a new Node object
            while (rset.next()) {
                id = rset.getInt("ID");
                x = rset.getInt("X");
                y = rset.getInt("Y");
                allNodes.put(id, new Node(id, x, y));
            }

            // Run SQL query to get all EDGES from the database
            rset = stmt.executeQuery("SELECT * FROM EDGES");

            Node n1, n2;

            // Go through each entry and connect the two nodes mentioned
            while (rset.next()) {
                n1 = allNodes.get(rset.getInt("ANODEID"));
                n2 = allNodes.get(rset.getInt("BNODEID"));

                n1.addConnection(n2);
                n2.addConnection(n1);
            }

            rset.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return the completed list of all nodes
        return allNodes;
    }

}
