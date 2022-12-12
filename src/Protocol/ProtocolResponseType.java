package Protocol;
/**
 * ProtocolResponseType
 * <br>
 * A enum for all the response that occurs on the server side
 *
 * @author Yulin Lin, 001
 * @version 12/11/2022
 */
public enum ProtocolResponseType {
    /**
    list of usernames: {user1, user2, user3, etc...} <br>
     **/
    USER_NAMES,
    /**
    only the things after ":" is in the array<br>
    String[6] - buyer<br>
    (1. account info): buyer '\n' mail@mail.com '\n' BUYER<br>
    (2. blocked users): <br>
    (3. made invisible users): <br>
    (4. censor mode): ON<br>
    (5. filtered words): []<br>
    (6. censored pattern): *<br>
     <br>
    String[7] - seller<br>
    (1. account info): buyer '\n' mail@mail.com '\n' SELLER '\n' ACCOUNT WAITING TO BE DELETED!<br>
    (2. blocked users): <br>
    (3. made invisible users): <br>
    (4. censor mode): OFF<br>
    (5. filtered words): []<br>
    (6. censored pattern): *<br>
    (7. Store): []<br>
     **/
    PROFILE,
    /**
    String[2] - buyer<br>
    1. array of store - [store1, store2...]<br>
    2. array of seller(for searching purpose ONLY) - [seller1, seller2...]<br>
    String[1] - seller<br>
    1. array of buyer(for searching purpose ONLY) - [buyer1, buyer2...]<br>
     **/
    PUBLIC_INFO,
    /*

     */
    CONVERSATION_TITLES,
    /**
    String[]<br>
    target_username<br>
    username: message<br>
    timestamp, edited flag<br>
    username1: message<br>
    timestamp, edited flag<br>
    ...
     **/
    CONVERSATION,
    CONVERSATION_USERNAME_CHANGE,
    CSV_EXPORT,
    LOGOUT_SUCCESS,
    ACCOUNT_DELETION,
    LOGIN_SUCCESS,
    DASHBOARD
}
