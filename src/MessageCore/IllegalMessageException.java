package MessageCore;

/**
 * IllegalMessageException
 * throw when a message cannot be put into a conversation
 */
public class IllegalMessageException extends IllegalArgumentException {
    public IllegalMessageException(String m, Message message) {
        super(m + "\n" + message.fileToString());
    }

    public IllegalMessageException(Message message) {
        this("The message have different participant than conversation!", message);
    }
}
