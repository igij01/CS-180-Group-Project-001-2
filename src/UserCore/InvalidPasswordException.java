package UserCore;

public class InvalidPasswordException extends Exception {
    /** InvalidPasswordException
     * throw this when a user tries to login but the password doesn't match the username
     *
     */
    public InvalidPasswordException() {
        super("INCORRECT PASSCODE");
    }
}
