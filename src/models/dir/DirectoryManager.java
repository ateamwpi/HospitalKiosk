package models.dir;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mattm on 3/29/2017.
 */
public class DirectoryManager {

    private HashMap<LocationType, Directory> directories;
    private Location theKiosk;
    private Location mainEntr;
    private Location belkinEntr;

    public DirectoryManager(HashMap<LocationType, Directory> allLocations, Location theKiosk, Location mainEntr, Location belkinEntr) {
        this.directories = allLocations;
        this.theKiosk = theKiosk;
        this.mainEntr = mainEntr;
        this.belkinEntr = belkinEntr;
    }

    public void addLocation(Location l) {
        getDirectory(l.getLocType()).addLocation(l);
    }

    public Location getTheKiosk() {
        return this.theKiosk;
    }

    public Location getMainEntr() {
        return this.mainEntr;
    }

    public Location getBelkinEntr() {
        return this.belkinEntr;
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
