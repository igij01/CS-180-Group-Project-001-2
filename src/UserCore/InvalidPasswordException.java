package UserCore;

/**
 * InvalidPasswordException
 * <p>
 * <p>
 * throw this when a user tries to login but the password doesn't match the username
 *
 * @author Samson Tesfagiorgis, 001
 * @version 11/11/22
 */
public class InvalidPasswordException extends Exception {
    public InvalidPasswordException() {
        super("INCORRECT PASSCODE");
    }
}
