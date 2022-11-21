package Server;

public class IllegalCharacterInRequest extends IllegalRequestFormat {
    public IllegalCharacterInRequest(String param, char separator) {
        super("params - " + param + "contains " + separator);
    }
}
