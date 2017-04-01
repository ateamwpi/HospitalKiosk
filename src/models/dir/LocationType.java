package models.dir;

/**
 * Created by mattm on 3/29/2017.
 */
public enum LocationType {
    Room,
    Stairs,
    Elevator,
    PointOfInterest,
    Service,
    Hallway,
    Physician,
    Unknown;

    public boolean isInternal() {
        return this.equals(Hallway) || this.equals(Stairs) || this.equals(Elevator) || this.equals(Unknown);
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
            default: return Unknown;
        }
    }
}
