package Protocol;

public enum Request {
    LOGIN (2),
    REGISTER (4),
    DISPLAY_PROFILE (0),
    CHANGE_USERNAME(2);

    private final int paramCount;
    Request(int paramCount) {
        this.paramCount = paramCount;
    }
    public int getParamCount() {
        return this.paramCount;
    }
}