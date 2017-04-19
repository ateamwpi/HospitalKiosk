package models.dir;

import core.KioskMain;
import core.exception.NearestNotFoundException;

import java.awt.*;
import java.util.*;
import java.util.List;

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

    public List<Location> getPOI() {
        HashMap<Location, Double> nearests = null;
        try {
            nearests = KioskMain.getPath().getNearest(LocationType.PointOfInterest, theKiosk.getNode());
        } catch (NearestNotFoundException e) {
            return new ArrayList<>();
        }
        List<Location> ret = new ArrayList();
        while (!nearests.isEmpty()) {
            Location l = Collections.min(nearests.entrySet(), Comparator.comparingInt(entry -> (int) entry.getValue().doubleValue())).getKey();
            nearests.remove(l);
            ret.add(l);
        }
        return ret;
    }
}
