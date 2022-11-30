package Protocol;

public enum Request {
    LOGIN (2),
    REGISTER (4),
    DISPLAY_PROFILE (0);

    public final int paramCount;
    private Request(int paramCount) {
        this.paramCount = paramCount;
    }
}