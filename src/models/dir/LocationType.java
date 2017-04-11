package models.dir;

import javafx.scene.paint.Color;

/**
 * Created by mattm on 3/29/2017.
 */
public enum LocationType {
    Room(Color.BLACK),
    Service(Color.MAGENTA),
    Physician(Color.CADETBLUE),
    PointOfInterest(Color.ORANGE),
    Kiosk(Color.GREEN),
    Stairs(Color.DARKBLUE),
    Elevator(Color.DARKBLUE),
    Hallway(Color.GRAY),
    Unknown(Color.GRAY);

    private Color nodeColor;

    LocationType(Color nodeColor) {
        this.nodeColor = nodeColor;
    }

    public Color getNodeColor() {
        return this.nodeColor;
    }

    public boolean isInternal() {
        return this.equals(Hallway) || this.equals(Unknown) || this.equals(Kiosk);
    }

    public boolean hasNearest() {
        return this.equals(PointOfInterest) || this.equals(Stairs) || this.equals(Elevator);
    }

    public static LocationType getType(String s) {
        switch (s.toUpperCase()) {
            case "ROOM": return Room;
            case "STAIRS": return Stairs;
            case "ELEVATOR": return Elevator;
            case "POINTOFINTEREST": return PointOfInterest;
            case "SERVICE": return Service;
            case "HALLWAY": return Hallway;
            case "PHYSICIAN": return Physician;
            case "KIOSK": return Kiosk;
            default: return Unknown;
        }
    }
}
