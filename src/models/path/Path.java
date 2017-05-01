package models.path;


import controllers.MapView.Map.MapController;
import controllers.MapView.MapView.DirectionsDrawer.DirectionStep;
import core.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import static controllers.MapView.MapView.DirectionsDrawer.DirectionStep.DirectionIcon;

/**
 * Created by mattm on 3/29/2017.
 * nuggies
 */
public class Path {
    private final LinkedList<Node> path;
    private ArrayList<DirectionStep> steps;

    public Path(){
        path = new LinkedList<>();
    }

    /** Adds nodes to the path in order, first to last. **/
    public void addInOrder(Node n) {
        path.addLast(n);
    }

    /** Builds the path in reverse order, last to first. **/
    public void buildPath(Node n){
        path.addFirst(n);
    }

    Path addSteps(Path p){
        for(Node n : p.getPath()){
            addInOrder(n);
        }
        return this;
    }

    public LinkedList<Node> getPath() {
        //noinspection unchecked
        return (LinkedList<Node>) path.clone();
    }

    public Node getStep(int i) {
        return path.get(i);
    }

    public String textPath() {
        steps = new ArrayList<>();
        if(path.size() < 2) {
            steps.add(new DirectionStep(DirectionIcon.STRAIGHT, "You are already at your destination!"));
            return "You are already at your destination!";
        }
        // Calculate the cardinal starting direction
        StringBuilder str = new StringBuilder("1. Start by leaving " + path.getFirst().getRoomName() + ".\n");
        steps.add(new DirectionStep(DirectionIcon.STRAIGHT, "Leave " + path.getFirst().getRoomName()));
        Direction cur = Direction.dirFor(getStep(0), getStep(1));

        // Initialize the array that keeps track of attempts for each turn
        HashMap<String, Integer> attempts = new HashMap<>();
        attempts.put("left", 0); attempts.put("right", 0); attempts.put("straight", 0); attempts.put("back", 0);
        int stepNum = 2;
        boolean waiting = false;
        for (int i = 2; i < path.size(); i++) {
            if(getStep(i-1).equals(getStep(i))) continue;

            if(getStep(i-1).isBelkin() && !getStep(i).isBelkin()) {
                // leaving belkin
                str.append(stepNum).append(". Leave the Belkin House and walk across the parking lot.\n");
                steps.add(new DirectionStep(DirectionIcon.STRAIGHT, "Leave the Belkin House"));
                stepNum ++;
                waiting = true;
            }
            else if(getStep(i-1).isMain() && !getStep(i).isMain()) {
                // leaving main
                str.append(stepNum).append(". Leave the main building and walk across the parking lot.\n");
                steps.add(new DirectionStep(DirectionIcon.STRAIGHT, "Leave the main building"));
                stepNum ++;
                waiting = true;
            }
            else if(!getStep(i-1).isBelkin() && getStep(i).isBelkin()) {
                // entering belkin
                str.append(stepNum).append(". Enter the Belkin House.\n");
                steps.add(new DirectionStep(DirectionIcon.STRAIGHT, "Enter the Belkin House"));
                stepNum ++;
                waiting = false;
                continue;
            }
            else if(!getStep(i-1).isMain() && getStep(i).isMain()) {
                // entering main
                str.append(stepNum).append(". Enter the main building.\n");
                steps.add(new DirectionStep(DirectionIcon.STRAIGHT, "Enter the main building"));
                stepNum ++;
                waiting = false;
                continue;
            }

            if(waiting) continue;

            if(!getStep(i).getNodeType().equals(NodeType.Elevator) && getStep(i-1).getNodeType().equals(NodeType.Elevator)) {
                str.append(stepNum).append(". Ride the elevator to Floor ").append(MapController.getAllFloors().get(getStep(i).getFloor()-1)).append(" and exit.\n");
                steps.add(new DirectionStep(DirectionIcon.STRAIGHT, "Ride the elevator to Floor " + MapController.getAllFloors().get(getStep(i).getFloor()-1) + ""));
                stepNum ++;
            }
            // Calculate the next cardinal turning direction
            Direction next = Direction.dirFor(getStep(i-1), getStep(i));

            // Turn the new direction into a relative direction (left, right, or straight)
            assert cur != null;
            String result = cur.turnFor(next);
            if(result.equals("straight")) {
                // If just continuing straight, record any possible turns that weren't taken.
                // This is used to keep track for the "4th left" or "3rd right" message.
                int hallways = 0;
                for (Node conn : getStep(i-1).getConnections()) {
                    Direction connect = Direction.dirFor(getStep(i-1), conn);
                    String turn = cur.turnFor(connect);
                    if(conn.getRoomName().equals("NONE")) { // if it's a hallway
                        attempts.put(turn, attempts.get(turn) + 1);
                        hallways ++;
                    }
                }
                if(hallways > 2) {
                    str.append(stepNum).append(". Go ").append(result).append(" through the intersection.\n");
                    steps.add(new DirectionStep(DirectionIcon.forString(result), "Go " + result + " through the intersection."));
                    stepNum ++;
                }
            }
            else {
                // If actually making a turn, add a message about it to the directions
                if(i+1 == path.size()) {
                    str.append(stepNum).append(". Your destination (").append(getEnd().getRoomName()).append(") will be on your ").append(result).append(".\n");
                    steps.add(new DirectionStep(DirectionIcon.forString(result), "Your destination will be on your " + result));
                    stepNum ++;
                }
                else {
                    if(!getStep(i-1).getNodeType().equals(NodeType.Elevator)) {
                        str.append(stepNum).append(". Make a ").append(result);
                        if (getStep(i).getNodeType().equals(NodeType.Elevator))
                            str.append(" into the ").append(getStep(i).getRoomName()).append(".\n");
                        else str.append(".\n");
                        stepNum ++;
                        steps.add(new DirectionStep(DirectionIcon.forString(result), "Make a " + result + ((getStep(i).getNodeType().equals(NodeType.Elevator)) ? (" into the " + getStep(i).getRoomName()) : "")));
                    }
                }
                // Reset attempt counters every time a turn is made
                attempts.put("left", 0); attempts.put("right", 0); attempts.put("straight", 0); attempts.put("back", 0);
            }

            // Update current cardinal direction to the new one
            cur = next;
        }

        return str.toString();
    }

    public String toString() {
        //System.out.println(textPath());
        StringBuilder str = new StringBuilder("Path: ");
        for (Node n : path) {
            str.append(n.getID()).append(", ");
        }
        return str.toString();
    }

    public Node getStart() {
        return path.getFirst();
    }

    public Node getEnd() {
        return path.getLast();
    }

    public ArrayList<String> getFloorsSpanning() {
        ArrayList<String> results = new ArrayList<>();

        for (Node n : path) {
            String floor = MapController.getAllFloors().get(n.getFloor()-1);
            if(!n.getNodeType().equals(NodeType.Elevator) && !results.contains(floor)) results.add(floor);
        }

        return results;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Path) {
            Path other = (Path) o;
            if(path.size() != other.path.size()) {
                return false;
            }
            for (int i = 0; i < path.size(); i++) {
                if(path.get(i).getID() != other.path.get(i).getID()) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public Collection<DirectionStep> getDirections() {
        if(steps == null) textPath();
        System.out.println(steps);
        return steps;
    }

}