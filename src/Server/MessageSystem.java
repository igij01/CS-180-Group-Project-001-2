package Server;

import UserCore.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class MessageSystem {
    private FullUser user;
    private final UserProfile userProfile;

    /**
     * convert string to ByteBuffer
     *
     * @param str the string to be converted
     * @return ByteBuffer equivalent of the string
     */
    protected static ByteBuffer toByteBuffer(String str) {
        return ByteBuffer.wrap(str.getBytes());
    }

    /**
     * convert ByteBuffer to string
     *
     * @param buffer buffer to be converted to String
     * @return String equivalent of the buffer
     * @throws IllegalRequestFormat when the request is blank
     */
    protected static String toStringFromBuffer(ByteBuffer buffer, int numRead) throws IllegalRequestFormat {
        byte[] data = new byte[numRead];
        System.arraycopy(buffer.array(), 0, data, 0, numRead);
        String str = new String(data).strip();
        if (str.isBlank())
            throw new IllegalRequestFormat("blank request!");
        return str;
    }

    /**
     * generate an exception message tagged with ! to be sent to the client
     *
     * @param e the exception
     * @return a ByteBuffer contains the message to be sent
     */
    protected static ByteBuffer sendException(Exception e, String requestType) {
        String str = '!' + e.getClass().getName() + '#' + requestType + '#' + '\"' + e.getMessage() + '\"';
        return toByteBuffer(str);
    }

    /**
     * get the command of the request
     *
     * @param raw the raw request in String
     * @return the command in String
     * @throws IllegalRequestFormat when the request does not have a # specifier
     */
    protected static String getCommand(String raw) throws IllegalRequestFormat {
        if (!raw.contains("#"))
            throw new IllegalRequestFormat(raw);
        return raw.substring(0, raw.indexOf('#'));
    }

    /**
     * split the parameter lists into a java String array
     * <br>
     * Note: This method only checks for situation which the number of parameters is <b>less</b>
     * than the number expected. Since the last parameter is allowed to contain comma by default, it's impossible
     * to check whether the parameters are more than expected. If the last parameter is not allowed to have comma,
     * use splitParamNoComma
     *
     * @param raw                the parameters waiting to be split
     * @param paramCountExpected the # of parameters expected
     * @return the list of String containing all the parameters on success
     * @throws IllegalParameter when the number of parameters is less than expected
     * @see MessageSystem#splitParamNoComma(String, int)
     */
    private static String[] splitParam(String raw, int paramCountExpected) throws IllegalParameter {
        if (paramCountExpected < 1)
            return new String[]{raw};
        ArrayList<String> array = new ArrayList<>();
        for (int i = 1; i < paramCountExpected; i++) {
            if (!raw.contains(","))
                throw new IllegalParameter(paramCountExpected, i);
            String toBeAdded = raw.substring(0, raw.indexOf(','));
            if (toBeAdded.isBlank()) {
                throw new IllegalParameter();
            }
            array.add(toBeAdded);
            raw = raw.substring(raw.indexOf(',') + 1);
        }
        array.add(raw);
        return array.toArray(new String[0]);
    }

    /**
     * split the parameter lists into a java String array and the last parameter is not allowed to contain comma
     *
     * @param raw                the raw string to be split
     * @param paramCountExpected the number of parameters expected
     * @return the list of String containing all the parameters on success
     * @throws IllegalCharacterInRequest when the number of parameters is less than expected
     * @throws IllegalParameter          when the last parameter contains comma either due to more elements than expected or
     *                                   the last parameter contains comma when it shouldn't
     */
    private static String[] splitParamNoComma(String raw, int paramCountExpected) throws IllegalCharacterInRequest,
            IllegalParameter {
        String[] array = splitParam(raw, paramCountExpected);
        if (array[array.length - 1].contains(","))
            throw new IllegalCharacterInRequest(Arrays.toString(array), ',');
        return array;
    }

    /**
     * initialize the message system with the initMessage
     *
     * @param initMessage the init message from the client
     * @param numRead     the num of bytes read
     * @throws InvalidPasswordException when password is wrong during login
     * @throws IllegalUserNameException when the username either does not exist during login or already taken
     * @throws EmailFormatException     when email format is wrong during account creation
     * @throws IllegalParameter         when the role passed in is not seller nor buyer or the parameter number was differed
     *                                  from expected
     * @throws IllegalRequestFormat     when the command passed in is not a login or register or not the right format
     */
    public MessageSystem(ByteBuffer initMessage, int numRead) throws
            InvalidPasswordException, IllegalUserNameException, EmailFormatException, IllegalRequestFormat {
        String message = toStringFromBuffer(initMessage, numRead);
        String command = getCommand(message);
        if (command.equalsIgnoreCase("login")) {
            String[] info = splitParam(message.substring(message.indexOf('#') + 1), 2);
            this.user = logIn(info[0], info[1]);
        } else if (command.equalsIgnoreCase("register")) {
            String[] info = splitParam(message.substring(message.indexOf('#') + 1), 4);
            this.user = register(info[0], info[1], info[2], info[3]);
        } else
            throw new IllegalRequestFormat(message + "- is not a login or register request!");
        userProfile = new UserProfile(this.user);
    }

    /**
     * log in the user
     *
     * @param username the username
     * @param password the password
     * @throws InvalidPasswordException when the password is incorrect
     * @throws IllegalUserNameException when the username does not exist
     */
    private FullUser logIn(String username, String password) throws
            InvalidPasswordException, IllegalUserNameException {
        return PublicInformation.login(username, password);
    }

    /**
     * register the user
     *
     * @param role     the role of the user
     * @param username the username
     * @param email    the email
     * @param password the password
     * @return the FullUser instance on success
     * @throws IllegalParameter         when the role specify is not buyer nor seller
     * @throws EmailFormatException     when the email format is wrong
     * @throws IllegalUserNameException when the username is already taken
     */
    private FullUser register(String role, String username, String email, String password) throws
            IllegalParameter, EmailFormatException, IllegalUserNameException {
        if (role.equalsIgnoreCase("seller")) {
            return new FullSeller(username, email, password);
        } else if (role.equalsIgnoreCase("buyer")) {
            return new FullBuyer(username, email, password);
        } else {
            throw new IllegalParameter("buyer/seller", role);
        }
    }

    public ByteBuffer processRequest(ByteBuffer buffer, int numRead) {
        String message = toStringFromBuffer(buffer, numRead);
        String command = message.substring(0, message.indexOf('#'));
        switch (command) {
            case "displayUserProfile":
                return userProfile.displayUserProfile();
            default:
                return toByteBuffer("!");
        }
    }
}
