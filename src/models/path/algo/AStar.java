package models.path.algo;

import models.path.Node;
import models.path.Path;

import java.util.*;

/**
 * Created by Jacob on 4/3/2017.
 */
public class AStar implements IPathfindingAlgorithm {

    public Path findPath(Node start, Node goal){
        LinkedList<Node> closedSet;
        LinkedList<Node> openSet;
        HashMap<Node, Node> cameFrom;
        HashMap<Node, Double> gScore;
        HashMap<Node, Double> fScore;

        closedSet = new LinkedList<Node>();
        openSet = new LinkedList<Node>();
        openSet.add(start);

        fScore = new HashMap<Node, Double>();
        fScore.put(start, this.heuristicCost(start, goal));

        gScore = new HashMap<Node, Double>();
        gScore.put(start, 0.0);

        cameFrom = new HashMap<Node, Node>();


        while(!openSet.isEmpty()){
            Node current = openSet.getFirst();
            for (Node n : openSet) {
                if (fScore.get(n) < fScore.get(current)) {
                    current = n;
                }
            }

            if(current.equals(goal)){
                return this.constructPath(cameFrom, current);
            }
            openSet.remove(current);
            closedSet.add(current);

            for(Node n: current.getConnections()){
                if(closedSet.contains(n)) continue;
                double score = gScore.get(current) + this.heuristicCost(current, n);
                if(!openSet.contains(n)) openSet.add(n);
                else if(score >= gScore.get(n)) continue;

                cameFrom.put(n, current);
                gScore.put(n, score);
                fScore.put(n, score + this.heuristicCost(n, goal));
            }
        }
        return null;
    }

    private Path constructPath(HashMap<Node, Node> cameFrom, Node end) {
        Path p = new Path();
        p.buildPath(end);
        Node current = end;
        while(cameFrom.keySet().contains(current)){
            current = cameFrom.get(current);
            p.buildPath(current);
        }
        return p;
    }

    private double heuristicCost(Node start, Node goal){
       return Math.abs(start.getX() - goal.getX()) + Math.abs(start.getY() - goal.getY());
    }


}
