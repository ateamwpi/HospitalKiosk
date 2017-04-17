package models.path;

import core.KioskMain;
import core.exception.WrongFloorException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import models.dir.Location;
import models.dir.LocationType;

import java.util.*;

/**
 * Created by mattm on 3/29/2017.
 */
public class Node {
    private static int nextNodeID;

    private IntegerProperty xProperty = new SimpleIntegerProperty();
    private IntegerProperty yProperty = new SimpleIntegerProperty();
    private IntegerProperty idProperty = new SimpleIntegerProperty();
    private IntegerProperty floorProperty = new SimpleIntegerProperty();
    private StringProperty roomNameProperty = new SimpleStringProperty();
    private String previousRoomName = "";
    private ArrayList<Node> connections;
    private HashMap<Integer, Location> locations;
    private HashMap<LocationType, Integer> counts;
    private final boolean isNew;
    private boolean isBelkin;
    private boolean isDone;

    /** This constructor should _ONLY_ be used when loading from the database. For any
     *  new nodes created, use Node(x, y) and a unique ID will automatically be generated.
     */
    public Node(int id, int x, int y, int floor, String roomName) {
        idProperty.set(id);
        xProperty.set(x);
        yProperty.set(y);
        floorProperty.set(floor);
        roomNameProperty.set(roomName);
        this.connections = new ArrayList<Node>();
        this.locations = new HashMap<Integer, Location>();
        this.counts = new HashMap<LocationType, Integer>();
        this.isNew = false;
        this.isDone = false;
        this.updateBuilding();
    }

    public Node(int x, int y, int floor, String roomName) {
        idProperty.set(getNextNodeID());
        xProperty.set(x);
        yProperty.set(y);
        floorProperty.set(floor);
        roomNameProperty.set(roomName);
        this.connections = new ArrayList<Node>();
        this.locations = new HashMap<Integer, Location>();
        this.counts = new HashMap<LocationType, Integer>();
        this.isNew = true;
        this.isDone = true;
        this.updateBuilding();
    }

    public Node(int x, int y, int floor) {
        idProperty.set(getNextNodeID());
        xProperty.set(x);
        yProperty.set(y);
        floorProperty.set(floor);
        roomNameProperty.set("NONE");
        this.connections = new ArrayList<Node>();
        this.locations = new HashMap<Integer, Location>();
        this.counts = new HashMap<LocationType, Integer>();
        this.isNew = true;
        this.isDone = true;
        this.updateBuilding();
    }

    public void addLocation(Location l) {
        this.locations.put(l.getID(), l);
        if(!this.counts.containsKey(l.getLocType())) this.counts.put(l.getLocType(), 1);
        else this.counts.put(l.getLocType(), this.counts.get(l.getLocType()) + 1);
    }

    public void addConnection(Node other) throws WrongFloorException {
        if(this.getFloor() != other.getFloor()) {
            throw new WrongFloorException(this, other);
        }
        if(!this.connections.contains(other)) {
            this.connections.add(other);
            if(this.isDone && !other.connections.contains(this)) {
                KioskMain.getDB().addConnection(this, other);
            }
            other.addConnection(this);
        }
    }

    public LocationType getPrimaryLocType() {
        if(this.counts.isEmpty()) return LocationType.Unknown;
        return Collections.max(this.counts.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    public Color getColor() {
        return this.getPrimaryLocType().getNodeColor();
    }

    public void removeConnection(Node other) {
        if(this.connections.contains(other)) {
            this.connections.remove(other);
            KioskMain.getDB().removeConnection(this, other);
            other.removeConnection(this);
        }
    }

    public void updateBuilding() {
        if(this.getX() >= 30 && this.getX() <= 220 && this.getY() >= 10 && this.getY() <= 210)
            this.isBelkin = true;
        else
            this.isBelkin = false;
    }

    public void removeAllConnections() {
        Collection<Node> clone = (Collection<Node>) this.connections.clone();
        this.connections.clear();
        for (Node n : clone) {
            n.removeConnection(this);
        }
    }

    public void setRoomName(String name) {
        this.previousRoomName = this.getRoomName();
        roomNameProperty.setValue(name);
    }

    public void setConnections(Collection<Node> conns) throws WrongFloorException {
        // Add anything new
        for (Node n : conns) {
            if(!this.connections.contains(n)) {
                this.addConnection(n);
            }
        }

        // Remove anything old
        ArrayList<Node> toRemove = new ArrayList<Node>();
        for (Node n : this.connections) {
            if(!conns.contains(n)) {
                toRemove.add(n);
            }
        }

        for (Node n : toRemove) {
            this.removeConnection(n);
        }
    }

    public int getID() {
        return idProperty.get();
    }

    public boolean isBelkin() {
        return this.isBelkin;
    }

    public final String getRoomName() {
        return roomNameProperty.get();
    }

    public StringProperty getRoomNameProperty() {
        return roomNameProperty;
    }

    public Collection<Location> getLocations() {
        return this.locations.values();
    }

    public Collection<Node> getConnections() {
        return (Collection<Node>)this.connections.clone();
    }

    public final int getFloor(){
        return floorProperty.get();
    }

    public final void setFloor(int value){
        floorProperty.set(value);
    }

    public final int getX(){
        return xProperty.get();
    }

    public final void setX(int value){
        xProperty.set(value);
        this.updateBuilding();
    }

    public IntegerProperty xProperty() {
        return xProperty;
    }

    public final int getY(){
        return yProperty.get();
    }

    public final void setY(int value){
        yProperty.set(value);
        this.updateBuilding();
    }

    public final void save() {
        // TODO check if node in db first
        KioskMain.getPath().updateRoomName(this, this.previousRoomName);
        KioskMain.getDB().updateNode(this);
    }

    public IntegerProperty yProperty() {
        return yProperty;
    }

    public void removeLocation(Location l) {
        System.out.println("hi");
        this.locations.remove(l.getID());
        this.counts.put(l.getLocType(), this.counts.get(l.getLocType())-1);
    }

    public String toString() {
        String str= "Node: ID=" + getID() + ", X=" + getX() + ", Y=" + getY() + ", FLOOR=" + getFloor() + ", NAME=" + getRoomName() + "\n";
        for (Node n : this.connections) {
            str += "Connected to Node ID=" + n.getID() + "\n";
        }
        return str;
    }

//    public boolean equals(Object o) {
//        Node n = (Node) o;
//        return n.getID() == this.getID();
//    }

    public boolean isNew() {
        return this.isNew;
    }

    public void setDone() {
        this.isDone = true;
    }


    public static void setNextNodeID(int i) {
        nextNodeID = i;
    }

    public static int getNextNodeID() {
        int val = nextNodeID;
        nextNodeID ++;
        return val;
    }

    public IntegerProperty idProperty() {
        return idProperty;
    }
}
