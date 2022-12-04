package Protocol;

public enum ProtocolRequestType {
    LOGIN(2),
    REGISTER(4),
    DISPLAY_PROFILE(0),
    CHANGE_USERNAME(2);

    private final int paramCount;

    ProtocolRequestType(int paramCount) {
        this.paramCount = paramCount;
    }

    public int getParamCount() {
        return this.paramCount;
    }
}