package UserCore;

public class FullBuyer extends FullUser {


    /**
     * Creates a new Full Buyer instance
     *
     * @param userName the name of the user
     * @param email    the email address assoc. with the user
     * @param pwd      the password of the user
     * @throws EmailFormatException     when email address are not put in the right format
     * @throws IllegalUserNameException when the passed in username is already taken
     */
    public FullBuyer(String userName, String email, String pwd) {
        super(new Buyer(userName, email, pwd));
    }


    public void viewDashboard() {

    }
}
