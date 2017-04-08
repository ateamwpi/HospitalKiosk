package models.path;

/**
 * Created by mattm on 4/6/2017.
 */
public enum Direction {
    North,
    East,
    South,
    West;

    public static Direction dirFor(Node n1, Node n2) {
        int dx = n2.getX() - n1.getX();
        int dy = n2.getY() - n1.getY();
        if(Math.abs(dx) >= Math.abs(dy)) {
            if(dx > 0) return East;
            else return West;
        }
        else if(Math.abs(dy) > Math.abs(dx)) {
            if(dy > 0) return South;
            else return North;
        }
        else {
            System.out.println("This shouldn't even be reachable");
            return null;
        }
    }

    public String turnFor(Direction dir) {
        switch(this) {
            case North:
                switch(dir) {
                    case East: return "right";
                    case West: return "left";
                    case South: return "back";
                    default: return "straight";
                }
            case East:
                switch(dir) {
                    case North: return "left";
                    case South: return "right";
                    case West: return "back";
                    default: return "straight";
                }
            case South:
                switch(dir) {
                    case East: return "left";
                    case West: return "right";
                    case North: return "back";
                    default: return "straight";
                }
            case West:
                switch(dir) {
                    case South: return "left";
                    case North: return "right";
                    case East: return "back";
                    default: return "straight";
                }
            default: return "straight";
        }
    }
}
