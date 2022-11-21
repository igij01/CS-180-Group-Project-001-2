package Server;

/**
 * IllegalCharacter
 * <p>
 * throw when the request contains illegal character like comma when it's not allowed
 *
 * @author Yulin Lin, 001
 * @version 11/21/2022
 */
public class IllegalCharacter extends IllegalRequestFormat {
    public IllegalCharacter(String param, char separator) {
        super("params - " + param + "contains " + separator);
    }
}
