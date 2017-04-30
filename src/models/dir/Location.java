package models.dir;

import core.KioskMain;
import javafx.beans.property.SimpleStringProperty;
import models.path.Node;

/**
 * Created by mattm on 3/29/2017.
 *
 */
public class Location {
    private static int nextLocID;

    private final int id;
    private String name;
    private LocationType locType;
    private Node node;
    private final boolean isNew;

    /** This constructor should _ONLY_ be used when loading from the database. For any
     *  new locations created, use Location(name, locType, node) and a unique ID will automatically
     *  be generated.
     */
    public Location(int id, String name, LocationType locType, Node node) {
        this.id = id;
        this.name = name;
        this.locType = locType;
        this.node = node;
        this.node.addLocation(this);
        this.isNew = false;
    }

    public Location(LocationType locType, Node node) {
        this.id = getNextLocID();
        this.name = "";
        this.locType = locType;
        this.node = node;
        this.node.addLocation(this);
        this.isNew = true;
    }

    public int getID() {
        return id;
    }

    public boolean isRestricted() {
        return this.getNode().isRestricted();
    }

    public boolean isNew() {
        return isNew;
    }

    public void setName(String name) {
        this.name = name;
        KioskMain.getDB().updateLocation(this);
    }

    public void setLocType(LocationType locType) {
        KioskMain.getDir().updateLocationType(this, locType);
        this.locType = locType;
        KioskMain.getDB().updateLocation(this);
    }

    public void setNode(Node node) {
        this.node = node;
        KioskMain.getDB().updateLocation(this);
    }

    public String getName() {
        return name;
    }

    public LocationType getLocType() {
        return locType;
    }

    public Node getNode() {
        return node;
    }

    public String toString() {
        return "Location " + name + ", ID=" + id + ", locType=" + locType + ", nodeID=" + node.getID();
    }




    public static void setNextLocID(int i) {
        nextLocID = i;
    }

    private static int getNextLocID() {
        int val = nextLocID;
        nextLocID ++;
        return val;
    }

    public SimpleStringProperty roomNameProperty() {
        return new SimpleStringProperty(node.getRoomName());
    }
}
