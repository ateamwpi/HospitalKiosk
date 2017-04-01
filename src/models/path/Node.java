package models.path;

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
    private final boolean isNew;

    /** This constructor should _ONLY_ be used when loading from the database. For any
     *  new nodes created, use Node(x, y) and a unique ID will automatically be generated.
     */
    public Node(int id, int x, int y) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.connections = new ArrayList<Node>();
        this.isNew = false;
    }

    public Node(int x, int y) {
        this.id = getNextNodeID();
        this.x = x;
        this.y = y;
        this.connections = new ArrayList<Node>();
        this.isNew = true;
    }

    public void addConnection(Node other) {
        this.connections.add(other);
    }

    public int getID() {
        return this.id;
    }

    public int getX() { return this.x; }

    public int getY() { return this.y; }

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
