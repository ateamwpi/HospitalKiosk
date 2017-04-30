package models.dir;

import core.KioskMain;
import core.exception.NearestNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by mattm on 3/29/2017.
 *
 */
public class DirectoryManager {

    private final HashMap<LocationType, Directory> directories;
    private final Location theKiosk;
    private final Location mainEntr;
    private final Location belkinEntr;

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
        return theKiosk;
    }

    public Location getMainEntr() {
        return mainEntr;
    }

    public Location getBelkinEntr() {
        return belkinEntr;
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

    void updateLocationType(Location location, LocationType newLocType) {
        getDirectory(location.getLocType()).moveLocation(location, newLocType);
        getDirectory(newLocType).moveLocation(location, newLocType);
    }

    public List<Location> search(String query) {
        return search(query, getAllLocations());
    }

    public List<Location> search(String query, Collection<Location> locations) {
        final String queryLower = query.toLowerCase();
        return locations.stream().filter(location -> location.getName().toLowerCase().contains(queryLower)
                || location.getNode().getRoomName().toLowerCase().contains(queryLower)).collect(Collectors.toList());
    }

    private Collection<Location> getAllLocations() {
        Collection<Location> locations = new ArrayList<>();
        for(LocationType locType : LocationType.values()) {
            if(!locType.isInternal()) {
                locations.addAll(getLocationsOfType(locType));
            }
        }
        locations.add(KioskMain.getDir().getTheKiosk());
        return locations;
    }

    private Collection<Location> getLocationsOfType(LocationType locType) {
        if (directories.containsKey(locType)) {
            Directory dir = directories.get(locType);
            Map<Integer, Location> locations = dir.getLocations();
            return locations.values();
        } else {
            throw new RuntimeException("Could not find location type: " + locType.toString());
        }
    }

    public List<Location> getPOI() {
        HashMap<Location, Double> nearPOI = null;
        HashMap<Location, Double> nearRest = null;
        try {
            nearPOI = KioskMain.getPath().getNearest(LocationType.PointOfInterest, theKiosk.getNode());

        } catch (NearestNotFoundException e) {
            // TODO
        }
        try {
            nearRest = KioskMain.getPath().getNearest(LocationType.Restroom, theKiosk.getNode());
        } catch (NearestNotFoundException e) {
            // TODO
        }

        if(nearPOI == null && nearRest == null) return new ArrayList<>();
        List<Location> ret = new ArrayList<>();
        // add the kiosk
        ret.add(KioskMain.getDir().getTheKiosk());
        while (nearPOI != null && !nearPOI.isEmpty()) {
            Location l = Collections.min(nearPOI.entrySet(), Comparator.comparingInt(entry -> (int) entry.getValue().doubleValue())).getKey();
            nearPOI.remove(l);
            ret.add(l);
        }
        while (nearRest != null && !nearRest.isEmpty()) {
            Location l = Collections.min(nearRest.entrySet(), Comparator.comparingInt(entry -> (int) entry.getValue().doubleValue())).getKey();
            nearRest.remove(l);
            ret.add(l);
        }
        return ret;
    }
}
