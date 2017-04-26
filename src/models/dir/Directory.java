package models.dir;

import core.KioskMain;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by mattm on 3/29/2017.
 */
public class Directory {
    private final String name;
    private final HashMap<Integer, Location> entries;

    public Directory(String name) {
        this.name = name;
        this.entries = new HashMap<>();
    }

    public Map<Integer, Location> getLocations() {
        Map<Integer, Location> clone = (HashMap<Integer, Location>) entries.clone();

        if(KioskMain.getLogin().getState().hasAccess())
            return clone;
        else
            return clone.entrySet().stream().filter(e -> !e.getValue().isRestricted()).collect(Collectors.toMap(e->e.getKey(), e->e.getValue()));
    }

    public void addLocation(Location l) {
        if (l.isNew()) {
            KioskMain.getDB().addLocation(l);
        }
        entries.put(l.getID(), l);
    }

    private LocationType getType() {
        return LocationType.getType(name);
    }

    void moveLocation(Location l, LocationType newType) {
        if(newType.equals(getType())) {
            entries.put(l.getID(), l);
        }
        else {
            entries.remove(l.getID());
        }
    }

    public Location getLocation(int id) {
        return entries.get(id);
    }

    public void removeLocation(Location l) {
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
