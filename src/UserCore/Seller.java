package UserCore;
/**
 * Seller
 * <p>
 * A Seller class that's a child of User class which includes attributes for a seller
 *
 * @author Yulin Lin, 001
 * @version 11/3/2022
 */
public class Seller extends User{
    //TODO Arraylist of stores

    public Seller(String userName, String email, String pwd) {
        super(userName, email, pwd, Role.SELLER);
    }

    //TODO searching for a costumer + message manipulation
}
