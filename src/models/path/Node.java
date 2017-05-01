package models.path;

import core.KioskMain;
import core.exception.NameInUseException;
import core.exception.WrongFloorException;
import javafx.beans.property.*;
import javafx.scene.paint.Color;
import models.dir.Location;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Created by mattm on 3/29/2017.
 *
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
    private NodeType nodeType;
    private final boolean isNew;
    private boolean isBelkin;
    private boolean isMain;
    private boolean isDone;

    /** This constructor should _ONLY_ be used when loading from the database. For any
     *  new nodes created, use Node(x, y) and a unique ID will automatically be generated.
     */
    public Node(int id, int x, int y, int floor, boolean restricted, NodeType type, String roomName) {
        idProperty.set(id);
        xProperty.set(x);
        yProperty.set(y);
        floorProperty.set(floor);
        roomNameProperty.set(roomName);
        restrictedProperty.set(restricted);
        connections = new ArrayList<>();
        locations = new HashMap<>();
        this.nodeType = type;
        isNew = false;
        isDone = false;
        updateBuilding();
    }

    public Node(int x, int y, int floor, boolean restricted, NodeType type, String roomName) {
        idProperty.set(getNextNodeID());
        xProperty.set(x);
        yProperty.set(y);
        floorProperty.set(floor);
        roomNameProperty.set(roomName);
        restrictedProperty.set(restricted);
        connections = new ArrayList<>();
        locations = new HashMap<>();
        this.nodeType = type;
        isNew = true;
        isDone = true;
        updateBuilding();
    }

    public Node(int x, int y, int floor, boolean restricted, NodeType type) {
        idProperty.set(getNextNodeID());
        xProperty.set(x);
        yProperty.set(y);
        floorProperty.set(floor);
        roomNameProperty.set("NONE");
        restrictedProperty.set(restricted);
        connections = new ArrayList<>();
        locations = new HashMap<>();
        this.nodeType = type;
        isNew = true;
        isDone = true;
        updateBuilding();
    }

    public void addLocation(Location l) {
        locations.put(l.getID(), l);
    }

    public void addConnection(Node other) throws WrongFloorException {
        if(getFloor() != other.getFloor() && !(this.nodeType.equals(NodeType.Elevator) || this.nodeType.equals(NodeType.Staircase))) {
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

    public Color getColor() {
        return this.nodeType.getNodeColor();
    }

    private void removeConnection(Node other) {
        if(connections.contains(other)) {
            connections.remove(other);
            KioskMain.getDB().removeConnection(this, other);
            other.removeConnection(this);
        }
    }

    public void removeCrossFloorConnections() {
        for(Node n : this.getConnections()) {
            if(n.getFloor() != this.getFloor()) {
                n.removeConnection(this);
            }
        }
    }

    private void updateBuilding() {
        isBelkin = getX() >= 30 && getX() <= 220 && getY() >= 10 && getY() <= 210;
        isMain = getX() >= 110 && getX() <= 910 && getY() >= 230 && getY() <= 680;
    }

    void removeAllConnections() {
        //noinspection unchecked
        Collection<Node> clone = (Collection<Node>) connections.clone();
        connections.clear();
        for (Node n : clone) {
            n.removeConnection(this);
        }
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public void setRoomName(String name) throws NameInUseException {
        if(name.equals(getRoomName())) return;
        if(KioskMain.getPath().hasRoomName(name)) {
            throw new NameInUseException(name);
        }
        previousRoomName = getRoomName();
        roomNameProperty.setValue(name);
        KioskMain.getPath().updateRoomName(this, previousRoomName);
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

    boolean isBelkin() {
        return (this.getFloor() == 1 && isBelkin) || (this.getFloor() > 7);
    }

    public boolean isMain() {
        return (this.getFloor() == 1 && isMain) || (this.getFloor() < 8 && this.getFloor() > 1);
    }

    public final String getRoomName() {
        return roomNameProperty.get();
    }

//    public StringProperty getRoomNameProperty() {
//        return roomNameProperty;
//    }

    public Collection<Location> getLocations() {
        return locations.values();
    }

    public Collection<Node> getConnections() {
        //noinspection unchecked
        Collection<Node> clone = (Collection<Node>) connections.clone();

        if(KioskMain.getLogin().getState().hasAccess())
            return clone;
        else
            return clone.stream().filter(node -> !node.isRestricted()).collect(Collectors.toList());
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

//    public IntegerProperty xProperty() {
//        return xProperty;
//    }

    public final int getY(){
        return yProperty.get();
    }

    public final void setY(int value){
        yProperty.set(value);
        updateBuilding();
    }

    public final void save() {
        // TODO check if node in db first
        KioskMain.getDB().updateNode(this);
    }

//    public IntegerProperty yProperty() {
//        return yProperty;
//    }

    public void removeLocation(Location l) {
        locations.remove(l.getID());
    }

    public NodeType getNodeType() {
        return this.nodeType;
    }

    public String toString() {
        StringBuilder str= new StringBuilder("Node: ID=" + getID() + ", X=" + getX() + ", Y=" + getY() + ", FLOOR=" + getFloor() + ", NAME=" + getRoomName() + "\n");
        for (Node n : connections) {
            str.append("Connected to Node ID=").append(n.getID()).append("\n");
        }
        return str.toString();
    }

    public boolean equals(Object o) {
        if(!(o instanceof Node)) return false;
        Node n = (Node) o;
        return n.getID() == this.getID();
    }

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

//    public IntegerProperty idProperty() {
//        return idProperty;
//    }
}
