package models.login;

import controllers.MapView.MapView.MenuItem;

import java.util.Collection;
import java.util.Objects;

/**
 * Created by mattm on 4/21/2017
 */
public abstract class AbstractUserType {

    private final String username;
    private final String password;

    AbstractUserType(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean tryLogin(String username, String password) {
        return this.username.equalsIgnoreCase(username) && this.password.equals(password);
    }

    public abstract String toString();

    public abstract Collection<MenuItem.EnumMenuItem> getAvailableOptions();

    public abstract String getWelcomeMessage();

    @Override
    public boolean equals(Object other) {
        return other instanceof AbstractUserType && Objects.equals(((AbstractUserType) other).username, username);
    }
}
