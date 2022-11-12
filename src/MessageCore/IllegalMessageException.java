package MessageCore;

/**
 * IllegalMessageException
 * throw when a message cannot be put into a conversation
 */
public class IllegalMessageException extends IllegalArgumentException {
    public IllegalMessageException(String m) {
        super(m);
    }

    public IllegalMessageException() {
        this("The message have different participant than conversation!");
    }
}
