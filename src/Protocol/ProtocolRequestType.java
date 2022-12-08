package Protocol;

public enum ProtocolRequestType {
    LOGIN(2),
    REGISTER(4),

    //PROFILE
    DISPLAY_PROFILE(0),
    CHANGE_USERNAME(2),
    CHANGE_EMAIL(2),
    BLOCK_USER(1),
    UNBLOCK_USER(1),
    INVIS_USER(1),
    UNINVIS_USER(1),
    FILTER_WORD(1),
    UNFILTER_WORD(1),
    CHANGE_CENSOR_PATTERN(1),
    TURN_ON_CENSOR_MODE(0),
    TURN_OFF_CENSOR_MODE(0),
    CREATE_STORE(1),
    DELETE_ACCOUNT(1),
    RECOVER_ACCOUNT(0),
    LOGOUT(0),
    FORCE_LOGOUT(0),

    //PUBLIC_INFO
    REQUEST_PUBLIC_INFO(0),
    REQUEST_DASHBOARD(1), //increasing order?

    //MESSAGE
    SEND_MESSAGE_USER(2),
    SEND_MESSAGE_STORE(2),
    EDIT_MESSAGE(3), //conversation NAME, message index, new message
    DELETE_MESSAGE(2),
    DISPLAY_CONVERSATION_TITLES(0),
    DISPLAY_CONVERSATION(1), //conversation NAME
    EXPORT_CONVERSATION(1), //conversation NAME
    EXPORT_ALL_CONVERSATION(0);


    private final int paramCount;

    ProtocolRequestType(int paramCount) {
        this.paramCount = paramCount;
    }

    public int getParamCount() {
        return this.paramCount;
    }
}