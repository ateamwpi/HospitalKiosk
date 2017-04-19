package models.dir;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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

    public List<Location> search(String query, Integer maxResults) {
        List<Location> locations = search(query);
        int length = locations.size();
        return locations.subList(0, maxResults > length ? length : maxResults);
    }

    public List<Location> search(String query) {
        List<Location> locations = new ArrayList<>();
        for (Location loc : getAllLocations()) {
            if (loc.getName().toLowerCase().contains(query)) {
                locations.add(loc);
            }
        }
        return locations;
    }

    private Collection<Location> getAllLocations() {
        Collection<Location> locations = new ArrayList<>();
        for(LocationType locType : LocationType.values()) {
            if(!locType.isInternal()) {
                locations.addAll(getLocationsOfType(locType));
            }
        }
        return locations;
    }

    private Collection<Location> getLocationsOfType(LocationType locType) {
        if (directories.containsKey(locType)) {
            Directory dir = directories.get(locType);
            HashMap<Integer, Location> locations = dir.getLocations();
            return locations.values();
        } else {
            throw new RuntimeException("Could not find location type: " + locType.toString());
        }
    }
}
