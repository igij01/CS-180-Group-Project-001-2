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
    String[2] - buyer
    1. array of store - [store1, store2...]
    2. array of seller(for searching purpose ONLY) - [seller1, seller2...]
    String[1] - seller
    1. array of buyer(for searching purpose ONLY) - [buyer1, buyer2...]
     */
    PUBLIC_INFO,
    /*

     */
    CONVERSATION_TITLES,
    /*
    String[]
    target_username
    username: message
    timestamp, edited flag
    username1: message
    timestamp, edited flag
    ...
     */
    CONVERSATION,
    CSV_EXPORT,
    LOGOUT_SUCCESS,
    ACCOUNT_DELETION,
    LOGIN_SUCCESS, DASHBOARD
}
