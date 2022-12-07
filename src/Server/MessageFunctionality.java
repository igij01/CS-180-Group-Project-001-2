package Server;

import MessageCore.IllegalTargetException;
import Protocol.ProtocolResponseType;
import UserCore.*;

import java.nio.ByteBuffer;
import java.util.Hashtable;
import java.util.Objects;

public class MessageFunctionality {
    private static Hashtable<FullUser, MessageFunctionality> userToMessageFunc = new Hashtable<>();
    private FullUser user;
    private String currentConversation = null;

    public MessageFunctionality(FullUser user) {
        this.user = user;
        userToMessageFunc.put(this.user, this);
    }

    /**
     * Form:
     * <p>
     * {user1'\n', user2, user3...} (it will add a new line after the name if the conversation has new messages)
     * @return sends the conversation titles
     */
    protected ByteBuffer displayConversationTitles() {
        return MessageSystem.toByteBufferPacket(ProtocolResponseType.CONVERSATION_TITLES,
                Objects.requireNonNullElseGet(user.printConversationTitlesArray(),
                        () -> new String[]{"You have no conversation!"}));
    }

    /**
     * send the message to the user
     * @param params the raw parameter list <b>(target_username)</b>
     * @return the new conversation titles
     * @throws IllegalTargetException when the target user cannot be found
     * @throws InvalidActionException when the target user blocked you
     */
    protected ByteBuffer sendMessageUser(String[] params) throws IllegalTargetException, InvalidActionException {
        FullUser target = PublicInformation.findUser(params[0], this.user);
        if (target == null)
            throw new IllegalTargetException("You are messaging someone that has the same role as you!");
        boolean blocked = user.createMessage(target, params[1]);
        if (!blocked)
            throw new InvalidActionException("The target user blocked you!");
        if (userToMessageFunc.get(target) != null) {
            ByteBuffer[] response = userToMessageFunc.get(target).updateMessage(this.user.getUsername());
            MessageSystem.runNotificationThread(MessageSystem.userToKey.get(this.user), response);
        }
        return displayConversationTitles();
    }

    /**
     * call this method to update the target client
     * @param username the username of the invoker
     * @return bytebuffer(s) to add to the target client write queue
     */
    private ByteBuffer[] updateMessage(String username) {
        ByteBuffer[] response;
        if (this.currentConversation.equalsIgnoreCase(username)) {
            response = new ByteBuffer[2];
            response[0] = displayConversationTitles();
            response[1] = displayConversation(new String[]{this.currentConversation});
        } else {
            response = new ByteBuffer[1];
            response[0] = displayConversationTitles();
        }
        return response;
    }

    /**
     * send the message to the store
     * @param params the raw parameter list <b>(target_store_name)</b>
     * @return the new conversation titles
     * @throws IllegalTargetException when the target store cannot be found
     * @throws InvalidActionException when the target store owner blocked you
     */
    protected ByteBuffer sendMessageStore(String[] params) throws IllegalTargetException, InvalidActionException {
        if (user instanceof FullSeller)
            throw new IllegalTargetException("You are a seller therefore cannot message a store!");
        boolean blocked = ((FullBuyer) user).messageStore(PublicInformation.getStore(params[0]), params[1]);
        if (!blocked)
            throw new InvalidActionException("The target user blocked you!");
        return displayConversationTitles();
    }

    /**
     * send the conversation by the title client selected
     * @param params the raw parameter list <b>(conversation_title)</b>
     * @return the current conversation
     * @throws IllegalUserNameException is thrown when the such title cannot be found
     */
    protected ByteBuffer displayConversation(String[] params) throws IllegalUserNameException {
        this.currentConversation = params[0].replace("\n", "");
        return MessageSystem.toByteBufferPacket(ProtocolResponseType.CONVERSATION, user.printConversation(params[0]));
    }


}
