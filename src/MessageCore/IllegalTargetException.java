package MessageCore;

import UserCore.User;

/**
 * IllegalTargetException
 * <p>
 * an exception that throws when the target of a message is invalid
 *
 * @author Yulin Lin, 001
 * @version 11/5/2022
 */
public class IllegalTargetException extends IllegalArgumentException {
    public IllegalTargetException(String message) {
        super(message);
    }

    public IllegalTargetException(User user) {
        this(String.format("The user %s cannot be a valid receiver of the message " +
                "because he/she has the same role as you", User.userName(user)));
    }
}
