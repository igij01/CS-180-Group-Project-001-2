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

    /**
     * create an instance of Seller. Note that users can't have the same name
     * but one email can have multiple users' assoc. with it
     *
     * @param username the name of the user
     * @param email    the email address assoc. with the user
     * @param pwd      the password of the user
     * @throws EmailFormatException     when email address are not put in the right format
     * @throws IllegalUserNameException when the passed in username is already taken
     */
    protected Seller(String username, String email, String pwd) throws IllegalUserNameException, EmailFormatException {
        super(username, email, pwd, Role.SELLER);
    }

}
