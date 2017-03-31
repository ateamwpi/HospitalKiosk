package models.path;

import java.util.ArrayList;
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

}
