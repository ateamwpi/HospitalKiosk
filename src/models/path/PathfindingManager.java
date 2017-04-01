package models.path;

import core.KioskMain;
import core.NodeInUseException;

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

    public void addNode(Node n) {
        if (n.isNew()) {
            KioskMain.getDB().addNode(n);
        }
        this.graph.put(n.getID(), n);
    }

    public HashMap<Integer, Node> getGraph() {
        return this.graph;
    }

    public void removeNode(Node n) throws NodeInUseException{
        if(!n.getLocations().isEmpty()) {
            throw new NodeInUseException(n);
        }
        else {
            for (Node other : n.getConnections()) {
                other.removeConnection(n);
            }
            this.graph.remove(n.getID());
            KioskMain.getDB().removeNode(n);
        }
    }

}

public class AStar {
    public static final int DIAGONAL_COST; //Set equal to some int value
    public static final int V_H_COST; //Vertical and horizontal cost
//set equal to some int value


    static Node[][] grid = new Node[5][5]; //5 is an arbitrary value, change as necessary

    static PriorityQueue<Node> open;

    static boolean closed[][];
    static int startX, startY;
    static int endX, endY;

    public static void setBlocked(int x, int y) {
        grid[x][y] = null;
    }

    public static void setStarterNode(int x, int y) {
        startX = x;
        startY = y;
    }

    public static void setEndNode(int x, int y) {
        endX = x;
        endY = y;
    }

    static void checkAndUpdateCost(Node current, Node t, int cost) {
        if (t == null || closed[t.x][t.y]) return;
        int t_final_cost = t.heuristicCost + cost;

        boolean inOpen = open.contains(t);
        if (!inOpen || t_final_cost < t.finalCost) {
            t.finalCost = t_final_cost;
            t.parent = current;
        }
    }

    public static void AStar() {

        //add the start location to open list.
        open.add(grid[startX][startY]);

        Node current;

        while (true) {
            current = open.poll();
            if (current == null) break;
            close[current.x][current.y] = true;


            if (current.equals(grid[endI][endJ])) {
                return;
            }
            Node t;
            if (current.x - 1 >= 0) {
                t = grid[current.x - 1][current.y];
                checkAndUpdateCost(current, t, current.finalCost + V_H_COST);

                if (current.y - 1 >= 0) {
                    t = grid[current.x - 1][current.y - 1];
                    checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
                }

                if (current.y + 1 < grid[0].length) {
                    t = grid[current.x - 1][current.y + 1];
                    checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
                }
            }
            if (current.y - 1 >= 0) {
                t = grid[current.x][current.y - 1];
                checkAndUpdateCost(current, t, current.finalCost + V_H_COST);
            }

            if (current.y + 1 < grid[0].length) {
                t = grid[current.x][current.y + 1];
                checkAndUpdateCost(current, t, current.finalCost + V_H_COST);
            }

            if (current.x + 1 < grid.length) {
                t = grid[current.x + 1][current.y];
                checkAndUpdateCost(current, t, current.finalCost + V_H_COST);

                if (current.y - 1 >= 0) {
                    t = grid[current.x + 1][current.y - 1];
                    checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
                }

                if (current.y + 1 < grid[0].length) {
                    t = grid[current.x + 1][current.y + 1];
                    checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
                }

            }
        }
    }
}
