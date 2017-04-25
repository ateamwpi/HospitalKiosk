package models.login;

import java.util.ArrayList;

/**
 * Created by mattm on 4/21/2017
 */
public class LoginManager {

    private final ArrayList<AbstractUserType> accounts = new ArrayList<>();
    private final ArrayList<ILoginObserver> observers = new ArrayList<>();
    private final AbstractUserType defaultAccount;
    private AbstractUserType currentState;

    public LoginManager() {
        defaultAccount = new UserTypeGuest();
        addUserType(new UserTypeProfessional());
        addUserType(new UserTypeAdministrator());
        currentState = defaultAccount;
        System.out.println("Loaded UserTypes: " + accounts);
        System.out.println("Selected UserType: " + currentState);
    }

    public void attachObserver(ILoginObserver o) {
        observers.add(o);
    }

    private void notifyObservers() {
        for(ILoginObserver l : observers) {
            l.onAccountChanged();
        }
    }

    private void addUserType(AbstractUserType u) {
        accounts.add(u);
    }

    public boolean tryLogin(String username, String password) {
        for(AbstractUserType userType : accounts) {
            if(!userType.equals(currentState) && userType.tryLogin(username, password)) {
                currentState = userType;
                notifyObservers();
                return true;
            }
        }
        return false;
    }

    public void logout() {
        currentState = defaultAccount;
        notifyObservers();
    }

    public AbstractUserType getState() {
        return currentState;
    }
}
