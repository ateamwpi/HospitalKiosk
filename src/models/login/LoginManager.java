package models.login;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mattm on 4/21/2017
 */
public class LoginManager {

    private ArrayList<AbstractUserType> states = new ArrayList<>();
    private AbstractUserType currentState;

    public LoginManager() {
        this.addUserType(new UserTypeGuest());
        this.addUserType(new UserTypeProfessional());
        this.addUserType(new UserTypeAdministrator());
        this.tryLogin("", "");
        System.out.println("Loaded UserTypes: " + this.states);
        System.out.println("Selected UserType: " + this.currentState);
    }

    private void addUserType(AbstractUserType u) {
        this.states.add(u);
        Collections.sort(this.states);
    }

    public boolean tryLogin(String username, String password) {
        for(AbstractUserType userType : this.states) {
            if(userType.tryLogin(username, password)) {
                this.currentState = userType;
                return true;
            }
        }
        return false;
    }

    public String getState() {
        return this.currentState.toString();
    }
}
