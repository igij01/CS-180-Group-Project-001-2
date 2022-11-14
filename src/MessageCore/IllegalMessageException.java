package MessageCore;

/**
 * IllegalMessageException
 * throw when a message cannot be put into a conversation
 *
 * @author Yulin Lin, 001
 * @version 11/14/2022
 */
public class IllegalMessageException extends IllegalArgumentException {
    public IllegalMessageException(String m) {
        super(m);
    }

    public IllegalMessageException() {
        this("The message have different participant than conversation!");
    }
}
