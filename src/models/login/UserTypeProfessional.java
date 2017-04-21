package models.login;

/**
 * Created by mattm on 4/21/2017
 */
public class UserTypeProfessional extends AbstractUserType {

    public UserTypeProfessional() {
        super(1, "professional", "faulkner");
    }

    @Override
    public String toString() {
        return "Professional";
    }
}
