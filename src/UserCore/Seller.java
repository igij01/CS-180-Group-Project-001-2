package UserCore;
/**
 * Seller
 * <p>
 * A Seller class that's a child of User class
 * ONLY serve as a <b>placeholder</b> for a seller user
 *
 * @author Yulin Lin, 001
 * @version 11/3/2022
 */
public class Seller extends User {

    protected Seller(String userName, String email, String pwd) {
        super(userName, email, pwd, Role.SELLER);
    }

}
