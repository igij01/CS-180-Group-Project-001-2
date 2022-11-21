package Server;

public class IllegalRequestFormat extends IllegalArgumentException {
    public IllegalRequestFormat(String illegalMessage) {
        super("The request is in the wrong format! - " + illegalMessage);
    }
}
