package Server;

import UserCore.FullUser;

import java.nio.ByteBuffer;

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
        return MessageSystem.toByteBuffer(this.user.toString());
    }

    protected void changeUsername(String[] params) {

    }


}
