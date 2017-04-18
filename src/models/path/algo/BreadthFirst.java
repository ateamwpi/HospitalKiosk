package models.path.algo;

import core.exception.PathNotFoundException;
import models.path.Node;
import models.path.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by mattm on 4/18/2017
 */
public class BreadthFirst extends AbstractPathfindingAlgorithm {

    @Override
    public Path findPath(Node start, Node goal) throws PathNotFoundException {
        ArrayList<Node> s = new ArrayList<>();
        LinkedList<Node> q = new LinkedList<>();
        HashMap<Node, Node> parents = new HashMap<>();
        Node current;

        s.add(start);
        q.addLast(start);

        while (!q.isEmpty()) {
            current = q.removeFirst();
            if(current.equals(goal)) {
                return constructPath(parents, goal);
            }
            for (Node n : current.getConnections()) {
                if(!s.contains(n)) {
                    s.add(n);
                    parents.put(n, current);
                    q.addLast(n);
                }
            }
        }
        throw new PathNotFoundException(start, goal);
    }

    @Override
    public String getName() {
        return "Breadth-First Search";
    }
}
