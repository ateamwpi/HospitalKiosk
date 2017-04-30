package models.dir;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mattm on 3/29/2017.
 * nice
 */
public enum LocationType {
    Room,
    Service,
    Physician,
    PointOfInterest,
    Restroom,
    Kiosk,
    Entrance,
    Staircase,
    Elevator,
    Hallway,
    Unknown;

    private static final HashMap<String, LocationType> names = new HashMap<>();

    LocationType() {}

    static {
        for (LocationType l : LocationType.values()) {
            names.put(l.name().toUpperCase(), l);
            names.put(l.friendlyName().toUpperCase(), l);
        }
    }

    public boolean isInternal() {
        return equals(Hallway) || equals(Unknown) || equals(Kiosk) || equals(Entrance);
    }

//    public boolean hasNearest() {
//        return equals(PointOfInterest) || equals(Restroom) || equals(Staircase) || equals(Elevator);
//    }

    public static LocationType getType(String s) {
        return names.getOrDefault(s.toUpperCase(), Unknown);
    }

    public String friendlyName() {
        if (equals(PointOfInterest)) {
            return "Point of Interest";
        } else  {
            return name();
        }
    }

    /**
     *
     * @return The LocationTypes that a user can set a location to through the admin interface
     */
    public static LocationType[] userValues() {
        ArrayList<LocationType> values = new ArrayList<>();
        for(LocationType locType : values()) {
            if(!locType.isInternal()) {
                values.add(locType);
            }
        }
        return values.toArray(new LocationType[values.size()]);
    }
}
