package models.login;

import controllers.NavigationDrawer.MenuItem;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by mattm on 4/21/2017
 */
public class UserTypeGuest extends AbstractUserType {

    public UserTypeGuest() {
        super("", "");
    }

    @Override
    public boolean tryLogin(String username, String password) {
        return true;
    }

    @Override
    public String toString() {
        return "Guest";
    }

    @Override
    public Collection<MenuItem.EnumMenuItem> getAvailableOptions() {
        return Arrays.asList(MenuItem.EnumMenuItem.About, MenuItem.EnumMenuItem.Login, MenuItem.EnumMenuItem.UserDir);
    }

    @Override
    public String getWelcomeMessage() {
        return "Not signed in";
    }
}
