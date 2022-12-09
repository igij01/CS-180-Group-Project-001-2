package UserCore;

// Any associated methods must be enclosed in a new class that has user in a field.
// All the functions, messaging, accessing, login, searching, etc, that will be public methods
// must be included in the new class instead of User.java. User.java can contain additional attributes,
// but it only can have protected or lower access level methods for the sake of hiding the critical information,
// like password, from outside classes not in the UserCore package.
// However, you can have static public method for public information, like role, username, etc.
// As a general rule of thumb, for any information that you think is ok or even want all other user to see,
// it's ok to have a static public method to put less strain on user functionality group.
// This rule applies for all the children classes as well. 

import java.io.Serial;
import java.io.Serializable;

/**
 * User
 * <p>
 * A parent class for all types of user in the system.
 * This class and any children classes only serves as placeholder for ease of use
 *
 * @author Yulin Lin, 001
 * @version 11/3/2022
 */
public class User implements Serializable {
    // update this field everytime you update the field of the class
    // put in a random number or just increment the number
    @Serial
    private static final long serialVersionUID = 2L;
    private String userName;
    private String email;
    private String pwd;
    private Role role;
    private volatile boolean loginStatus;
    private volatile boolean waitingToBeDeleted; //TODO do we need to RE-DELETE account once a server shutdown occurred?

    /**
     * create an instance of user. Note that users can't have the same name
     * but one email can have multiple users' assoc. with it
     *
     * @param userName the name of the user
     * @param email    the email address assoc. with the user
     * @param pwd      the password of the user
     * @param role     the role of the user
     * @throws EmailFormatException     when email address are not put in the right format
     * @throws IllegalUserNameException when the passed in username is already taken
     */
    protected User(String userName, String email, String pwd, Role role) throws IllegalUserNameException,
            EmailFormatException {
        if (PublicInformation.listOfUsersNames.contains(userName))
            throw new IllegalUserNameException(userName);
        this.userName = userName;
        if (!email.matches("\\b[\\w.%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\\b"))
            throw new EmailFormatException(email);
        this.email = email;
        this.pwd = pwd;
        this.role = role;
        loginStatus = true;
        waitingToBeDeleted = false;
        PublicInformation.addListOfUsersNames(userName);
    }

    protected String getUserName() {
        return userName;
    }

    /**
     * set the username and add it to the list of usernames while delete the old one
     *
     * @param userName the new username
     * @throws IllegalUserNameException when the new name is already taken
     */
    protected void setUserName(String userName) {
        if (PublicInformation.listOfUsersNames.contains(userName))
            throw new IllegalUserNameException(userName);
        PublicInformation.listOfUsersNames.remove(this.userName);
        this.userName = userName;
        PublicInformation.listOfUsersNames.add(this.userName);
    }

    protected String getEmail() {
        return email;
    }

    /**
     * change the email to the new email
     *
     * @param email the new email
     * @throws EmailFormatException when the email format is wrong
     */
    protected void setEmail(String email) {
        if (!email.matches("\\b[\\w.%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\\b"))
            throw new EmailFormatException(email);
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

    protected boolean isWaitingToBeDeleted() {
        return this.waitingToBeDeleted;
    }

    protected void setWaitingDeletionStatus(boolean status) {
        this.waitingToBeDeleted = status;
    }

    public static String userName(User user) {
        return user.userName;
    }

    public static Role checkRole(User user) {
        return user.role;
    }

    public static boolean isLogIn(User user) {
        return user.loginStatus;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof User) {
            User user = (User) obj;
            return userName.equals(user.userName) && email.equals(user.email)
                    && pwd.equals(user.pwd) && role == user.role;
        }
        return false;
    }

    @Override
    public String toString() {
        return "UserName = '" + userName + '\'' +
                ", Email = '" + email + '\'' +
                ", role = " + role +
                (waitingToBeDeleted ? ", ACCOUNT WAITING TO BE DELETED!" : "");
    }

    public String serverToString() {
        return String.format("%s\n%s\n%s%s", userName, email, role,
                (waitingToBeDeleted ? "\nACCOUNT WAITING TO BE DELETED!" : ""));
    }
}
