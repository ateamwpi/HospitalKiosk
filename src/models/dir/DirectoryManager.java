package models.dir;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mattm on 3/29/2017.
 */
public class DirectoryManager {

    private HashMap<LocationType, Directory> directories;

    public DirectoryManager(HashMap<LocationType, Directory> allLocations) {
        this.directories = allLocations;
    }

    public void addLocation(Location l) {
        this.getDirectory(l.getLocType()).addLocation(l);
    }

    public void removeLocation(Location l) {
        this.getDirectory(l.getLocType()).removeLocation(l);
    }

    public HashMap<LocationType, Directory> getDirectories() {
        return this.directories;
    }

    public Directory getDirectory(LocationType locType) {
        return this.directories.get(locType);
    }

}
