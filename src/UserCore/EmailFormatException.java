package UserCore;

public class EmailFormatException extends IllegalArgumentException {
    public EmailFormatException(String email) {
        super(email + " is not a correct format!");
    }
}
