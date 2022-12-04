package Protocol;

public enum Request {
    //client request
    LOGIN (2),
    REGISTER (4),
    DISPLAY_PROFILE (0),
    CHANGE_USERNAME (2),


    //server response
    PROFILE (1);

    private final int paramCount;
    Request(int paramCount) {
        this.paramCount = paramCount;
    }
    public int getParamCount() {
        return this.paramCount;
    }
}