package Server;

import Protocol.Request;
import UserCore.FullUser;

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
        return MessageSystem.toByteBufferPacket(Request.PROFILE, this.user.toString());
    }

    protected void changeUsername(String[] params) {

    }


}
