package Protocol;

public enum Request {
    LOGIN (2),
    REGISTER (4),
    DISPLAY_PROFILE (0),
    CHANGE_USERNAME(2);

    public final int paramCount;
    private Request(int paramCount) {
        this.paramCount = paramCount;
    }
}