package models.path;


import models.dir.Directory;
import models.dir.LocationType;
import sun.awt.image.ImageWatched;

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

    public Path addSteps(Path p){
        for(Node n : p.getPath()){
            this.addInOrder(n);
        }
        return this;
    }

    public LinkedList<Node> getPath() {
        return (LinkedList<Node>) this.path.clone();
    }

    public Node getStep(int i) {
        return this.path.get(i);
    }

    public String textPath() {
        if(this.path.size() < 2) return "You are already at your destination!";
        // Calculate the cardinal starting direction
        String str = "1. Start by leaving " + this.path.getFirst().getRoomName() + ".\n";
        Direction cur = Direction.dirFor(this.getStep(0), this.getStep(1));

        // Initialize the array that keeps track of attempts for each turn
        HashMap<String, Integer> attempts = new HashMap<String, Integer>();
        attempts.put("left", 0); attempts.put("right", 0); attempts.put("straight", 0); attempts.put("back", 0);
        int stepNum = 2;
        for (int i = 2; i < this.path.size(); i++) {
            if(this.getStep(i).getPrimaryLocType().equals(LocationType.Elevator) && this.getStep(i-1).getPrimaryLocType().equals(LocationType.Elevator)) {
                str += "Ride the elevator to the" + strForNum(this.getStep(i).getFloor()) + "floor.\n";
            }
            // Calculate the next cardinal turning direction
            Direction next = Direction.dirFor(this.getStep(i-1), this.getStep(i));

            // Turn the new direction into a relative direction (left, right, or straight)
            String result = cur.turnFor(next);
            if(result.equals("straight")) {
                // If just continuing straight, record any possible turns that weren't taken.
                // This is used to keep track for the "4th left" or "3rd right" message.
                int hallways = 0;
                for (Node conn : this.getStep(i-1).getConnections()) {
                    Direction connect = Direction.dirFor(this.getStep(i-1), conn);
                    String turn = cur.turnFor(connect);
                    if(conn.getRoomName().equals("NONE")) { // if it's a hallway
                        attempts.put(turn, attempts.get(turn) + 1);
                        hallways ++;
                    }
                }
                if(hallways > 2) {
                    str += stepNum + ". Go " + result + " through the intersection.\n";
                    stepNum ++;
                }
            }
            else {
                // If actually making a turn, add a message about it to the directions
                if(i+1 == this.path.size()) {
                    str += stepNum + ". Your destination (" + this.getEnd().getRoomName() + ") will be on your " + result + ".\n";
                }
                else {
                    str += stepNum + ". Make a " + result + ".\n";
                    //str += stepNum + ". Make the" + strForNum(attempts.get(result)+1) + result + ".\n";
                }
                stepNum ++;

                // Reset attempt counters every time a turn is made
                attempts.put("left", 0); attempts.put("right", 0); attempts.put("straight", 0); attempts.put("back", 0);
            }

            // Update current cardinal direction to the new one
            cur = next;
        }

        return str;
    }

    private String strForNum(int i) {
        // Assumes there will never be more than 20 turns options.
        switch(i) {
            case 1: return " 1st ";
            case 2: return " 2nd ";
            case 3: return " 3rd ";
            default: return " " + i + "th ";
        }
    }

    public String toString() {
        //System.out.println(this.textPath());
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