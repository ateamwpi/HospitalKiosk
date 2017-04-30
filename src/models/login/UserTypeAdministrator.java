package models.login;

import controllers.NavigationDrawer.MenuItem;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by mattm on 4/21/2017
 */
public class UserTypeAdministrator extends AbstractUserType {

    public UserTypeAdministrator() {
        super("admin", "jade");
    }

    @Override
    public String toString() {
        return "Administrator";
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public Collection<MenuItem.EnumMenuItem> getAvailableOptions() {
        return Arrays.asList(MenuItem.EnumMenuItem.About, MenuItem.EnumMenuItem.GetDirections, MenuItem.EnumMenuItem.Logout,
                MenuItem.EnumMenuItem.ManageDir, MenuItem.EnumMenuItem.ManageMap, MenuItem.EnumMenuItem.SelectAlgo,
                MenuItem.EnumMenuItem.SelectKiosk, MenuItem.EnumMenuItem.SelectTimeout);
    }

    @Override
    public String getWelcomeMessage() {
        return "Signed in as Administrator";
    }
}
