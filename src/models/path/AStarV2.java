package models.path;

import java.util.*;

/**
 * Created by Jacob on 4/3/2017.
 */
public class AStarV2 {

    private static Path FindPath(Node start, Node goal){
        LinkedList<Node> closedSet;
        LinkedList<Node> openSet;
        HashMap<Node, Node> cameFrom;
        HashMap<Node, Double> gScore;
        HashMap<Node, Double> fScore;

        closedSet = new LinkedList<Node>();
        openSet = new LinkedList<Node>();

        fScore = new HashMap<Node, Double>();
        fScore.put(start, HeuristicCost(start, goal));

        gScore = new HashMap<Node, Double>();
        gScore.put(start, 0.0);

        cameFrom = new HashMap<Node, Node>();


        while(!openSet.isEmpty()){
            Map.Entry<Node, Double> min = Collections.min(fScore.entrySet(), new Comparator<Map.Entry<Node, Double>>() {
                public int compare(Map.Entry<Node, Double> entry1, Map.Entry<Node, Double> entry2) {
                    return entry1.getValue().compareTo(entry2.getValue());
                }

            });
            Node current = min.getKey();

            if(current.equals(goal)){
                return constructPath(cameFrom, current);
            }
            openSet.remove(current);
            closedSet.add(current);

            for(Node n: current.getConnections()){
                if(closedSet.contains(n)) continue;
                double score = gScore.get(current) + HeuristicCost(current, n);
                if(!openSet.contains(n)) openSet.add(n);
                else if(score >= gScore.get(n)) continue;

                cameFrom.put(n, current);
                gScore.put(n, score);
                fScore.put(n, score + HeuristicCost(n, goal));
            }
        }
        return null;
    }

    private static Path constructPath(HashMap<Node, Node> cameFrom, Node end) {
        Path p = new Path();
        p.addStep(end);
        Node current = end;
        while(cameFrom.keySet().contains(current)){
            current = cameFrom.get(current);
            p.addStep(current);
        }
        return p;
    }




    private static double HeuristicCost(Node start, Node goal){
       return Math.abs(start.getX() - goal.getX()) + Math.abs(start.getY() - goal.getY());
    }


}
