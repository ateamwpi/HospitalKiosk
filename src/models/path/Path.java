package models.path;


import models.dir.Directory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by mattm on 3/29/2017.
 */
public class Path {
    private LinkedList<Node> path;

    public Path(){
        this.path = new LinkedList<>();
    }

    /** Adds nodes to the path in order, first to last. **/
    public void addInOrder(Node n) {
        this.path.addLast(n);
    }

    /** Builds the path in reverse order, last to first. **/
    public void buildPath(Node n){
        this.path.addFirst(n);
    }

    public LinkedList<Node> getPath() {
        return this.path;
    }

    public Node getStep(int i) {
        return this.path.get(i);
    }

    public String textPath() {
        String str = "1. Start by leaving " + this.path.getFirst().getRoomName() + ".\n";
        Direction cur = Direction.dirFor(this.getStep(0), this.getStep(1));
        HashMap<String, Integer> attempts = new HashMap<String, Integer>();
        attempts.put("left", 0); attempts.put("right", 0); attempts.put("straight", 0); attempts.put("back", 0);
        int stepNum = 2;
        for (int i = 2; i < this.path.size(); i++) {
            System.out.println(this.getStep(i).getID() + " " + attempts);
            Direction next = Direction.dirFor(this.getStep(i-1), this.getStep(i));
            String result = cur.turnFor(next);
            if(result.equals("straight")) {
                System.out.println(this.getStep(i-1).getID());
                for (Node conn : this.getStep(i-1).getConnections()) {
                    Direction connect = Direction.dirFor(this.getStep(i-1), conn);
                    String turn = cur.turnFor(connect);
                    System.out.println(turn);
                    attempts.put(turn, attempts.get(turn)+1);
                }
            }
            else if(i+1==this.path.size())
                str += stepNum + ". Make the" + strForNum(attempts.get(result)+1) + result + ", into " + this.getEnd().getRoomName() + ".\n";
            else {
                str += stepNum + ". Make the" + strForNum(attempts.get(result)+1) + result + ".\n";
                stepNum ++;
                attempts.put("left", 0);
                attempts.put("right", 0);
            }
            cur = next;
        }

        return str;
    }

    private String strForNum(int i) {
        switch(i) {
            case 1: return " 1st ";
            case 2: return " 2nd ";
            case 3: return " 3rd ";
            default: return " " + i + "th ";
        }
    }

    public String toString() {
        System.out.println(this.textPath());
        String str = "Path: ";
        for (Node n : this.path) {
            str += n.getID() + ", ";
        }
        return str;
    }

    public Node getStart() {
        return this.path.getFirst();
    }

    public Node getEnd() {
        return this.path.getLast();
    }

    @Override
    public boolean equals(Object o) {
        Path p = (Path)o;
        if(this.path.size() != p.path.size()) return false;
        for (int i = 0; i < this.path.size(); i++) {
            if(this.path.get(i).getID() != p.path.get(i).getID()) return false;
        }
        return true;
    }
}