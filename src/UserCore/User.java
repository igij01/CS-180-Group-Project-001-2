package UserCore;

// Any associated methods must be enclosed in a new class that has user in a field.
// All the functions, messaging, accessing, login, searching, etc, that will be public methods
// must be included in the new class instead of User.java. User.java can contain additional attributes,
// but it only can have protected or lower access level methods for the sake of hiding the critical information,
// like password, from outside classes not in the UserCore package.

/**
 * User
 * <p>
 * A parent class for all types of user in the system.
 * This class and any children classes only serves as placeholder for ease of use
 *
 * @author Yulin Lin, 001
 * @version 11/3/2022
 */
class User {
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
    protected User(String userName, String email, String pwd, Role role) {
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
