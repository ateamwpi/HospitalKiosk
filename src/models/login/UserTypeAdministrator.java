package models.login;

import controllers.mapView.MenuItem;

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
    public Collection<MenuItem.EnumMenuItem> getAvailableOptions() {
        return Arrays.asList(MenuItem.EnumMenuItem.About, MenuItem.EnumMenuItem.Logout, MenuItem.EnumMenuItem.ManageDir, MenuItem.EnumMenuItem.ManageMap, MenuItem.EnumMenuItem.SelectAlgo, MenuItem.EnumMenuItem.SelectKiosk);
    }

    @Override
    public String getWelcomeMessage() {
        return "Signed in as Administrator";
    }
}
