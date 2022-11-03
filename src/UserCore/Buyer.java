package UserCore;

/**
 * Buyer
 * <p>
 * A child class of user that
 * ONLY serve as a <b>placeholder</b> for a buyer user
 *
 * @author Yulin Lin, 001
 * @version 11/3/2022
 */
public class Buyer extends User{
    public Buyer(String username, String email, String pwd) {
        super(username, email, pwd, Role.BUYER);
    }
}
