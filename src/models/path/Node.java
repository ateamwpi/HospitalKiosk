package models.path;

import java.util.ArrayList;

/**
 * Created by mattm on 3/29/2017.
 */
public class Node {
    private int x;
    private int y;
    private final int id;
    private ArrayList<Node> connections;

    public Node(int id, int x, int y) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.connections = new ArrayList<Node>();
    }

    public void addConnection(Node other) {
        this.connections.add(other);
    }

    public int getID() {
        return this.id;
    }

    public String toString() {
        String str= "models.path.Node: ID=" + this.id + ", X=" + this.x + ", Y=" + this.y + "\n";
        for (Node n : this.connections) {
            str += "Connected to Node ID=" + n.getID() + "\n";
        }
        return str;
    }
}
