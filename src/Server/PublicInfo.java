package Server;

import Protocol.ProtocolResponseType;
import UserCore.FullBuyer;
import UserCore.FullSeller;
import UserCore.FullUser;
import UserCore.PublicInformation;

import java.nio.ByteBuffer;

/**
 * PublicInfo
 * <br>
 * A class that provides an interface/abstraction of the Public Information class
 *
 * @author Yulin Lin, 001
 * @version 12/11/2022
 */
public class PublicInfo {
    private PublicInfo() {
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
     * @param user the user requesting this action
     * @return lists of stores and sellers for buyers, and list of buyers for sellers
     */
    public static ByteBuffer sendPublicInfo(FullUser user) {
        String[] infos = null;
        if (user instanceof FullBuyer) {
            infos = new String[2];
            infos[0] = PublicInformation.storeList((FullBuyer) user);
            if (infos[0] == null)
                infos[0] = "There are no stores!";
            infos[1] = PublicInformation.sellerList((FullBuyer) user);
            if (infos[1] == null)
                infos[1] = "There are no sellers!";
        } else if (user instanceof FullSeller) {
            infos = new String[1];
            infos[0] = PublicInformation.buyerList((FullSeller) user);
            if (infos[0] == null)
                infos[0] = "There are no buyers!";
        }
        assert infos != null;
        return MessageSystem.toByteBufferPacket(ProtocolResponseType.PUBLIC_INFO, infos);
    }

    /**
     * return the dashboard assoc. with that user
     *
     * @param user   the user requesting this action
     * @param params the parameter list <b>({@code Boolean} increasing?)</b>
     * @return the dashboard
     */
    public static ByteBuffer sendDashBoard(FullUser user, String[] params) {
        boolean increasing;
        if (params[0].equalsIgnoreCase("true") || params[0].equalsIgnoreCase("t"))
            increasing = true;
        else if (params[0].equalsIgnoreCase("false") || params[0].equalsIgnoreCase("f"))
            increasing = false;
        else
            throw new InvalidActionException(params[0] + " is not a proper boolean flag!");
        if (user instanceof FullBuyer)
            return MessageSystem.toByteBufferPacket(ProtocolResponseType.DASHBOARD,
                    ((FullBuyer) user).viewDashboard(increasing));
        else {
            assert user instanceof FullSeller;
            return MessageSystem.toByteBufferPacket(ProtocolResponseType.DASHBOARD,
                    ((FullSeller) user).viewDashboard(increasing));
        }
    }
}
