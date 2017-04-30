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
        return "Admin";
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public Collection<MenuItem.EnumMenuItem> getAvailableOptions() {
        return Arrays.asList(MenuItem.EnumMenuItem.ManageMap, MenuItem.EnumMenuItem.ManageDir,
                MenuItem.EnumMenuItem.SelectKiosk, MenuItem.EnumMenuItem.SelectTimeout,
                MenuItem.EnumMenuItem.SelectAlgo,

                MenuItem.EnumMenuItem.GetDirections, MenuItem.EnumMenuItem.UserDir,
                MenuItem.EnumMenuItem.HelpInfo, MenuItem.EnumMenuItem.About,

                MenuItem.EnumMenuItem.Logout);
    }

    @Override
    public String getWelcomeMessage() {
        return "Signed in as Admin";
    }
}
