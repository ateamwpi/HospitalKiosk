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
        this.entries = new HashMap<Integer, Location>();
    }

    public void addEntry(Location l) {
        if (l.isNew()) {
            KioskMain.getDB().addLocation(l);
        }
        this.entries.put(l.getID(), l);
    }

    public String toString() {
        String str = this.name + " Directory\n";
        for (Location l : this.entries.values()) {
            str += l.toString() + "\n";
        }
        return str;
    }
}
