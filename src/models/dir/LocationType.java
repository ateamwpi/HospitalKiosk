package models.dir;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mattm on 3/29/2017.
 */
public enum LocationType {
    Room(Color.BLACK),
    Service(Color.MAGENTA),
    Physician(Color.CADETBLUE),
    PointOfInterest(Color.ORANGE),
    Restroom(Color.ORANGE),
    Kiosk(Color.GREEN),
    Entrance(Color.GREEN),
    Staircase(Color.DARKBLUE),
    Elevator(Color.DARKBLUE),
    Hallway(Color.GRAY),
    Unknown(Color.GRAY);

    private Color nodeColor;
    private static final HashMap<String, LocationType> names = new HashMap<>();

    LocationType(Color nodeColor) {
        this.nodeColor = nodeColor;
    }

    static {
        for (LocationType l : LocationType.values()) {
            names.put(l.name().toUpperCase(), l);
            names.put(l.friendlyName().toUpperCase(), l);
        }
    }

    public Color getNodeColor() {
        return nodeColor;
    }

    public boolean isInternal() {
        return equals(Hallway) || equals(Unknown) || equals(Kiosk) || equals(Entrance);
    }

    public boolean hasNearest() {
        return equals(PointOfInterest) || equals(Restroom) || equals(Staircase) || equals(Elevator);
    }

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
