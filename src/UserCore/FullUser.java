package UserCore;

import MessageCore.*;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static UserCore.PublicInformation.listOfUsersNames;

public class FullUser {
    private final User user; //an instance of user
    private ArrayList<Conversation> conversations; //a list of the conversation a user has
    private ArrayList<User> blocked; //a list of blocked users

    /**
     * the user is set to the user field
     * and the conversations list is initialized
     *
     * @param user a previously created user
     */
    protected FullUser(User user) { //replace with this since full user cannot instantiate this class. Therefore, instantiation should occur in FullSeller/FullBuyer
        this.user = user;
        conversations = new ArrayList<>();
    }

    /**
     * creates a new message to the conversation between user and receiver
     * If there's no existing conversation between user and the receiver, it will be created
     *
     * @param receiver    The receiver of the message
     * @param messageBody the Message the seller wants to send
     */
    public boolean createMessage(FullUser receiver, String messageBody) throws IllegalTargetException, IllegalMessageException {
        if (!receiver.checkBlocked(this.user)) {
            for (Conversation c : conversations) {
                if ( c.getOtherUser(this.user).equals(receiver.user)) {
                    c.addMessage(this.user, receiver.user, messageBody);
                    return true;
                }
            }
            Conversation tempConversation = new Conversation(this.user, receiver.user);
            tempConversation.addMessage(this.user, receiver.user, messageBody);
            conversations.add(tempConversation);
            return true;
        }
        return false;
    }

    /**
     * Receive conversation. Can only be called from this class to prevent unauthorized conversation being sent
     * i.e. the user is not a participant of the conversation
     * <p>
     * Therefore, there's no check being done. Make sure to really understand the intention behind this method before
     * changing the access of the method or call this method
     *
     * @param conversation the conversation that the user is a target of
     */
    private void receiveConversation(Conversation conversation) {
        conversations.add(conversation);
    }

    /**
     * Deletes the specific message in a specific conversation of a user
     * if all messages are deleted the conversation is removed.
     *
     * @param user The user that the conversation is being deleted from
     * @param conversationIndex   Which conversation they want to edit
     * @param messageIndex Which message they want to delete
     * @throws IllegalUserAccessException is thrown if the user is not authorized to delete message
     * @throws IndexOutOfBoundsException is throw if an invalid index is given
     */
    public void deleteMessage(User user,int conversationIndex, int messageIndex) throws IllegalUserAccessException, IndexOutOfBoundsException {
        if (this.conversations.get(conversationIndex).deleteMessage(user, messageIndex)) {
            this.conversations.remove(conversationIndex);
        }
    }

    /**
     * deletes the previous message the user selects
     * adds new user specified message to index of deleted message
     *
     * @param conversationIndex where the conversation is located in conversations list
     * @param messageIndex where the message is located in the specified conversation
     * @param newMessage the message that will replace the previous message
     * @throws IndexOutOfBoundsException is thrown from editMessage when an invalid index is specified
     * @throws IllegalUserAccessException is thrown from editMessage when user is not allowed to access method
     */
    public void editMessage(int conversationIndex, int messageIndex, String newMessage) throws IndexOutOfBoundsException, IllegalUserAccessException {
        conversations.get(conversationIndex).editMessage(this.user, messageIndex , newMessage);
    }

    /**
     * creates NewConversation list that hold new conversations
     * removes new conversations from Conversations list
     * adds new conversations list to start of conversations list
     * builds a string that puts asterisks for new conversations
     *
     * @return StringBuilder of conversations to be printed
     */
    public StringBuilder printConversation() {
        ArrayList<Conversation> newConversations = new ArrayList<Conversation>();
        for (Conversation c : conversations) {
            if (c.newMessageStatus(this.user)) {
                newConversations.add(c);
                conversations.remove(c);
            }
        }
        conversations.addAll(0, newConversations);
        StringBuilder sdr = new StringBuilder();
        for (int i = 0; i < conversations.size(); i++) {
            sdr.append(i);
            if (conversations.get(i).newMessageStatus(this.user)) {
                sdr.append("    ").append("*").append("    ");
                sdr.append(conversations.get(i).getOtherUser(this.user).getUserName());
            } else {
                sdr.append("    ");
                sdr.append(conversations.get(i).getOtherUser(this.user).getUserName());
            }
            sdr.append(System.lineSeparator());
        }
        return sdr;
    }

    /**
     * Checks if the receiving user has blocked sender
     *
     * @param user user that will be receiving message
     * @return true if blocked
     */
    protected boolean checkBlocked(User user) {
        return blocked.contains(user);
    }

    /**
     * adds specified user to list of blocked users
     *
     * @param User specified user
     */
    public void block(User User) {
        blocked.add(user);
    }

    /**
     * removes specified user from list of blocked user
     *
     * @param User specified user
     */
    public void unblock(User User) {
        blocked.remove(user);
    }

    /**
     * @return True if user is logged in
     */
    public boolean loginStatus() {
        return this.user.isLoginStatus();
    }

    /**
     * @return user object
     */
    protected User getUser() {
        return this.user;
    }

    /**
     * updates the login status of user to false
     */
    public void logout() {
        this.user.setLoginStatus(false);
    }
}
