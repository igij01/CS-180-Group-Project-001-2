package UserCore;

/**
 * IllegalUserNameException
 * <p>
 * throw when the username is illegal
 *
 * @author Yulin, Lin
 * @version 11/5/2022
 */
public class IllegalUserNameException extends IllegalArgumentException {

    /**
     * the message will be in the format of "the name %s is already taken!"
     *
     * @param invalidName the invalid name
     */
    public IllegalUserNameException(String invalidName) {
        super(String.format("the name %s is already taken!", invalidName));
    }
}
