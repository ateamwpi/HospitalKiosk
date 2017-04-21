package models.login;

/**
 * Created by mattm on 4/21/2017
 */
public abstract class AbstractUserType implements Comparable<AbstractUserType> {

    private final int permission;
    private final String username;
    private final String password;

    public AbstractUserType(int permission, String username, String password) {
        this.permission = permission;
        this.username = username;
        this.password = password;
    }

    public boolean tryLogin(String username, String password) {
        return this.username.equalsIgnoreCase(username) && this.password.equals(password);
    }

    public abstract String toString();

    @Override
    public int compareTo(AbstractUserType o) {
        return o.permission - this.permission;
    }
}
