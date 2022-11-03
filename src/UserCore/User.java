package UserCore;

import java.util.Objects;

/**
 * User
 * <p>
 * A parent class for all types of user in the system
 *
 * @author Yulin Lin, 001
 * @version 11/3/2022
 */
public class User {
    //TODO arraylist of messages
    private String userName;
    private String email;
    private String pwd;
    private Role role;

    public User(String userName, String email, String pwd, Role role) {
        //TODO add checking mechanism to check if the name is already taken
        this.userName = userName;
        //TODO add checking mechanism to check if the email format is valid and if the email is already taken
        this.email = email;
        this.pwd = pwd;
        this.role = role;
    }

    //TODO message manipulations
}
