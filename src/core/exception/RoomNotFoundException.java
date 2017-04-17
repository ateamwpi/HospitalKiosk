package core.exception;

import models.path.Node;

/**
 * Created by mattm on 4/1/2017.
 */
public class RoomNotFoundException extends Exception {
    private String s;

    public RoomNotFoundException(String s) {
        this.s = s;
    }

    public void printStackTrace() {
        System.out.println("The room " + s + " was not found!");
        super.printStackTrace();
    }
}
