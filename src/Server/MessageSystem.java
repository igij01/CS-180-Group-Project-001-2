package Server;

import Protocol.*;
import UserCore.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * MessageSystem
 * <p>
 * class responsible for interpreting the packet send by the client
 *
 * @author Yulin Lin, 001
 * @version 11/21/2022
 */
public class MessageSystem {
    private FullUser user;
    private final UserProfile userProfile;

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
     * deserialize serialized packet
     *
     * @param buffer the buffer that contains the serialized packet
     * @return the deserialized packet
     */
    public static DataPacket packetDeserialize(ByteBuffer buffer) {
        byte[] packet = buffer.array();
        try (ByteArrayInputStream in = new ByteArrayInputStream(packet);
             ObjectInputStream oin = new ObjectInputStream(in)) {
            return (DataPacket) oin.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
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
        DataPacket initPacket = packetDeserialize(initMessage);
        if (initPacket != null) {
            if (initPacket.protocolRequestType == ProtocolRequestType.LOGIN)
                this.user = logIn(initPacket.args[0], initPacket.args[1]);
            else if (initPacket.protocolRequestType == ProtocolRequestType.REGISTER)
                this.user = register(initPacket.args[0], initPacket.args[1], initPacket.args[2], initPacket.args[3]);
            else
                throw new IllegalRequestFormat((initPacket.protocolRequestType.toString()) +
                        "- is not a login or register request!");
        } else
            throw new IllegalRequestFormat("blank request!");
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

    /**
     * main method responsible for processing the request
     *
     * @param buffer the input buffer
     * @return the ByteBuffer as a response
     */
    public ByteBuffer processRequest(ByteBuffer buffer) {
        DataPacket packet = packetDeserialize(buffer);
        try {
            return switch (Objects.requireNonNull(packet).protocolRequestType) {
                case DISPLAY_PROFILE -> userProfile.displayUserProfile();
                case CHANGE_USERNAME -> userProfile.changeUsername(packet.args);
                //case ""
                case LOGIN, REGISTER ->
                        throw new IllegalRequestFormat(packet.protocolRequestType + "- is not allowed here!");
            };
        } catch (Exception e) {
            return sendException(e, packet.protocolRequestType);
        }
    }
}
