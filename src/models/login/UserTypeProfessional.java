package models.login;

import controllers.NavigationDrawer.MenuItem;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by mattm on 4/21/2017
 */
public class UserTypeProfessional extends AbstractUserType {

    public UserTypeProfessional() {
        super("professional", "faulkner");
    }

    @Override
    public String toString() {
        return "Professional";
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public Collection<MenuItem.EnumMenuItem> getAvailableOptions() {
        return Arrays.asList(MenuItem.EnumMenuItem.About, MenuItem.EnumMenuItem.GetDirections,
                MenuItem.EnumMenuItem.HelpInfo, MenuItem.EnumMenuItem.Logout, MenuItem.EnumMenuItem.UserDir);
    }

    @Override
    public String getWelcomeMessage() {
        return "Signed in as Professional";
    }
}
