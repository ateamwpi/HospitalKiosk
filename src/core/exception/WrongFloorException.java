package core.exception;

import models.path.Node;

/**
 * Created by mattm on 4/8/2017.
 */
public class WrongFloorException extends Exception{
    private final Node n;
    private final Node n2;

    public WrongFloorException(Node n, Node n2) {
        this.n2 = n2;
        this.n = n;
    }

    public void printStackTrace() {
        System.out.println("The nodes n1=" + n + " and n2=" + n2 + " are not on the same floor and can't be connected!");
        super.printStackTrace();
    }
}
