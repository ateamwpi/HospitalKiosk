package models.path.algo;

import controllers.AbstractController;
import core.KioskMain;
import core.exception.PathNotFoundException;
import models.path.Node;
import models.path.Path;

import java.util.HashMap;
import java.util.Stack;

/**
 * Created by Kevin O'Brien on 4/18/2017.
 */
public class DepthFirst extends AbstractPathfindingAlgorithm {
    private HashMap<Node, Boolean> marked;
    private HashMap<Node, Node> edgeTo;
    private Node s;

    @Override
    public Path findPath(Node start, Node goal) throws PathNotFoundException {
        marked = new HashMap<>();
        edgeTo = new HashMap<>();
        s = start;
        dfs(s);

        return constructPath(edgeTo, goal);
    }

    @Override
    public String getName() {
        return "Depth-First Search";
    }

    private void dfs(Node v) {
        marked.put(v, true);
        for(Node w : v.getConnections()) {
            if (!marked.containsKey(w)) {
                edgeTo.put(w, v);
                dfs(w);
            }
        }
    }
}
