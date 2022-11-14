package UserCore;

/**
 * IllegalUserLoginStatus
 * <p>
 * the exception occurs when the user is trying to perform operation without login first
 *
 * @author Yulin Lin, 001
 * @version 11/12/2022
 */
public class IllegalUserLoginStatus extends IllegalStateException {
    public IllegalUserLoginStatus(String message) {
        super(message);
    }

    public IllegalUserLoginStatus() {
        this("The user is not logged in!");
    }
}
