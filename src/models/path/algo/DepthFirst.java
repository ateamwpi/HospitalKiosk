package models.path.algo;

import core.KioskMain;
import core.exception.PathNotFoundException;
import models.path.Node;
import models.path.Path;

import java.util.HashMap;
import java.util.Stack;

/**
 * Created by Kevin O'Brien on 4/18/2017.
 */
public class DepthFirst implements IPathfindingAlgorithm {
    private HashMap<Node, Boolean> marked;
    private HashMap<Node, Node> edgeTo;
    private Node s;

    @Override
    public Path findPath(Node start, Node goal) throws PathNotFoundException {
        marked = new HashMap<>();
        edgeTo = new HashMap<>();
        s = start;
        dfs(s);

        return pathTo(goal);
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

    private Path pathTo(Node v) {
        Path path = new Path();
        if (!hasPathTo(v)) return path;
        for (Node x = v; x != s; x = edgeTo.get(x)) {
            path.buildPath(x);
        }
        path.buildPath(s);
        System.out.println(path);
        return path;
    }

    private boolean hasPathTo(Node v) {
        return marked.get(v);
    }

}
