package models.path;

import core.KioskMain;
import core.exception.NameInUseException;
import core.exception.WrongFloorException;
import javafx.beans.property.*;
import javafx.scene.paint.Color;
import models.dir.Location;
import models.dir.LocationType;

import java.util.*;

/**
 * Created by mattm on 3/29/2017.
 */
public class Node {
    private static int nextNodeID;

    private final IntegerProperty xProperty = new SimpleIntegerProperty();
    private final IntegerProperty yProperty = new SimpleIntegerProperty();
    private final IntegerProperty idProperty = new SimpleIntegerProperty();
    private final IntegerProperty floorProperty = new SimpleIntegerProperty();
    private final StringProperty roomNameProperty = new SimpleStringProperty();
    private final BooleanProperty restrictedProperty = new SimpleBooleanProperty();
    private String previousRoomName = "";
    private final ArrayList<Node> connections;
    private final HashMap<Integer, Location> locations;
    private final HashMap<LocationType, Integer> counts;
    private final boolean isNew;
    private boolean isBelkin;
    private boolean isMain;
    private boolean isDone;

    /** This constructor should _ONLY_ be used when loading from the database. For any
     *  new nodes created, use Node(x, y) and a unique ID will automatically be generated.
     */
    public Node(int id, int x, int y, int floor, boolean restricted, String roomName) {
        idProperty.set(id);
        xProperty.set(x);
        yProperty.set(y);
        floorProperty.set(floor);
        roomNameProperty.set(roomName);
        restrictedProperty.set(restricted);
        connections = new ArrayList<>();
        locations = new HashMap<>();
        counts = new HashMap<>();
        isNew = false;
        isDone = false;
        updateBuilding();
    }

    public Node(int x, int y, int floor, boolean restricted, String roomName) {
        idProperty.set(getNextNodeID());
        xProperty.set(x);
        yProperty.set(y);
        floorProperty.set(floor);
        roomNameProperty.set(roomName);
        restrictedProperty.set(restricted);
        connections = new ArrayList<>();
        locations = new HashMap<>();
        counts = new HashMap<>();
        isNew = true;
        isDone = true;
        updateBuilding();
    }

    public Node(int x, int y, int floor, boolean restricted) {
        idProperty.set(getNextNodeID());
        xProperty.set(x);
        yProperty.set(y);
        floorProperty.set(floor);
        roomNameProperty.set("NONE");
        restrictedProperty.set(restricted);
        connections = new ArrayList<>();
        locations = new HashMap<>();
        counts = new HashMap<>();
        isNew = true;
        isDone = true;
        updateBuilding();
    }

    public void addLocation(Location l) {
        locations.put(l.getID(), l);
        if(!counts.containsKey(l.getLocType())) counts.put(l.getLocType(), 1);
        else counts.put(l.getLocType(), counts.get(l.getLocType()) + 1);
    }

    public void addConnection(Node other) throws WrongFloorException {
        if(getFloor() != other.getFloor()) {
            throw new WrongFloorException(this, other);
        }
        if(!connections.contains(other)) {
            connections.add(other);
            if(isDone && !other.connections.contains(this)) {
                KioskMain.getDB().addConnection(this, other);
            }
            other.addConnection(this);
        }
    }

    public LocationType getPrimaryLocType() {
        if(counts.isEmpty()) return LocationType.Unknown;
        return Collections.max(counts.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    public Color getColor() {
        return getPrimaryLocType().getNodeColor();
    }

    private void removeConnection(Node other) {
        if(connections.contains(other)) {
            connections.remove(other);
            KioskMain.getDB().removeConnection(this, other);
            other.removeConnection(this);
        }
    }

    private void updateBuilding() {
        isBelkin = getX() >= 30 && getX() <= 220 && getY() >= 10 && getY() <= 210;
        isMain = getX() >= 110 && getX() <= 910 && getY() >= 230 && getY() <= 680;
    }

    public void removeAllConnections() {
        //noinspection unchecked
        Collection<Node> clone = (Collection<Node>) connections.clone();
        connections.clear();
        for (Node n : clone) {
            n.removeConnection(this);
        }
    }

    public void setRoomName(String name) throws NameInUseException {
        if(name.equals(getRoomName())) return;
        if(KioskMain.getPath().hasRoomName(name)) {
            throw new NameInUseException(name);
        }
        previousRoomName = getRoomName();
        roomNameProperty.setValue(name);
    }

    public void setConnections(Collection<Node> conns) throws WrongFloorException {
        // Add anything new
        for (Node n : conns) {
            if(!connections.contains(n)) {
                addConnection(n);
            }
        }

        // Remove anything old
        ArrayList<Node> toRemove = new ArrayList<>();
        for (Node n : connections) {
            if(!conns.contains(n)) {
                toRemove.add(n);
            }
        }

        for (Node n : toRemove) {
            removeConnection(n);
        }
    }

    public int getID() {
        return idProperty.get();
    }

    public boolean isBelkin() {
        return isBelkin;
    }

    public boolean isMain() {
        return isMain;
    }

    public final String getRoomName() {
        return roomNameProperty.get();
    }

    public StringProperty getRoomNameProperty() {
        return roomNameProperty;
    }

    public Collection<Location> getLocations() {
        return locations.values();
    }

    public Collection<Node> getConnections() {
        //noinspection unchecked
        return (Collection<Node>) connections.clone();
    }

    public final int getFloor(){
        return floorProperty.get();
    }

    public final void setFloor(int value) {
        floorProperty.set(value);
    }

    public final int getX(){
        return xProperty.get();
    }

    public final void setX(int value){
        xProperty.set(value);
        updateBuilding();
    }

    public IntegerProperty xProperty() {
        return xProperty;
    }

    public final int getY(){
        return yProperty.get();
    }

    public final void setY(int value){
        yProperty.set(value);
        updateBuilding();
    }

    public final void save() {
        // TODO check if node in db first
        KioskMain.getPath().updateRoomName(this, previousRoomName);
        KioskMain.getDB().updateNode(this);
    }

    public IntegerProperty yProperty() {
        return yProperty;
    }

    public void removeLocation(Location l) {
        locations.remove(l.getID());
        counts.put(l.getLocType(), counts.get(l.getLocType())-1);
    }

    public String toString() {
        StringBuilder str= new StringBuilder("Node: ID=" + getID() + ", X=" + getX() + ", Y=" + getY() + ", FLOOR=" + getFloor() + ", NAME=" + getRoomName() + "\n");
        for (Node n : connections) {
            str.append("Connected to Node ID=").append(n.getID()).append("\n");
        }
        return str.toString();
    }

//    public boolean equals(Object o) {
//        Node n = (Node) o;
//        return n.getID() == getID();
//    }

    public boolean isNew() {
        return isNew;
    }

    public void setDone() {
        isDone = true;
    }

    public boolean isRestricted() { return restrictedProperty.get(); }

    public void setRestricted(boolean value) { restrictedProperty.set(value); }


    public static void setNextNodeID(int i) {
        nextNodeID = i;
    }

    private static int getNextNodeID() {
        int val = nextNodeID;
        nextNodeID ++;
        return val;
    }

    public IntegerProperty idProperty() {
        return idProperty;
    }
}
