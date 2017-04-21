package models.login;

/**
 * Created by mattm on 4/21/2017
 */
public class UserTypeGuest extends AbstractUserType {

    public UserTypeGuest() {
        super(0, "", "");
    }

    @Override
    public boolean tryLogin(String username, String password) {
        return true;
    }

    @Override
    public String toString() {
        return "Guest";
    }
}
