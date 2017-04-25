package core.exception;

import models.path.Node;

/**
 * Created by mattm on 4/13/2017.
 */
public class PathNotFoundException extends Exception {
    private final Node start;
    private final Node end;

    public PathNotFoundException(Node start, Node end) {
        this.start = start;
        this.end = end;
    }

    public void printStackTrace() {
        System.out.println("No path was found between " + start + " and " + end + "!");
        super.printStackTrace();
    }


}
