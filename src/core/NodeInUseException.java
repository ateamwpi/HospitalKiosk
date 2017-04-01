package core;

import models.path.Node;

/**
 * Created by mattm on 4/1/2017.
 */
public class NodeInUseException extends Exception {
    private Node n;

    public NodeInUseException(Node n) {
        this.n = n;
    }

    public void printStackTrace() {
        System.out.println("The node " + n.toString() + " is still in use and cannot be deleted!");
        super.printStackTrace();
    }
}
