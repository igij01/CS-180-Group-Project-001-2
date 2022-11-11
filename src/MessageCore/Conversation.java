package MessageCore;

import UserCore.Buyer;
import UserCore.Seller;
import UserCore.User;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Conversation
 * <p>
 * A class that contains a conversation between a buyer and a seller for a particular store
 *
 * @author Yulin Lin, 001
 * @version 11/5/2022
 */
public class Conversation implements Serializable {
    // update this field everytime you update the field of the class
    // put in a random number or just increment the number
    @Serial
    private static final long serialVersionUID = 2L;

    private boolean newMessageBuyer; //true if there is a new message for buyer
    private boolean newMessageSeller; //true if there is a new message for the seller
    private final Buyer buyer;
    private final Seller seller;
    private final ArrayList<Message> conversation;

    /**
     * Create an instance of conversation
     * <b>Note: This constructor relies on the fact that sender and target will be Seller and Buyer.
     * Therefore, make sure the any instantiation statement knows ahead of time that
     * sender and target will be Seller/Buyer</b>
     *
     * @param sender the user requesting the creation
     * @param target the target of the conversation
     * @throws IllegalTargetException when the target and the sender are the same role
     * @throws ClassCastException     when the note is violated; i.e. the sender/target is a User instead of Buyer/Seller
     */
    public Conversation(User sender, User target) throws IllegalTargetException {
        if (sender instanceof Buyer) {
            this.buyer = (Buyer) sender;
            newMessageSeller = true;
            newMessageBuyer = false;
            if (target instanceof Seller)
                this.seller = (Seller) target;
            else
                throw new IllegalTargetException(target);
        } else {
            this.buyer = (Buyer) target;
            if (sender instanceof Seller) {
                this.seller = (Seller) sender;
                newMessageSeller = false;
                newMessageBuyer = true;
            }
            else
                throw new IllegalTargetException(target);
        }
        conversation = new ArrayList<>();
    }

    /**
     * Add a message to the conversation if the buyer and seller are participant of the message
     *
     * @param message the message to be added
     * @throws IllegalMessageException if the message created have different participant than conversation
     */
    private void addMessage(Message message) {
        if (!message.isParticipant(buyer) || !message.isParticipant(seller))
            throw new IllegalMessageException(message);
        conversation.add(message);
    }

    /**
     * Add a message to the conversation and change the target read status to
     * @param sender      the user sending the message
     * @param receiver    the intended receiver
     * @param messageBody the message body
     * @throws IllegalMessageException when either the sender or the
     *                                 receiver is not a participant of the conversation
     */
    public void addMessage(User sender, User receiver, String messageBody) {
        this.addMessage(new Message(sender, receiver, messageBody));
        updateReadStatus(sender);
    }

    /**
     * Add a message to the conversation
     * @param sender      the user sending the message
     * @param receiver    the intended receiver
     * @param messageBody the message body in a text file
     * @throws IllegalMessageException when either the sender or the
     *                                 receiver is not a participant of the conversation
     * @throws IOException             when an ioexception occurs while reading the txt file
     */
    public void addMessage(User sender, User receiver, File messageBody)
            throws IOException {
        this.addMessage(new Message(sender, receiver, messageBody));
        updateReadStatus(sender);
    }

    /**
     * a method to update the read status
     * this assumes that the action User is already being checked that it's a participant
     * @param sender the sender
     */
    private void updateReadStatus(User sender) {
        if (sender instanceof Buyer)
            newMessageSeller = true;
        else
            newMessageBuyer = true;
    }

    /**
     * @param actionUser the requesting user
     * @param index      the index of the message that the user wants to delete
     * @return true if the conversation is empty and can be deleted
     * @throws IllegalUserAccessException when the requesting user is not allowed to delete the message
     * @throws IndexOutOfBoundsException  when the index that the user selected is out of bound
     */
    public boolean deleteMessage(User actionUser, int index) {
        if (conversation.get(index).deleteMessage(actionUser))
            conversation.remove(index);
        return conversation.isEmpty();
    }

    /**
     * @param actionUser the requesting user
     * @param index      the index of the message that the user wants to edit
     * @param newMessage the new message
     * @throws IllegalUserAccessException when the requesting user is not allowed to edit the message
     *                                    (only the person sending the message can edit it
     * @throws IndexOutOfBoundsException  when the index that the user selected is out of bound
     */
    public void editMessage(User actionUser, int index, String newMessage) {
        conversation.get(index).editMessage(actionUser, newMessage);
        updateReadStatus(actionUser);
    }

    /**
     * @param actionUser     the requesting user
     * @param index          the index of the message that the user wants to edit
     * @param newMessageFile the file that contains new message
     * @throws IllegalUserAccessException when the requesting user is not allowed to edit the message
     *                                    (only the person sending the message can edit it
     * @throws IndexOutOfBoundsException  when the index that the user selected is out of bound
     * @throws IOException                when an IO exception occurs while reading the txt file
     */
    public void editMessage(User actionUser, int index, File newMessageFile) throws IOException {
        conversation.get(index).editMessage(actionUser, newMessageFile);
        updateReadStatus(actionUser);
    }

    /**
     * allow the participant of the conversation to get the other User instance
     * @param requestingUser the user requesting the action
     * @return the User instance of the other user
     * @throws IllegalUserAccessException when the requesting user is not a participant
     */
    public User getOtherUser(User requestingUser) {
        if (requestingUser.equals(this.buyer))
            return this.seller;
        else if (requestingUser.equals(this.seller))
            return this.buyer;
        throw new IllegalUserAccessException();
    }

    /**
     * check if the user has new unread messages
     * @param requestingUser the requesting user
     * @return true if the user has unread messages
     * @throws IllegalUserAccessException when the requesting user is not a participant
     */
    public boolean newMessageStatus(User requestingUser) {
        if (requestingUser.equals(this.buyer))
            return newMessageBuyer;
        else if (requestingUser.equals(this.seller))
            return newMessageSeller;
        throw new IllegalUserAccessException();
    }


    /**
     * Print out the conversation with each message labeled with its index
     * and update the read status
     * @param requestingUser the user requesting this action
     * @return the conversation in the format of "sender: content \n time"
     * @throws IllegalUserAccessException if the user is not a participant
     */
    public String toStringConversation(User requestingUser) {
        if (requestingUser.equals(this.buyer) || requestingUser.equals(this.seller)) {
            updateReadStatus(requestingUser);
            StringBuilder rawString = new StringBuilder();
            int index = 0;
            for (Message m : conversation) {
                rawString.append(index++).append('\t').append(m.toString()).append('\n');
            }
            rawString.deleteCharAt(rawString.length() - 1);
            return rawString.toString();
        }
        else {
            throw new IllegalUserAccessException();
        }
    }
}
