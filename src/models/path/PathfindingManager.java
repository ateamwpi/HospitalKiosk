package models.path;

import core.KioskMain;
import core.NodeInUseException;
import core.RoomNotFoundException;
import models.path.algo.AStar;
import models.path.algo.IPathfindingAlgorithm;

import java.util.HashMap;

/**
 * Created by mattm on 3/29/2017.
 */
public class PathfindingManager {

    private HashMap<Integer, Node> graph;
    private HashMap<String, Integer> ids;
    private IPathfindingAlgorithm astar;

    public PathfindingManager(HashMap<Integer, Node> allNodes) {
        this.graph = allNodes;
        this.ids = new HashMap<String, Integer>();
        for (Node n : this.graph.values()) {
            if(!n.getRoomName().equals("NONE")) this.ids.put(n.getRoomName(), n.getID());
        }
        this.astar = new AStar();
    }

    public Node getNode(int id) {
        return this.graph.get(id);
    }

    public void addNode(Node n) {
        if (n.isNew()) {
            KioskMain.getDB().addNode(n);
        }
        this.graph.put(n.getID(), n);
        this.ids.put(n.getRoomName(), n.getID());
    }

    public HashMap<Integer, Node> getGraph() {
        return this.graph;
    }

    public void removeNode(Node n) throws NodeInUseException{
        if(!n.getLocations().isEmpty()) {
            throw new NodeInUseException(n);
        }
        else {
            n.removeAllConnections();
            this.graph.remove(n.getID());
            this.ids.remove(n.getRoomName());
            KioskMain.getDB().removeNode(n);
        }
    }

    public Node getRoom(String roomName) throws RoomNotFoundException {
        if(!this.ids.containsKey(roomName)) {
            throw new RoomNotFoundException(roomName);
        }
        else return this.graph.get(this.ids.get(roomName));
    }

    public Path findPath(Node start, Node end) {
        return this.astar.findPath(start, end);
    }
}
