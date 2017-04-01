package models.path;

import java.util.*;

/**
 * Created by Jacob on 4/1/2017.
 */
public class AStar {
    private static final int DIAGONAL_COST = 20; //Set equal to some int value
    private static final int V_H_COST = 20; //Vertical and horizontal cost
//set equal to some int value


    private static Node[][] grid = new Node[5][5]; //5 is an arbitrary value, change as necessary

    private static PriorityQueue<Node> open;

    private static boolean closed[][];
    private static int startX, startY;
    private static int endX, endY;

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

    private static void checkAndUpdateCost(Node current, Node t, int cost) {
        if (t == null || closed[t.getX()][t.getY()]) return;
        int t_final_cost = t.heuristicCost + cost;

        boolean inOpen = open.contains(t);
        if (!inOpen || t_final_cost < t.finalCost) {
            t.finalCost = t_final_cost;
            t.parent = current;
        }
    }

     static void AStar() {

        //add the start location to open list.
        open.add(grid[startX][startY]);

        Node current;

        while (true) {
            current = open.poll();
            if (current == null) break;
            closed[current.getX()][current.getY()] = true;


            if (current.equals(grid[endX][endY])) {
                return;
            }
            Node t;
            if (current.getX() - 1 >= 0) {
                t = grid[current.getX() - 1][current.getY()];
                checkAndUpdateCost(current, t, current.finalCost + V_H_COST);

                if (current.getY() - 1 >= 0) {
                    t = grid[current.getX() - 1][current.getY() - 1];
                    checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
                }

                if (current.getY() + 1 < grid[0].length) {
                    t = grid[current.getX() - 1][current.getY() + 1];
                    checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
                }
            }
            if (current.getY() - 1 >= 0) {
                t = grid[current.getX()][current.getY() - 1];
                checkAndUpdateCost(current, t, current.finalCost + V_H_COST);
            }

            if (current.getY() + 1 < grid[0].length) {
                t = grid[current.getX()][current.getY() + 1];
                checkAndUpdateCost(current, t, current.finalCost + V_H_COST);
            }

            if (current.getX() + 1 < grid.length) {
                t = grid[current.getX() + 1][current.getY()];
                checkAndUpdateCost(current, t, current.finalCost + V_H_COST);

                if (current.getY() - 1 >= 0) {
                    t = grid[current.getX() + 1][current.getY() - 1];
                    checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
                }

                if (current.getY() + 1 < grid[0].length) {
                    t = grid[current.getX() + 1][current.getY() + 1];
                    checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
                }

            }
        }
    }
}