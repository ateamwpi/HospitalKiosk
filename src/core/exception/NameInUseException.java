package core.exception;

/**
 * Created by mattm on 4/17/2017.
 */
public class NameInUseException extends Exception {

    String name;

    public NameInUseException(String name) {
        this.name = name;
    }

    public void printStackTrace() {
        System.out.println("The name " + name + " is already in use!");
        super.printStackTrace();
    }
}
