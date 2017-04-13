package models.dir;

import core.KioskMain;

import java.util.HashMap;

/**
 * Created by mattm on 3/29/2017.
 */
public class Directory {
    private String name;
    private HashMap<Integer, Location> entries;

    public Directory(String name) {
        this.name = name;
        this.entries = new HashMap<>();
    }

    public HashMap<Integer, Location> getLocations() {
        return entries;
    }

    public void addLocation(Location l) {
        if (l.isNew()) {
            KioskMain.getDB().addLocation(l);
        }
        entries.put(l.getID(), l);
    }

    private LocationType getType() {
        return LocationType.getType(this.name);
    }

    void moveLocation(Location l, LocationType newType) {
        if(newType.equals(this.getType())) {
            this.entries.put(l.getID(), l);
        }
        else {
            this.entries.remove(l.getID());
        }
    }

    public Location getLocation(int id) {
        return entries.get(id);
    }

    void removeLocation(Location l) {
        System.out.println("removing location " + l);
        l.getNode().removeLocation(l);
        entries.remove(l.getID());
        KioskMain.getDB().removeLocation(l);
    }

    public String toString() {
        StringBuilder str = new StringBuilder(name + " Directory\n");
        for (Location l : entries.values()) {
            str.append(l.toString()).append("\n");
        }
        return str.toString();
    }
}
