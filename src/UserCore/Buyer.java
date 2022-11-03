package UserCore;

/**
 * Buyer
 * <p>
 * A child class of user that contains additional features specific of a Buyer
 *
 * @author Yulin Lin, 001
 * @version 11/3/2022
 */
public class Buyer extends User{
    public Buyer(String username, String email, String pwd) {
        super(username, email, pwd, Role.BUYER);
    }

    //TODO search for seller + message manipulation
}
