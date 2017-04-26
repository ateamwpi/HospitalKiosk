package models.path.algo;

import core.exception.PathNotFoundException;
import models.path.Node;
import models.path.Path;

import java.util.HashMap;

/**
 * Created by mattm on 4/3/2017.
 */
public abstract class   AbstractPathfindingAlgorithm {

    public abstract Path findPath(Node start, Node goal) throws PathNotFoundException;

    public abstract String getName();

    protected Path constructPath(HashMap<Node, Node> cameFrom, Node end) {
        Path p = new Path();
        p.buildPath(end);
        Node current = end;


        while(cameFrom.keySet().contains(current)){
            current = cameFrom.get(current);
            p.buildPath(current);
        }
        return p;
    }
}
