package models.path.algo;

import core.exception.PathNotFoundException;
import models.path.Node;
import models.path.Path;

import java.util.HashMap;

/**
 * Created by Kevin O'Brien on 4/18/2017.
 */
public class DepthFirst extends AbstractPathfindingAlgorithm {
    private HashMap<Node, Boolean> marked;
    private HashMap<Node, Node> edgeTo;

    @Override
    public Path findPath(Node start, Node goal) throws PathNotFoundException {
        marked = new HashMap<>();
        edgeTo = new HashMap<>();
        dfs(start);

        if(!hasPathTo(goal)) throw new PathNotFoundException(start, goal);
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

    private boolean hasPathTo(Node v) {
        return marked.containsKey(v) && marked.get(v);
    }
}
