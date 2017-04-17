package models.path;

import core.KioskMain;
import core.NodeInUseException;
import core.RoomNotFoundException;
import models.dir.Location;
import models.dir.LocationType;
import models.path.algo.AStar;
import models.path.algo.IPathfindingAlgorithm;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Created by mattm on 3/29/2017.
 */
public class PathfindingManager {

    private HashMap<Integer, Node> graph;
    private HashMap<String, Integer> ids;
    private IPathfindingAlgorithm astar;

    public PathfindingManager(HashMap<Integer, Node> allNodes) {
        this.graph = allNodes;
        this.ids = new HashMap<String, Integer>();
        for (Node n : this.graph.values()) {
            if(n.getRoomName() != null && !n.getRoomName().equals("NONE")) this.ids.put(n.getRoomName(), n.getID());
        }
        this.astar = new AStar();
    }

    public Node getNode(int id) {
        return this.graph.get(id);
    }

    public void updateRoomName(Node n, String oldName) {
        this.ids.remove(oldName);
        this.ids.put(n.getRoomName(), n.getID());
    }

    public void addNode(Node n) {
        if (n.isNew()) {
            KioskMain.getDB().addNode(n);
        }
        this.graph.put(n.getID(), n);
        this.ids.put(n.getRoomName(), n.getID());
    }

    public HashMap<Integer, Node> getGraph() {
        return this.graph;
    }

    public void removeNode(Node n) throws NodeInUseException{
        if(!n.getLocations().isEmpty()) {
            throw new NodeInUseException(n);
        }
        else {
            n.removeAllConnections();
            this.graph.remove(n.getID());
            this.ids.remove(n.getRoomName());
            KioskMain.getDB().removeNode(n);
        }
    }

    private Node findMatching(Node cur, int floor, LocationType locType) {
        Collection<Location> locations = KioskMain.getDir().getDirectory(locType).getLocations().values();
        Node other;
        for (Location l : locations) {
            other = l.getNode();
            if(other.getFloor() == floor && cur.getX() == other.getX() && cur.getY() == other.getY()) return other;
        }
        return null;
    }

    public HashMap<Location, Double> getNearest(LocationType loc, Node start){
        HashMap<Location, Double> nearests = new HashMap<Location, Double>();
        Collection<Location> locations = KioskMain.getDir().getDirectory(loc).getLocations().values();

        for(Location l : locations){
            if(l.getNode().getFloor() == start.getFloor()){
                nearests.put(l, distanceFormula(l.getNode(), start));
//                if(currentShortest == null || distanceFormula(l.getNode(), start) < distanceFormula(currentShortest.getNode(), start)) {
//                    currentShortest = l;
//                }
            }
        }
        System.out.println(nearests);
        return nearests;
    }

    private double distanceFormula(Node end, Node start){

        double endX = (double)end.getX();
        double endY = (double)end.getY();

        double startX = (double)start.getX();
        double startY = (double)start.getY();

        double distX = endX - startX;
        double distY = endY - startY;

        double distance = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));

        return distance;
    }

    public Node getRoom(String roomName) throws RoomNotFoundException {
        if(!this.ids.containsKey(roomName)) {
            throw new RoomNotFoundException(roomName);
        }
        else return this.graph.get(this.ids.get(roomName));
    }


    public Path findPath(Node start, Node end) {

        if(start.getFloor() != end.getFloor()){
            //Node elevator = getNearest(LocationType.Elevator, start).getNode();
            //System.out.println("start=" + start + " elevator=" + elevator);
            HashMap<Location, Double> nearests = getNearest(LocationType.Elevator, start);
            Node curr;
            Node matching;
            do {
                Location min = Collections.min(nearests.entrySet(), (entry1, entry2) -> (int)entry1.getValue().doubleValue() - (int)entry2.getValue().doubleValue()).getKey();
                curr = min.getNode();
                matching = findMatching(curr, end.getFloor(), LocationType.Elevator);
                nearests.remove(min);
            } while(matching == null);
            System.out.println("curr=" + curr + " matching=" + matching);
            Path startFloor = this.astar.findPath(start, curr);
            //System.out.println(findMatching(elevator, end.getFloor(), LocationType.Elevator));
            Path endFloor = this.astar.findPath(matching, end);
            System.out.println("startFloor: " + startFloor);
            System.out.println("endFloor: " + endFloor);
            return startFloor.addSteps(endFloor);
        }
        else{
            return this.astar.findPath(start, end);
        }
    }

    public Collection<String> getRoomNames() {
        ArrayList<String> roomNames = new ArrayList<String>(this.ids.keySet());
        Collections.sort(roomNames);
        return roomNames;
    }
}
