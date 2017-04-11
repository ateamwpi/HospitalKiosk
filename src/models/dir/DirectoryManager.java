package models.dir;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mattm on 3/29/2017.
 */
public class DirectoryManager {

    private HashMap<LocationType, Directory> directories;
    private Location theKiosk;

    public DirectoryManager(HashMap<LocationType, Directory> allLocations, Location theKiosk) {
        this.directories = allLocations;
        this.theKiosk = theKiosk;
    }

    public void addLocation(Location l) {
        getDirectory(l.getLocType()).addLocation(l);
    }

    public Location getTheKiosk() {
        return this.theKiosk;
    }

    public void removeLocation(Location l) {
        getDirectory(l.getLocType()).removeLocation(l);
    }

    public HashMap<LocationType, Directory> getDirectories() {
        return directories;
    }

    public Directory getDirectory(LocationType locType) {
        return directories.get(locType);
    }

    public void updateLocationType(Location location, LocationType newLocType) {
        this.getDirectory(location.getLocType()).moveLocation(location, newLocType);
        this.getDirectory(newLocType).moveLocation(location, newLocType);
    }
}
