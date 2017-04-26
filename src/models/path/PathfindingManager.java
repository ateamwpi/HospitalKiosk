package models.path;

import core.KioskMain;
import core.exception.*;
import models.dir.Location;
import models.dir.LocationType;
import models.path.algo.AStar;
import models.path.algo.AbstractPathfindingAlgorithm;
import models.path.algo.BreadthFirst;
import models.path.algo.DepthFirst;

import java.util.*;

/**
 * Created by mattm on 3/29/2017.
 */
public class PathfindingManager {

    private HashMap<Integer, Node> graph;
    private HashMap<String, Integer> ids;
    private ArrayList<AbstractPathfindingAlgorithm> algorithms = new ArrayList<AbstractPathfindingAlgorithm>();
    private AbstractPathfindingAlgorithm cur;

    public PathfindingManager(HashMap<Integer, Node> allNodes) {
        this.graph = allNodes;
        this.ids = new HashMap<String, Integer>();
        for (Node n : this.graph.values()) {
            if(n.getRoomName() != null && !n.getRoomName().equals("NONE")) this.ids.put(n.getRoomName(), n.getID());
        }
        this.algorithms.add(new AStar());
        this.algorithms.add(new BreadthFirst());
        this.algorithms.add(new DepthFirst());
        this.selectAlgorithm("A* Search");
        System.out.println("Loaded algorithms " + this.getAlgorithms());
        System.out.println("Selected algorithm: " + this.getSelectedAlgorithm().getName());
    }

    public Node getNode(int id) {
        return this.graph.get(id);
    }

    public void updateRoomName(Node n, String oldName) {
        this.ids.remove(oldName);
        this.ids.put(n.getRoomName(), n.getID());
    }

    public boolean selectAlgorithm(String name) {
        for(AbstractPathfindingAlgorithm algo : this.algorithms) {
            if(name.equals(algo.getName())) {
                this.cur = algo;
                return true;
            }
        }
        return false;
    }

    public AbstractPathfindingAlgorithm getSelectedAlgorithm() {
        return this.cur;
    }

    public ArrayList<String> getAlgorithms() {
        ArrayList<String> names = new ArrayList<>();

        for(AbstractPathfindingAlgorithm algo : this.algorithms) {
            names.add(algo.getName());
        }

        return names;
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

    public HashMap<Location, Double> getNearest(LocationType loc, Node start) throws NearestNotFoundException {
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
        if(nearests.size() == 0) {
            throw new NearestNotFoundException(loc, start.getFloor());
        }
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

    public int distanceInFeet(Node end, Node start){
        double inPixels;
        double inFeet;
        double inchesPerPixel = 4.068;
        int inFeetRounded;
        inPixels = distanceFormula(end, start);
        inFeet = ((inPixels * inchesPerPixel) / 12);

        inFeetRounded = (int) (inFeet + 0.5);

        return inFeetRounded;
    }

    public int totalDistance(Node end, Node start) throws PathNotFoundException, NearestNotFoundException, FloorNotReachableException {

        int distance = 0;
        Path path = findPath(start, end);
        int i = 0;

        while(true){
            if (path.getPath().get(i).equals(end)) {
                break;
            }else{
                distance = distance + distanceInFeet(path.getPath().get(i+1), path.getPath().get(i));
            }
            i++;
        }
        return distance;
    }

    public int timeInSeconds(Node end, Node start) throws PathNotFoundException, NearestNotFoundException, FloorNotReachableException {

        int timeTaken = 0;
        Path path = findPath(start, end);
        double distance = (double)totalDistance(end, start);
        int count = 0;
        int minimumElevatorTime = 20;
        double averagePace = 4.54667;
        int floorDifference = 0;
        int elevatorTime = 14;
        for(Node n: path.getPath()){
            if(n.getPrimaryLocType().equals(LocationType.Elevator)){
                count++;
            }
        }

        for(int i = 0; i <= path.getPath().size(); i++){
            if(path.getPath().get(i+1).getFloor() != path.getPath().get(i).getFloor(){
                floorDifference += Math.abs(path.getPath().get(i+1).getFloor() - path.getPath().get(i).getFloor());
            }
        }

        timeTaken = (int)((distance/averagePace)+ ((count/2)*minimumElevatorTime)+ (floorDifference*elevatorTime));

        return timeTaken;
    }

    public Node getRoom(String roomName) throws RoomNotFoundException {
        if(!this.ids.containsKey(roomName)) {
            throw new RoomNotFoundException(roomName);
        }
        else return this.graph.get(this.ids.get(roomName));
    }

    public boolean hasRoomName(String name) {
        return this.ids.containsKey(name);
    }

    public Path findPath(Node start, Node end) throws PathNotFoundException, NearestNotFoundException, FloorNotReachableException {
        if(start.isBelkin() == end.isBelkin()) {
            return this.findSameBuilding(start, end);
        }
        else {
            return this.findDifferentBuilding(start, end);
        }
    }

    private Path findDifferentBuilding(Node start, Node end) throws PathNotFoundException, NearestNotFoundException, FloorNotReachableException {
        Node startBuild;
        Node endBuild;
        if(start.isBelkin()) {
            startBuild = KioskMain.getDir().getBelkinEntr().getNode();
            endBuild = KioskMain.getDir().getMainEntr().getNode();
        }
        else {
            startBuild = KioskMain.getDir().getMainEntr().getNode();
            endBuild = KioskMain.getDir().getBelkinEntr().getNode();
        }
        Path build1 = this.findSameBuilding(start, startBuild);
        Path crossLot = this.cur.findPath(startBuild, endBuild);
        Path build2 = this.findSameBuilding(endBuild, end);
        Path path = build1.addSteps(crossLot).addSteps(build2);
        return build1.addSteps(crossLot).addSteps(build2);
    }

    private Path findSameBuilding(Node start, Node end) throws PathNotFoundException, NearestNotFoundException, FloorNotReachableException {
        if (start.getFloor() != end.getFloor()) {
            HashMap<Location, Double> nearests = getNearest(LocationType.Elevator, start);
            Node curr;
            Node matching;
            Location min;
            do {
                try {
                    min = Collections.min(nearests.entrySet(), (entry1, entry2) -> (int) entry1.getValue().doubleValue() - (int) entry2.getValue().doubleValue()).getKey();
                } catch (NoSuchElementException e) {
                    throw new FloorNotReachableException(start, end.getFloor());
                }
                curr = min.getNode();
                matching = findMatching(curr, end.getFloor(), LocationType.Elevator);
                nearests.remove(min);
            } while (matching == null);
            Path startFloor = this.cur.findPath(start, curr);
            Path endFloor = this.cur.findPath(matching, end);
            return startFloor.addSteps(endFloor);
        }
        else {
            return this.cur.findPath(start, end);
        }
    }


    public Collection<String> getRoomNames() {
        ArrayList<String> roomNames = new ArrayList<String>(this.ids.keySet());
        Collections.sort(roomNames);
        return roomNames;
    }
}
