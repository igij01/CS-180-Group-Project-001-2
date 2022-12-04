package UserCore;

/**
 * EmailFormatException
 * <p>
 * an exception that throws when user's email is in an invalid format
 *
 * @author Yulin Lin, 001
 * @version 11/3/2022
 */
public class EmailFormatException extends IllegalArgumentException {
    public EmailFormatException(String email) {
        super(email + " is not a correct format!");
    }
}
