package models.path;

import java.util.*;

/**
 * Created by Jacob on 4/1/2017.
 */
public class AStar {
    private static final int DIAGONAL_COST = 14; //Set equal to some int value
    private static final int V_H_COST = 10; //Vertical and horizontal cost
//set equal to some int value


    private static Node[][] grid = new Node[5][5]; //5 is an arbitrary value, change as necessary

    private static PriorityQueue<Node> open;

    private static boolean closed[][];
    private static int startX, startY;
    private static int endX, endY;

    private static void setBlocked(int x, int y) {
        grid[x][y] = null;
    }

    private static void setStartNode(int x, int y) {
        startX = x;
        startY = y;
    }

    private static void setEndNode(int x, int y) {
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

    private static void AStar() {

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

    /*
    Params :
    tCase = test case No.
    X, Y = Board's dimensions
    sx, sy = start location's x and y coordinates
    ex, ey = end location's x and y coordinates
    int[][] blocked = array containing inaccessible node coordinates
    */

    private static void test(int tCase,
                            int X,
                            int Y,
                            int sx,
                            int sy,
                            int ex,
                            int ey,
                            int[][] blocked) {
        System.out.println("\n\nTest Case #"+tCase);
        //Reset
        grid = new Node[X][Y];
        closed = new boolean[X][Y];
        open = new PriorityQueue<>((Object o1, Object o2) -> {
            Node n1 = (Node)o1;
            Node n2 = (Node)o2;

            return n1.finalCost < n2.finalCost?-1:
                    n1.finalCost > n2.finalCost?1:0;

        });
        //Set start position
        setStartNode(sx, sy); //Setting to coordinate 0,0 as default for UI test purposes

        //Set end position
        setEndNode(ex, ey);

        for (int x = 0; x < X; ++x) {
            for (int y = 0; y < Y; ++y) {
                grid[x][y] = new Node(x, y);
                grid[x][y].heuristicCost = Math.abs(x - endX) + Math.abs(y - endY);
                //             System.out.print(grid[x][y].heuristicCost + " ");
            }
            //        System.out.println();
        }
        grid[sx][sy].finalCost = 0;

        /*
        Set blocked nodes. Simply set the nodes values to null
        for blocked nodes.
        */
        for (int x = 0; x < blocked.length; ++x) {
            setBlocked(blocked[x][0], blocked[x][1]);
        }

        //Display test map
        System.out.println("Grid: ");
        for(int x = 0; x < X ; ++x){
            for(int y = 0; y < Y; ++y){
                if(x == sx && y == sy)System.out.print("SO  "); //Source
                else if(x == ex && y == ey)System.out.print("DE  ");  //Destination
                else if(grid[x][y] != null)System.out.printf("%-3d ", 0);
                else System.out.print("BL  ");
            }
            System.out.println();
        }
        System.out.println();

        AStar();

        System.out.println("\nScores for cells: ");
        for(int x = 0; x < X ; ++x){
            for(int y = 0; y < Y; ++y){
                if(grid[x][y] != null)System.out.printf("%-3d ", grid[x][y].finalCost);
                else System.out.print("BL  ");
            }
            System.out.println();
        }
        System.out.println();

        if(closed[endX][endY]){
            //Trace back the path
            System.out.println("Path: ");
            Node current = grid[endX][endY];
            System.out.print(current);    //original version
            //System.out.print("["+current.getX()+", "+current.getY()+"]"); //modified version


            while(current.parent != null){
                System.out.print(" -> "+current.parent);
                current = current.parent;

            /*
            while(current.parent != null){
                System.out.print(" -> "+ "["+current.getX()+", "+current.getY()+"]");
                current = current.parent;
                */

            }

            System.out.println();
        }else System.out.println("No possible path");
    }

    public static void main(String[] args) throws Exception{
        //test(5, 7,7,3,3,3,3, new int[][]{});
        test(1, 5, 5, 0, 0, 3, 2, new int[][]{{0,4},{2,2},{3,1},{3,3}});
        //test(2, 5, 5, 0, 0, 4, 4, new int[][]{{0,4},{2,2},{3,1},{3,3}});
        //test(3, 7, 7, 2, 1, 5, 4, new int[][]{{4,1},{4,3},{5,3},{2,3}});
        //test(4, 5, 5, 0, 0, 4, 4, new int[][]{{3,4},{3,3},{4,3}});
    }
}




