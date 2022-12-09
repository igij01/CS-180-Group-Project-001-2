package Server;

import Protocol.ProtocolResponseType;
import UserCore.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * UserProfile
 * <p>
 * responsible for user profile change request
 *
 * @author Yulin Lin, 001
 * @version 12/4/2022
 */
public class UserProfile {

    private FullUser user;

    public UserProfile(FullUser user) {
        this.user = user;
    }

    /**
     * print the user profile
     * <p>
     * see protocol response type for detail output
     *
     * @return the user profile in ByteBuffer
     * @see ProtocolResponseType#PROFILE
     */
    protected ByteBuffer displayUserProfile() {
        return MessageSystem.toByteBufferPacket(ProtocolResponseType.PROFILE, this.user.serverToString());
    }

    /**
     * change the user's name
     *
     * @param params the raw parameter list <b>(username, pwd)</b>
     * @return the user profile after the change
     * @throws InvalidPasswordException when the password is incorrect
     * @throws IllegalUserNameException when the new username is already taken
     */
    protected ByteBuffer changeUsername(String[] params) throws InvalidPasswordException, IllegalUserNameException {
        this.user.changeUsername(params[0], params[1]);
        return displayUserProfile();
    }

    /**
     * change the user's email
     *
     * @param params the raw parameter list <b>(email, pwd)</b>
     * @return the user profile after the change
     * @throws InvalidPasswordException when the password is incorrect
     * @throws EmailFormatException     when the email is the wrong format
     */
    protected ByteBuffer changeEmail(String[] params) throws InvalidPasswordException, EmailFormatException {
        this.user.changeEmail(params[0], params[1]);
        return displayUserProfile();
    }

    /**
     * add a user to the blocked list
     *
     * @param params the raw parameter list <b>(block_username)</b>
     * @return the user profile after the change
     * @throws IllegalUserNameException when the username cannot be found
     */
    protected ByteBuffer blockUser(String[] params) throws IllegalUserNameException {
        FullUser blockUser = findUserBasedOnRole(params[0]);
        assert blockUser != null;
        this.user.block(blockUser);
        return displayUserProfile();
    }

    /**
     * remove a user from the blocked list
     *
     * @param params the raw parameter list <b>(unblock_username)</b>
     * @return the user profile after the change
     * @throws IllegalUserNameException when the username cannot be found
     * @throws InvalidActionException   when the user is not blocked
     */
    protected ByteBuffer unblockUser(String[] params) throws IllegalUserNameException, InvalidActionException {
        FullUser unBlockUser = findUserBasedOnRole(params[0]);
        assert unBlockUser != null;
        if (!user.unblock(unBlockUser))
            throw new InvalidActionException("the username " + params[0] + " is not blocked by you!");
        return displayUserProfile();
    }

    /**
     * find the corresponding buyer/seller based on the role of the requesting user
     *
     * @param actionUsername the username of the user that need to be found
     * @return the FullBuyer/FullSeller object of the user
     * @throws IllegalUserNameException when the username cannot be found
     */
    private FullUser findUserBasedOnRole(String actionUsername) throws IllegalUserNameException {
        FullUser actionUser = null;
        if (this.user instanceof FullSeller) {
            if ((actionUser = PublicInformation.findBuyer(actionUsername, (FullSeller) this.user)) == null) {
                throw new IllegalUserNameException();
            }
        } else if (this.user instanceof FullBuyer) {
            if ((actionUser = PublicInformation.findSeller(actionUsername, (FullBuyer) this.user)) == null) {
                throw new IllegalUserNameException();
            }
        }
        return actionUser;
    }

    /**
     * add a user to the invisible list
     *
     * @param params the raw parameter list <b>(invis_username)</b>
     * @return the user profile after the change
     * @throws IllegalUserNameException when the username cannot be found
     */
    protected ByteBuffer invisUser(String[] params) throws IllegalUserNameException {
        FullUser invisUser = findUserBasedOnRole(params[0]);
        assert invisUser != null;
        this.user.makeInvisible(invisUser);
        return displayUserProfile();
    }

    /**
     * remove a user from the invisible list
     *
     * @param params the raw parameter list <b>(uninvis_username)</b>
     * @return the user profile after the change
     * @throws IllegalUserNameException when the username cannot be found
     * @throws InvalidActionException   when the user is not made invisible
     */
    protected ByteBuffer uninvisUser(String[] params) throws IllegalUserNameException, InvalidActionException {
        FullUser unInvidUser = findUserBasedOnRole(params[0]);
        assert unInvidUser != null;
        if (!user.unblock(unInvidUser))
            throw new InvalidActionException("the username " + params[0] + " is not made invisible by you!");
        return displayUserProfile();
    }

    /**
     * add a word to the filtered list
     *
     * @param params the raw parameter list <b>(filter_word)</b>
     * @return the user profile after the change
     * @throws InvalidActionException InvalidActionException when it's not a word
     */
    protected ByteBuffer addFilterWord(String[] params) throws InvalidActionException {
        String filterWord = params[0];
        if (filterWord.matches("[^\\w']+")) {
            throw new InvalidActionException("Not a word!");
        }
        user.addFilterWord(filterWord);
        return displayUserProfile();
    }

    /**
     * remove a word from the filtered list(note, this is not going to check whether it's word, it's checked when you
     * put it in; so not a word and word not in the list exceptions will be squashed together; check at client side!)
     *
     * @param params the raw parameter list <b>(unfiltered_word)</b>
     * @return the user profile after the change
     * @throws InvalidActionException when it's not a word or word is not filtered
     */
    protected ByteBuffer removeAFilteredWord(String[] params) throws InvalidActionException {
        String unFilterWord = params[0];
        if (!user.removeFilteredWord(unFilterWord))
            throw new InvalidActionException("the word " + unFilterWord + " is not in your filtered word list!");
        return displayUserProfile();
    }

    /**
     * replace the filter pattern
     *
     * @param params the raw parameter list <b>({@code Character} replaceChar)</b>
     * @return the user profile after the change
     * @throws InvalidActionException when the passed in parameter is not a character(i.e. length != 1)
     */
    protected ByteBuffer replaceFilterPattern(String[] params) throws InvalidActionException {
        String str = params[0];
        char censoredChar;
        if (str.length() == 1)
            censoredChar = str.charAt(0);
        else
            throw new InvalidActionException(str + " is not a valid character!");
        if (censoredChar != (char) -1)
            user.changeReplacedChar(censoredChar);
        return displayUserProfile();
    }

    /**
     * toggle the filter mode on or off. <b>>True</b> if you want to turn it off
     *
     * @param off whether you want to turn it off
     * @return the user profile after the change
     */
    protected ByteBuffer toggleFilterMode(boolean off) {
        this.user.changeFilteringMode(off);
        return displayUserProfile();
    }

    /**
     * create a store (FullSeller ONLY!)
     *
     * @param params the raw parameter list <b>(store_name)</b>
     * @return the user profile after the change
     * @throws IllegalStoreNameException when the store name is already taken
     * @throws InvalidActionException    when the requesting user is a buyer
     */
    protected ByteBuffer createStore(String[] params) throws IllegalStoreNameException, InvalidActionException {
        if (this.user instanceof FullSeller) {
            ((FullSeller) this.user).createStore(params[0]);
            return displayUserProfile();
        }
        throw new InvalidActionException("The user is not a seller!");
    }

    /**
     * delete the account
     *
     * @param params the raw parameter list <b>(pwd)</b>
     * @return the user profile after the change
     * @throws InvalidPasswordException when the password is incorrect
     */
    protected ByteBuffer deleteAccount(String[] params) throws InvalidPasswordException {
        PublicInformation.deleteAccount(this.user, params[0]);
        return displayUserProfile();
    }

    /**
     * recover your account
     *
     * @return the user profile after the change
     * @throws InvalidActionException when the user never attempted to delete the account
     */
    protected ByteBuffer recoverAccount() throws InvalidActionException {
        if (!PublicInformation.recoverAccount(this.user))
            throw new InvalidActionException("You never attempted to delete your account!");
        return displayUserProfile();
    }

    /**
     * logout the user
     * <p>
     * if the user is in waiting to be deleted status, it will send a packet to confirm such action. Client
     * can confirm this action by submitting a FORCE_LOGOUT request
     *
     * @return success message if login succeed, account deletion warning if the account is about to be deleted
     * @see #confirmLogOut()
     */
    protected ByteBuffer logout() {
        if (user.waitingToBeDeletedStatus())
            return MessageSystem.toByteBufferPacket(
                    ProtocolResponseType.ACCOUNT_DELETION, "you are about to lose your account!");
        return confirmLogOut();
    }

    /**
     * Force logout the user
     *
     * @return success upon success logout
     */
    protected ByteBuffer confirmLogOut() {
        PublicInformation.logout(this.user);
        MessageSystem.userToKey.remove(this.user);
        return MessageSystem.toByteBufferPacket(
                ProtocolResponseType.LOGOUT_SUCCESS, "logout success!");
    }


}
