package models.path.algo;

import core.exception.PathNotFoundException;
import models.path.Node;
import models.path.Path;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Jacob on 4/3/2017.
 */
public class AStar extends AbstractPathfindingAlgorithm {

    public Path findPath(Node start, Node goal) throws PathNotFoundException {
        LinkedList<Node> closedSet;
        LinkedList<Node> openSet;
        HashMap<Node, Node> cameFrom;
        HashMap<Node, Double> gScore;
        HashMap<Node, Double> fScore;

        closedSet = new LinkedList<>();
        openSet = new LinkedList<>();
        openSet.add(start);

        fScore = new HashMap<>();
        fScore.put(start, heuristicCost(start, goal));

        gScore = new HashMap<>();
        gScore.put(start, 0.0);

        cameFrom = new HashMap<>();


        while(!openSet.isEmpty()){
            Node current = openSet.getFirst();
            for (Node n : openSet) {
                if (fScore.get(n) < fScore.get(current)) {
                    current = n;
                }
            }

            if(current.equals(goal)){
                return constructPath(cameFrom, current);
            }
            openSet.remove(current);
            closedSet.add(current);

            for(Node n: current.getConnections()){
                if(closedSet.contains(n)) continue;
                double score = gScore.get(current) + heuristicCost(current, n);
                if(!openSet.contains(n)) openSet.add(n);
                else if(score >= gScore.get(n)) continue;

                cameFrom.put(n, current);
                gScore.put(n, score);
                fScore.put(n, score + heuristicCost(n, goal));
            }
        }
        throw new PathNotFoundException(start, goal);
    }

    @Override
    public String getName() {
        return "A* Search";
    }

    private double heuristicCost(Node start, Node goal){
       return Math.abs(start.getX() - goal.getX())
               + Math.abs(start.getY() - goal.getY())
               + Math.abs(start.getFloor() - goal.getFloor());
    }


}
