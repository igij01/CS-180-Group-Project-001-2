package MessageCore;

public class IllegalUserAccessException extends Exception {
    public IllegalUserAccessException(String message) {
        super(message);
    }

    public IllegalUserAccessException() {
        this("The user does not have permission to perform this action");
    }
}
