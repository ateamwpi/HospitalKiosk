package models.path;

import models.dir.Location;

import java.util.ArrayList;

/**
 * Created by mattm on 3/29/2017.
 */
public class Node {
    private static int nextNodeID;

    private int x;
    private int y;
    private final int id;
    private ArrayList<Node> connections;
    private ArrayList<Location> locations;
    private final boolean isNew;

    int heuristicCost = 0; //Heuristic cost
    int finalCost = 0; //G+H
    Node parent;


    /** This constructor should _ONLY_ be used when loading from the database. For any
     *  new nodes created, use Node(x, y) and a unique ID will automatically be generated.
     */
    public Node(int id, int x, int y) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.connections = new ArrayList<Node>();
        this.locations = new ArrayList<Location>();
        this.isNew = false;
    }

    public Node(int x, int y) {
        this.id = getNextNodeID();
        this.x = x;
        this.y = y;
        this.connections = new ArrayList<Node>();
        this.locations = new ArrayList<Location>();
        this.isNew = true;
    }

    public void addLocation(Location l) {
        this.locations.add(l);
    }

    public void addConnection(Node other) {
        if(!this.connections.contains(other)) {
            this.connections.add(other);
            other.addConnection(this);
        }
    }

    public int getID() {
        return this.id;
    }

    public int getX() { return this.x; }

    public int getY() { return this.y; }

    public ArrayList<Location> getLocations() {
        return this.locations;
    }

    public ArrayList<Node> getConnections() {
        return this.connections;
    }

    public void removeConnection(Node n) {
        this.connections.remove(n);
    }

    public void removeLocation(Location l) {
        this.locations.remove(l);
    }

    public String toString() {
        String str= "Node: ID=" + this.id + ", X=" + this.x + ", Y=" + this.y + "\n";
        for (Node n : this.connections) {
            str += "Connected to Node ID=" + n.getID() + "\n";
        }
        return str;
    }

    public boolean isNew() {
        return this.isNew;
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
