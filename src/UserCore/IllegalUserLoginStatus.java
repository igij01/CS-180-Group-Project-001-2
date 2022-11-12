package UserCore;

/**
 * IllegalUserLoginStatus
 * <p>
 * the exception occurs when the user is trying to perform operation without login first
 */
public class IllegalUserLoginStatus extends IllegalStateException {
    public IllegalUserLoginStatus(String message) {
        super(message);
    }

    public IllegalUserLoginStatus() {
        this("The user is not logged in!");
    }
}
