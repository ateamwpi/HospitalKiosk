package models.dir;

import java.util.HashMap;

/**
 * Created by mattm on 3/29/2017.
 */
public class DirectoryManager {

    private final HashMap<LocationType, Directory> directories;
    private final Location theKiosk;

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

}
