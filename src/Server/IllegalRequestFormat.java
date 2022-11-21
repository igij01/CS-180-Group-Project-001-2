package Server;

/**
 * IllegalRequestFormat
 * <p>
 * throw when the request format is wrong
 *
 * @author Yulin Lin, 001
 * @version 11/21/2022
 */
public class IllegalRequestFormat extends IllegalArgumentException {
    public IllegalRequestFormat(String illegalMessage) {
        super("The request is in the wrong format! - " + illegalMessage);
    }
}
