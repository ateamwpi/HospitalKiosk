package models.path;

import core.KioskMain;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;
import models.dir.Location;
import models.dir.LocationType;

import java.util.*;

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
    private HashMap<Integer, Location> locations;
    private HashMap<LocationType, Integer> counts;
    private final boolean isNew;
    private boolean isDone;

    /** This constructor should _ONLY_ be used when loading from the database. For any
     *  new nodes created, use Node(x, y) and a unique ID will automatically be generated.
     */
    public Node(int id, int x, int y, String roomName) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.roomName = roomName;
        this.connections = new ArrayList<Node>();
        this.locations = new HashMap<Integer, Location>();
        this.counts = new HashMap<LocationType, Integer>();
        this.isNew = false;
        this.isDone = false;
    }

    public Node(int x, int y, String roomName) {
        this.id = getNextNodeID();
        this.x = x;
        this.y = y;
        this.roomName = roomName;
        this.connections = new ArrayList<Node>();
        this.locations = new HashMap<Integer, Location>();
        this.counts = new HashMap<LocationType, Integer>();
        this.isNew = true;
        this.isDone = true;
    }

    public Node(int x, int y) {
        this.id = getNextNodeID();
        this.x = x;
        this.y = y;
        this.roomName = "NONE";
        this.connections = new ArrayList<Node>();
        this.locations = new HashMap<Integer, Location>();
        this.counts = new HashMap<LocationType, Integer>();
        this.isNew = true;
        this.isDone = true;
    }

    public void addLocation(Location l) {
        this.locations.put(l.getID(), l);
        if(!this.counts.containsKey(l.getLocType())) this.counts.put(l.getLocType(), 1);
        else this.counts.put(l.getLocType(), this.counts.get(l.getLocType()) + 1);
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

    public Color getColor() {
        if(this.counts.isEmpty()) return LocationType.Unknown.getNodeColor();
        LocationType lt = Collections.max(this.counts.entrySet(), Map.Entry.comparingByValue()).getKey();
        return lt.getNodeColor();
    }

    public void removeConnection(Node other) {
        if(this.connections.contains(other)) {
            this.connections.remove(other);
            KioskMain.getDB().removeConnection(this, other);
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
        KioskMain.getPath().updateRoomName(this, name);
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
        ArrayList<Node> toRemove = new ArrayList<Node>();
        for (Node n : this.connections) {
            if(!conns.contains(n)) {
                toRemove.add(n);
            }
        }

        for (Node n : toRemove) {
            this.removeConnection(n);
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

    public Collection<Location> getLocations() {
        return this.locations.values();
    }

    public ArrayList<Node> getConnections() {
        return (ArrayList<Node>)this.connections.clone();
    }

    public void removeLocation(Location l) {
        System.out.println("hi");
        this.locations.remove(l.getID());
        this.counts.put(l.getLocType(), this.counts.get(l.getLocType())-1);
    }

    public String toString() {
        String str= "Node: ID=" + this.id + ", X=" + this.x + ", Y=" + this.y + ", NAME=" + this.roomName + "\n";
        for (Node n : this.connections) {
            str += "Connected to Node ID=" + n.getID() + "\n";
        }
        return str;
    }

//    public boolean equals(Object o) {
//        Node n = (Node) o;
//        return n.getID() == this.getID();
//    }

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

    public SimpleStringProperty nodeIDProperty() {
        String nodeIDString = Integer.toString(this.id);
        return new SimpleStringProperty(nodeIDString);
    }
}
