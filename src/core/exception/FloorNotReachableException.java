package core.exception;

import models.path.Node;

/**
 * Created by mattm on 4/13/2017.
 */
public class FloorNotReachableException extends Exception {
    private final Node start;
    private final int floor;

    public FloorNotReachableException(Node start, int floor) {
        this.start = start;
        this.floor = floor;
    }

    public void printStackTrace() {
        System.out.println("There is currently no way to reach floor " + floor + " from " + start + "!");
        System.out.println("This is most likely caused by issues with the Elevators in the database.");
        super.printStackTrace();
    }

}
