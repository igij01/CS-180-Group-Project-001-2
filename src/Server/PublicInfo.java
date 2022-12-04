package Server;

import Protocol.ProtocolResponseType;
import UserCore.FullBuyer;
import UserCore.FullSeller;
import UserCore.FullUser;
import UserCore.PublicInformation;

import java.nio.ByteBuffer;

public class PublicInfo {
    private FullUser user;

    public PublicInfo(FullUser user) {
        this.user = user;
    }

    /**
     * return list of usernames for the client to check for user login and register
     *
     * @return list of usernames
     */
    public static ByteBuffer sendAllUsernames() {
        return MessageSystem.toByteBufferPacket(ProtocolResponseType.USER_NAMES,
                PublicInformation.listOfUsersNames.toString());
    }

    /**
     * return lists of stores and sellers for buyers, and list of buyers for sellers
     *
     * @return lists of stores and sellers for buyers, and list of buyers for sellers
     */
    public ByteBuffer sendPublicInfo() {
        String[] infos = new String[2];
        if (user instanceof FullBuyer) {
            infos[0] = PublicInformation.storeList((FullBuyer) user);
            if (infos[0] == null)
                infos[0] = "There are no stores!";
            infos[1] = PublicInformation.sellerList((FullBuyer) user);
            if (infos[1] == null)
                infos[1] = "There are no sellers!";
        } else if (user instanceof FullSeller) {
            infos[0] = PublicInformation.buyerList((FullSeller) user);
            if (infos[0] == null)
                infos[0] = "There are no buyers!";
        }
        return MessageSystem.toByteBufferPacket(ProtocolResponseType.PUBLIC_INFO, infos);
    }
}
