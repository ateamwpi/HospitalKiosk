package models.path;

import core.KioskMain;
import core.NodeInUseException;

import java.util.HashMap;

/**
 * Created by mattm on 3/29/2017.
 */
public class PathfindingManager {

    private HashMap<Integer, Node> graph;

    public PathfindingManager(HashMap<Integer, Node> allNodes) {
        this.graph = allNodes;
    }

    public Node getNode(int id) {
        return this.graph.get(id);
    }

    public void addNode(Node n) {
        if (n.isNew()) {
            KioskMain.getDB().addNode(n);
        }
        this.graph.put(n.getID(), n);
    }

    public HashMap<Integer, Node> getGraph() {
        return this.graph;
    }

    public void removeNode(Node n) throws NodeInUseException{
        if(!n.getLocations().isEmpty()) {
            throw new NodeInUseException(n);
        }
        else {
            for (Node other : n.getConnections()) {
                other.removeConnection(n);
            }
            this.graph.remove(n.getID());
            KioskMain.getDB().removeNode(n);
        }
    }

}
