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
    private boolean loginStatus;

    /**
     * create an instance of user. Note that users can't have the same name
     * but one email can have multiple users' assoc. with it
     *
     * @param userName the name of the user
     * @param email the email address assoc. with the user
     * @param pwd the password of the user
     * @param role the role of the user
     * @throws EmailFormatException when email address are not put in the right format
     */
    public User(String userName, String email, String pwd, Role role) {
        //TODO add checking mechanism to check if the name is already taken
        this.userName = userName;
        if (!email.matches("\\b[\\w.%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\\b"))
            throw new EmailFormatException(email);
        this.email = email;
        this.pwd = pwd;
        this.role = role;
        loginStatus = false;
    }

    protected String getUserName() {
        return userName;
    }

    protected void setUserName(String userName) {
        this.userName = userName;
    }

    protected String getEmail() {
        return email;
    }

    protected void setEmail(String email) {
        this.email = email;
    }

    protected String getPwd() {
        return pwd;
    }

    protected void setPwd(String pwd) {
        this.pwd = pwd;
    }

    protected Role getRole() {
        return role;
    }

    protected void setRole(Role role) {
        this.role = role;
    }

    protected boolean isLoginStatus() {
        return loginStatus;
    }

    protected void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }
}
