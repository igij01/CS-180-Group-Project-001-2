package Server;

/**
 * InvalidActionException
 * <p>
 * throw when an action is invalid and can't be described using other exceptions(like unblock a user that's not blocked)
 * , especially those that are normally returned a false flag upon encounter
 *
 * @author Yulin Lin,001
 * @version 12/4/2022
 */
public class InvalidActionException extends IllegalArgumentException {
    public InvalidActionException(String message) {
        super(message);
    }
}
