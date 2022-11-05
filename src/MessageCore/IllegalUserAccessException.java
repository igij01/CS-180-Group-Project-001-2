package MessageCore;
/**
 * IllegalUserAccessException
 * <p>
 * an exception that throws when the user cannot perform a certain action
 *
 * @author Yulin Lin, 001
 * @version 11/5/2022
 */
public class IllegalUserAccessException extends IllegalArgumentException {
    public IllegalUserAccessException(String message) {
        super(message);
    }

    public IllegalUserAccessException() {
        this("The user does not have permission to perform this action");
    }
}
