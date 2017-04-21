package models.login;

/**
 * Created by mattm on 4/21/2017
 */
public class UserTypeAdministrator extends AbstractUserType {

    public UserTypeAdministrator() {
        super(2, "admin", "jade");
    }

    @Override
    public String toString() {
        return "Administrator";
    }
}
