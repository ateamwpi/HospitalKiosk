package models.dir;

import models.path.Node;

/**
 * Created by mattm on 3/29/2017.
 */
public class Location {
    private final int id;
    private String name;
    private LocationType locType;
    private Node node;

    public Location(int id, String name, LocationType locType, Node node) {
        this.id = id;
        this.name = name;
        this.locType = locType;
        this.node = node;
    }

    public int getID() {
        return this.id;
    }

    public String toString() {
        String str = "Location " + name + ", ID=" + id + ", locType=" + locType + ", nodeID=" + node.getID();
        return str;
    }
}
