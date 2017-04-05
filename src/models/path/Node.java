package models.path;

import core.KioskMain;
import models.dir.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by mattm on 3/29/2017.
 */
public class Node {
    private static int nextNodeID;

    private int x;
    private int y;
    private final int id;
    private String roomName;
    private ArrayList<Node> connections;
    private ArrayList<Location> locations;
    private final boolean isNew;
    private boolean isDone;

    int heuristicCost = 0; //Heuristic cost
    int finalCost = 0; //G+H
    Node parent;


    /** This constructor should _ONLY_ be used when loading from the database. For any
     *  new nodes created, use Node(x, y) and a unique ID will automatically be generated.
     */
    public Node(int id, int x, int y, String roomName) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.roomName = roomName;
        this.connections = new ArrayList<Node>();
        this.locations = new ArrayList<Location>();
        this.isNew = false;
        this.isDone = false;
    }

    public Node(int x, int y, String roomName) {
        this.id = getNextNodeID();
        this.x = x;
        this.y = y;
        this.roomName = roomName;
        this.connections = new ArrayList<Node>();
        this.locations = new ArrayList<Location>();
        this.isNew = true;
        this.isDone = true;
    }

    public Node(int x, int y) {
        this.id = getNextNodeID();
        this.x = x;
        this.y = y;
        this.roomName = "NONE";
        this.connections = new ArrayList<Node>();
        this.locations = new ArrayList<Location>();
        this.isNew = true;
        this.isDone = true;
    }

    public void addLocation(Location l) {
        this.locations.add(l);
    }

    public void addConnection(Node other) {
        if(!this.connections.contains(other)) {
            this.connections.add(other);
            if(this.isDone && !other.connections.contains(this)) {
                KioskMain.getDB().addConnection(this, other);
            }
            other.addConnection(this);
        }
    }

    public void removeConnection(Node other) {
        if(this.connections.contains(other)) {
            this.connections.remove(other);
            if(other.connections.contains(this)) {
                KioskMain.getDB().removeConnection(this, other);
            }
            other.removeConnection(this);
        }
    }

    public void removeAllConnections() {
        ArrayList<Node> clone = (ArrayList<Node>) this.connections.clone();
        this.connections.clear();
        for (Node n : clone) {
            n.removeConnection(this);
        }
    }

    public void setX(int x) {
        this.x = x;
        KioskMain.getDB().updateNode(this);
    }

    public void setY(int y) {
        this.y = y;
        KioskMain.getDB().updateNode(this);
    }

    public void setRoomName(String name) {
        this.roomName = name;
        KioskMain.getDB().updateNode(this);
    }

    public void setConnections(Collection<Node> conns) {
        // Add anything new
        for (Node n : conns) {
            if(!this.connections.contains(n)) {
                this.addConnection(n);
            }
        }

        // Remove anything old
        for (Node n : this.connections) {
            if(!conns.contains(n)) {
                this.removeConnection(n);
            }
        }
    }

    public int getID() {
        return this.id;
    }

    public int getX() { return this.x; }

    public int getY() { return this.y; }

    public String getRoomName() {
        return this.roomName;
    }

    public ArrayList<Location> getLocations() {
        return this.locations;
    }

    public ArrayList<Node> getConnections() {
        return this.connections;
    }

    public void removeLocation(Location l) {
        this.locations.remove(l);
    }

    public String toString() {
        String str= "Node: ID=" + this.id + ", X=" + this.x + ", Y=" + this.y + ", NAME=" + this.roomName + "\n";
        for (Node n : this.connections) {
            str += "Connected to Node ID=" + n.getID() + "\n";
        }
        return str;
    }

    public boolean isNew() {
        return this.isNew;
    }

    public void setDone() {
        this.isDone = true;
    }


    public static void setNextNodeID(int i) {
        nextNodeID = i;
    }

    public static int getNextNodeID() {
        int val = nextNodeID;
        nextNodeID ++;
        return val;
    }
}
