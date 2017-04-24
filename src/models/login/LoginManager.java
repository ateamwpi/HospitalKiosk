package models.login;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mattm on 4/21/2017
 */
public class LoginManager {

    private ArrayList<AbstractUserType> accounts = new ArrayList<>();
    private ArrayList<ILoginObserver> observers = new ArrayList<>();
    private AbstractUserType defaultAccount;
    private AbstractUserType currentState;

    public LoginManager() {
        this.defaultAccount = new UserTypeGuest();
        this.addUserType(new UserTypeProfessional());
        this.addUserType(new UserTypeAdministrator());
        this.currentState = defaultAccount;
        System.out.println("Loaded UserTypes: " + this.accounts);
        System.out.println("Selected UserType: " + this.currentState);
    }

    public void attachObserver(ILoginObserver o) {
        this.observers.add(o);
    }

    public void notifyObservers() {
        for(ILoginObserver l : this.observers) {
            l.onAccountChanged();
        }
    }

    private void addUserType(AbstractUserType u) {
        this.accounts.add(u);
        Collections.sort(this.accounts);
    }

    public boolean tryLogin(String username, String password) {
        for(AbstractUserType userType : this.accounts) {
            if(!userType.equals(this.currentState) && userType.tryLogin(username, password)) {
                System.out.println("accepted " + userType);
                this.currentState = userType;
                this.notifyObservers();
                return true;
            }
        }
        return false;
    }

    public void logout() {
        this.currentState = defaultAccount;
        this.notifyObservers();
    }

    public AbstractUserType getState() {
        return this.currentState;
    }
}
