package UserCore;

import java.io.Serial;
import java.io.Serializable;

/**
 * Buyer
 * <p>
 * A child class of user that
 * ONLY serve as a <b>placeholder</b> for a buyer user
 *
 * @author Yulin Lin, 001
 * @version 11/3/2022
 */
public class Buyer extends User implements Serializable {
    // update this field everytime you update the field of the class
    // put in a random number or just increment the number
    @Serial
    private static final long serialVersionUID = 2L;
    protected Buyer(String username, String email, String pwd) {
        super(username, email, pwd, Role.BUYER);
    }
}
