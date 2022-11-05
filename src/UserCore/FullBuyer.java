package UserCore;

public class FullBuyer extends FullUser {


    /**
     * Creates a new Full User instance
     *
     * @param userName the name of the user
     * @param email    the email address assoc. with the user
     * @param pwd      the password of the user
     * @throws EmailFormatException when email address are not put in the right format
     */
    public FullBuyer(String userName, String email, String pwd) {
        super(userName, email, pwd, Role.BUYER);
    }


    public void viewDashboard() {

    }
}
