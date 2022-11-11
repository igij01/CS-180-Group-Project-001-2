package UserCore;

import MessageCore.*;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static UserCore.PublicInformation.listOfUsersNames;

public class FullUser {
    //1. When creating a message, make sure to call the receiver receive method!
    private final User user;
    private ArrayList<Conversation> conversations;
    private ArrayList<User> blocked;

    protected FullUser(User user) { //replace with this since full user cannot instantiate this class. Therefore, instantiation should occur in FullSeller/FullBuyer
        if (PublicInformation.listOfUsersNames.contains(user.getUserName()))
            throw new IllegalUserNameException(user.getUserName());
        this.user = user;
        conversations = new ArrayList<>();
    }

    protected User getUser() {
        return this.user;
    }
    protected boolean checkBlocked(User user) {
        return blocked.contains(user);
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
     * if all messages are deleted the conversation is also removed.
     *
     * @param user The user that the conversation is being deleted from
     * @param conversationIndex   Which conversation they want to edit
     * @param messageIndex Which message they want to delete
     */
    public void deleteMessage(User user,int conversationIndex, int messageIndex) throws IllegalUserAccessException, IndexOutOfBoundsException {
        if (this.conversations.get(conversationIndex).deleteMessage(user, messageIndex)) {
            this.conversations.remove(conversationIndex);
        }
    }
    public void editMessage(int conversationIndex, int messageIndex, String newMessage) throws IOException, IllegalUserAccessException {
        conversations.get(conversationIndex).editMessage(this.user, messageIndex , newMessage);
    }
    public boolean reception() {
        return this.user.isLoginStatus();
    }
    public void logout() {
        this.user.setLoginStatus(false);
    }
    public void block(User User) {
        blocked.add(user);
    }
    public void unblock(User User) {
        blocked.remove(user);
    }
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
        }
        return sdr;
    }
}
