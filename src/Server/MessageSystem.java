package Server;

import Protocol.*;
import UserCore.*;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.*;

/**
 * MessageSystem
 * <p>
 * class responsible for interpreting the packet send by the client and interact with the message and user core
 * accordingly
 *
 * @author Yulin Lin, 001
 * @version 11/21/2022
 */
public class MessageSystem {
    protected static Selector selector = null;
    protected static Hashtable<FullUser, SelectionKey> userToKey = new Hashtable<>();
    private final FullUser user;
    private final UserProfile userProfile;
    private final MessageFunctionality message;

    /**
     * set the selector static var used to call Notification Factory
     *
     * @param selector the global selector
     */
    protected static void setSelector(Selector selector) {
        MessageSystem.selector = selector;
    }

    /**
     * form a response DataPacket and
     *
     * @param protocolRequestType the request type
     * @param params              the parameters
     * @return ByteBuffer equivalent of the string
     */
    protected static ByteBuffer toByteBufferPacket(ProtocolResponseType protocolRequestType, String... params) {
        ResponsePacket packet = new ResponsePacket(protocolRequestType, params);
        return ByteBuffer.wrap(Objects.requireNonNull(DataPacket.serialize(packet)));
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
     * generate an exception packet to be sent to the client
     *
     * @param e           the exception
     * @param requestType the request that caused the exception
     * @return a ByteBuffer contains the serialized form of the exception packet to be sent
     */
    protected static ByteBuffer sendException(Exception e, ProtocolRequestType requestType) {
        ErrorPacket packet = new ErrorPacket(requestType, e);
        return ByteBuffer.wrap(Objects.requireNonNull(DataPacket.serialize(packet)));
    }

    /**
     * generate an exception packet to be sent to the client
     *
     * @param e the exception
     * @return a ByteBuffer contains the serialized form of the exception packet to be sent
     */
    protected static ByteBuffer sendException(Exception e) {
        ErrorPacket packet = new ErrorPacket(e);
        return ByteBuffer.wrap(Objects.requireNonNull(DataPacket.serialize(packet)));
    }

    /**
     * run the notification thread on specific key
     *
     * @param key     the key
     * @param packets packet to be sent
     * @see #userToKey
     */
    protected static void runNotificationThread(SelectionKey key, ByteBuffer... packets) {
        for (ByteBuffer buffer : packets) {
            NotificationFactory.runNotificationThread(selector, key, buffer);
        }
    }

    /**
     * run the notification thread all users
     *
     * @param packets packet to be sent
     * @see #userToKey
     */
    protected static void runNotificationThread(ByteBuffer... packets) {
        for (ByteBuffer buffer : packets) {
            NotificationFactory.runNotificationThread(selector, buffer);
        }
    }

    protected static void runNotificationThreadUpdateMessage(String olderUsername, String newUserName) {
        for (Map.Entry<FullUser, SelectionKey> entry : userToKey.entrySet()) {
            NotificationFactory.runNotificationThread(selector, entry.getValue(),
                    MessageFunctionality.userToMessageFunc.get(entry.getKey()).displayConversationTitles());
        }
        for (Map.Entry<FullUser, String> entry : MessageFunctionality.userCurrentSelection.entrySet()) {
            if (entry.getValue().equals(olderUsername)) {
                ArrayList<String> list = new ArrayList<>(List.of(entry.getKey().printConversation(newUserName)));
                list.add(0, newUserName);
                NotificationFactory.runNotificationThread(selector, userToKey.get(entry.getKey()),
                        toByteBufferPacket(ProtocolResponseType.CONVERSATION_USERNAME_CHANGE,
                                list.toArray(new String[0])));
                MessageFunctionality.userToMessageFunc.get(entry.getKey()).updateCurrentConversationField(newUserName);
            }
        }
    }

    /**
     * run the notification thread only buyer/seller
     *
     * @param buyer whether you want to send it to buyer(true) or seller(false)
     * @see #userToKey
     */
    protected static void runNotificationThreadPublicInfo(boolean buyer) {
        for (Map.Entry<FullUser, SelectionKey> entry : userToKey.entrySet()) {
            if (!buyer ^ entry.getKey() instanceof FullBuyer) {
                NotificationFactory.runNotificationThread(selector, entry.getValue(),
                        PublicInfo.sendPublicInfo(entry.getKey()));
            }
        }
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
    public MessageSystem(ByteBuffer initMessage, int numRead, SelectionKey key) throws
            InvalidPasswordException, IllegalUserNameException, EmailFormatException, IllegalRequestFormat {
        DataPacket initPacket = DataPacket.packetDeserializeServer(initMessage);
        if (initPacket != null) {
            if (initPacket.protocolRequestType == ProtocolRequestType.LOGIN) {
                this.user = logIn(initPacket.args[0], initPacket.args[1]);
                userToKey.put(this.user, key);
            } else if (initPacket.protocolRequestType == ProtocolRequestType.REGISTER) {
                this.user = register(initPacket.args[0], initPacket.args[1], initPacket.args[2], initPacket.args[3]);
                userToKey.put(this.user, key);
                runNotificationThread(PublicInfo.sendAllUsernames());
                runNotificationThreadPublicInfo(!(this.user instanceof FullBuyer));
            } else
                throw new IllegalRequestFormat((initPacket.protocolRequestType.toString()) +
                        " - is not a login or register request!");
        } else
            throw new IllegalRequestFormat("blank request!");
        userProfile = new UserProfile(this.user);
        message = new MessageFunctionality(this.user);
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

    /**
     * Forcibly log the user out when the user disconnect
     */
    protected void userLogOut() {
        PublicInformation.logout(this.user);
    }

    /**
     * main method responsible for processing the request
     *
     * @param buffer the input buffer
     * @return the ArrayList<ByteBuffer> as a response
     */
    public ArrayList<ByteBuffer> processRequest(ByteBuffer buffer) {
        ArrayList<DataPacket> packets = new ArrayList<>();
        ArrayList<ByteBuffer> response = new ArrayList<>();
        boolean repeat = true;
        DataPacket dataPacket = DataPacket.packetDeserializeServer(buffer);
        System.out.println(dataPacket);
        if (dataPacket != null)
            packets.add(dataPacket);
        else
            repeat = false;

        while (repeat) {
            dataPacket = DataPacket.packetDeserializeServer(null);
            if (dataPacket != null)
                packets.add(dataPacket);
            else
                repeat = false;
        }

        assert packets.size() > 0;
        for (DataPacket packet : packets) {
            try {
                if (!this.user.loginStatus())
                    throw new IllegalUserLoginStatus("The user is not logged in!"); //should never happen
                response.add(switch (Objects.requireNonNull(packet).protocolRequestType) {
                    case DISPLAY_PROFILE -> userProfile.displayUserProfile();
                    case CHANGE_USERNAME -> userProfile.changeUsername(packet.args);
                    case LOGIN, REGISTER -> throw new IllegalRequestFormat(packet.protocolRequestType +
                            "- is not allowed here!");
                    case CHANGE_EMAIL -> userProfile.changeEmail(packet.args);
                    case BLOCK_USER -> userProfile.blockUser(packet.args);
                    case UNBLOCK_USER -> userProfile.unblockUser(packet.args);
                    case INVIS_USER -> userProfile.invisUser(packet.args);
                    case UNINVIS_USER -> userProfile.uninvisUser(packet.args);
                    case FILTER_WORD -> userProfile.addFilterWord(packet.args);
                    case UNFILTER_WORD -> userProfile.removeAFilteredWord(packet.args);
                    case CHANGE_CENSOR_PATTERN -> userProfile.replaceFilterPattern(packet.args);
                    case TURN_ON_CENSOR_MODE -> userProfile.toggleFilterMode(false);
                    case TURN_OFF_CENSOR_MODE -> userProfile.toggleFilterMode(true);
                    case CREATE_STORE -> userProfile.createStore(packet.args);
                    case DELETE_ACCOUNT -> userProfile.deleteAccount(packet.args);
                    case RECOVER_ACCOUNT -> userProfile.recoverAccount();
                    case LOGOUT -> userProfile.logout();
                    case FORCE_LOGOUT -> userProfile.confirmLogOut();

                    case REQUEST_PUBLIC_INFO -> PublicInfo.sendPublicInfo(this.user);
                    case REQUEST_DASHBOARD -> PublicInfo.sendDashBoard(this.user, packet.args);

                    case SEND_MESSAGE_USER -> message.sendMessageUser(packet.args);
                    case SEND_MESSAGE_STORE -> message.sendMessageStore(packet.args);
                    case EDIT_MESSAGE -> message.editMessage(packet.args);
                    case DELETE_MESSAGE -> message.deleteMessage(packet.args);
                    case DISPLAY_CONVERSATION_TITLES -> message.displayConversationTitles();
                    case DISPLAY_CONVERSATION -> message.displayConversation(packet.args);
                    case EXPORT_CONVERSATION -> message.exportCSV(packet.args);
                    case EXPORT_ALL_CONVERSATION -> message.exportCSVAll();
                });
            } catch (Exception e) {
                e.printStackTrace();
                response.add(sendException(e, packet.protocolRequestType));
            }
        }
        return response;
    }
}
