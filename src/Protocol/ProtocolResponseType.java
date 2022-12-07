package Protocol;

public enum ProtocolResponseType {
    /*
    list of usernames: {user1, user2, user3, etc...}
     */
    USER_NAMES,
    /*
    String[6] - buyer
    1. account info: UserName = 'buyer', Email = 'mail@mail.com', role = BUYER
    2. blocked users: []
    3. made invisible users: []
    4. censor mode: ON
    5. filtered words: []
    6. censored pattern: *

    String[7] - seller
    1. account info: UserName = 'seller', Email = 'mail@mail.com', role = SELLER
    2. blocked users: []
    3. made invisible users: []
    4. censor mode: ON
    5. filtered words: []
    6. censored pattern: *
    7. Store: []
     */
    PROFILE,
    /*

     */
    PUBLIC_INFO,
    CONVERSATION_TITLES,
    CONVERSATION,
    CSV_EXPORT,
    LOGOUT_SUCCESS,
    ACCOUNT_DELETION,
    LOGIN_SUCCESS, DASHBOARD
}
