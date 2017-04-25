package models.db;

import core.KioskMain;
import core.exception.WrongFloorException;
import models.dir.Directory;
import models.dir.Location;
import models.dir.LocationType;
import models.path.Node;

import java.sql.*;
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
        conn = DriverManager.getConnection("jdbc:derby:hospitalDB;create=false");
        System.out.println("Successfully connected to database.");
    }

    /* LOCATIONS AND DIRECTORIES */

    public HashMap<LocationType, Directory> getAllDirectories() {
        // Run SQL query to get all LOCATIONS from the database
        HashMap<LocationType, Directory> allDirectories = new HashMap<>();
        Statement stmt;
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
            assert rset != null;
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
            String str = "INSERT INTO LOCATION VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(str);
            stmt.setInt(1, l.getID());
            stmt.setString(2, l.getName());
            stmt.setString(3, l.getLocType().name());
            stmt.setInt(4, l.getNode().getID());
            stmt.execute();
        }
        catch (SQLException e) {
            System.out.println("Failed to add " + l.toString() + " to the database.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void removeLocation(Location l) {
        try {
            String str = "DELETE FROM LOCATION WHERE ID=?";
            PreparedStatement stmt = conn.prepareStatement(str);
            stmt.setInt(1, l.getID());
            stmt.execute();
        }
        catch (SQLException e) {
            System.out.println("Failed to remove " + l.toString() + " from the database.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void updateLocation(Location l) {
        try {
            String str = "UPDATE LOCATION SET NAME=?, LOCTYPE=?, NODEID=? WHERE ID=?";
            PreparedStatement stmt = conn.prepareStatement(str);
            stmt.setString(1, l.getName());
            stmt.setString(2, l.getLocType().name());
            stmt.setInt(3, l.getNode().getID());
            stmt.setInt(4, l.getID());
            stmt.execute();
        }
        catch (SQLException e) {
            System.out.println("Failed to update location " + l.toString() + ".");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /* NODES */

    public HashMap<Integer, Node> getAllNodes() {
        // Run SQL query to get all NODEs from the database
        HashMap<Integer, Node> allNodes = new HashMap<>();
        Statement stmt = null;
        ResultSet rset = null;
        try {
            stmt = conn.createStatement();
            rset = stmt.executeQuery("SELECT * FROM NODE ORDER BY ID ASC");
        } catch (SQLException e) {
            System.out.println("Failed to load information from the database!");
            e.printStackTrace();
        }

        int x, y, floor, id = 0;
        boolean restricted;
        String roomName;

        //noinspection TryWithIdenticalCatches
        try {
            // Go through each entry one at a time and make a new Node object
            assert rset != null;
            while (rset.next()) {
                id = rset.getInt("ID");
                x = rset.getInt("X");
                y = rset.getInt("Y");
                floor = rset.getInt("FLOOR");
                roomName = rset.getString("ROOMNAME");
                restricted = rset.getBoolean("RESTRICTED");
                allNodes.put(id, new Node(id, x, y, floor, restricted, roomName));
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

            for (Node n : allNodes.values()) {
                n.setDone();
            }

            rset.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (WrongFloorException e) {
            e.printStackTrace();
            // this shouldn't be reachable unless there is bad data in the database
        }

        // Return the completed list of all nodes
        return allNodes;
    }

    public void addNode(Node n) {
        try {
            String str = "INSERT INTO NODE VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(str);
            stmt.setInt(1, n.getID());
            stmt.setInt(2, n.getX());
            stmt.setInt(3, n.getY());
            stmt.setString(4, n.getRoomName());
            stmt.setInt(5, n.getFloor());
            stmt.setBoolean(6, n.isRestricted());
            stmt.execute();
        }
        catch (SQLException e) {
            System.out.println("Failed to add " + n.toString() + " to the database.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void removeNode(Node n) {
        try {
            String str1 = "DELETE FROM EDGE WHERE ANODEID=?";
            String str2 = "DELETE FROM EDGE WHERE BNODEID=?";
            String str3 = "DELETE FROM NODE WHERE ID=?";
            PreparedStatement stmt1 = conn.prepareStatement(str1);
            PreparedStatement stmt2 = conn.prepareStatement(str2);
            PreparedStatement stmt3 = conn.prepareStatement(str3);
            stmt1.setInt(1, n.getID());
            stmt2.setInt(1, n.getID());
            stmt3.setInt(1, n.getID());
            stmt1.execute();
            stmt2.execute();
            stmt3.execute();
        }
        catch (SQLException e) {
            System.out.println("Failed to remove " + n.toString() + " from the database.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void updateNode(Node n) {
        try {
            String str = "UPDATE NODE SET X=?, Y=?, FLOOR=?, ROOMNAME=?, RESTRICTED=? WHERE ID=?";
            PreparedStatement stmt = conn.prepareStatement(str);
            stmt.setInt(1, n.getX());
            stmt.setInt(2, n.getY());
            stmt.setInt(3, n.getFloor());
            stmt.setString(4, n.getRoomName());
            stmt.setBoolean(5, n.isRestricted());
            stmt.setInt(6, n.getID());
            stmt.execute();
        }
        catch (SQLException e) {
            System.out.println("Failed to update node " + n.toString() + ".");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /* CONNECTIONS */

    public void addConnection(Node n1, Node n2) {
        try {
            String str = "INSERT INTO EDGE VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(str);
            stmt.setInt(1, n1.getID());
            stmt.setInt(2, n2.getID());
            stmt.execute();
        }
        catch (SQLException e) {
            System.out.println("Failed to add connection between " + n1.getID() + " and " + n2.getID() + ".");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void removeConnection(Node n1, Node n2) {
        try {
            String str = "DELETE FROM EDGE WHERE ANODEID=? AND BNODEID=?";
            PreparedStatement stmt1 = conn.prepareStatement(str);
            //PreparedStatement stmt2 = conn.prepareStatement(str);
            stmt1.setInt(1, n1.getID());
            stmt1.setInt(2, n2.getID());
            //stmt2.setInt(1, n2.getID());
            //stmt2.setInt(2, n1.getID());
            stmt1.execute();
            //stmt2.execute();
        }
        catch (SQLException e) {
            System.out.println("Failed to remove connection between " + n1.getID() + " and " + n2.getID() + ".");
            e.printStackTrace();
            System.exit(1);
        }
    }

}
