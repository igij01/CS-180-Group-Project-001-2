package UserCore;

import java.io.Serial;
import java.io.Serializable;

/**
 * Seller
 * <p>
 * A Seller class that's a child of User class
 * ONLY serve as a <b>placeholder</b> for a seller user
 *
 * @author Yulin Lin, 001
 * @version 11/3/2022
 */
public class Seller extends User implements Serializable {
    // update this field everytime you update the field of the class
    // put in a random number or just increment the number
    @Serial
    private static final long serialVersionUID = 2L;
    protected Seller(String userName, String email, String pwd) {
        super(userName, email, pwd, Role.SELLER);
    }

}
