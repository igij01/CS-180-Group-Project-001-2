package Server;

import Protocol.ProtocolResponseType;
import UserCore.FullUser;
import UserCore.IllegalUserNameException;
import UserCore.InvalidPasswordException;

import java.nio.ByteBuffer;

/**
 * UserProfile
 * <p>
 * responsible for user profile change request
 *
 * @author Yulin Lin, 001
 * @version 11/21/2022
 */
public class UserProfile {

    private FullUser user;

    public UserProfile(FullUser user) {
        this.user = user;
    }

    /**
     * print the user profile
     *
     * @return the user profile in ByteBuffer
     */
    protected ByteBuffer displayUserProfile() {
        return MessageSystem.toByteBufferPacket(ProtocolResponseType.PROFILE, this.user.toString());
    }

    /**
     * change the user's name
     *
     * @param params the raw parameter list
     * @return the user profile after the change
     * @throws IllegalParameter         when the number of parameters is less than expected or a parameter is empty
     * @throws InvalidPasswordException when the password is incorrect
     * @throws IllegalUserNameException when the new username is already taken
     */
    protected ByteBuffer changeUsername(String[] params) throws IllegalParameter,
            InvalidPasswordException, IllegalUserNameException {
        this.user.changeUsername(params[0], params[1]);
        return displayUserProfile();
    }


}
