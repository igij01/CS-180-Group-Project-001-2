package MessageCore;

import UserCore.Buyer;
import UserCore.Seller;
import UserCore.User;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
        if (!(sender instanceof Buyer) && !(sender instanceof Seller) ||
                !(target instanceof Buyer) && !(target instanceof Seller))
            throw new ClassCastException("Either sender or target is a User instance");
        if ((sender instanceof Buyer) ^ (target instanceof Seller))
            throw new IllegalTargetException(target);
        if (sender instanceof Buyer) {
            this.buyer = (Buyer) sender;
            newMessageSeller = true;
            newMessageBuyer = false;
            this.seller = (Seller) target;
        } else {
            this.buyer = (Buyer) target;
            newMessageSeller = false;
            newMessageBuyer = true;
            this.seller = (Seller) sender;
        }
        conversation = new ArrayList<>();
    }

    public ArrayList<Message> getConversation() {
        return conversation;
    }

    /**
     * Add a message to the conversation if the buyer and seller are participant of the message
     *
     * @param message the message to be added
     * @throws IllegalMessageException if the message created have different participant than conversation
     */
    private void addMessage(Message message) {
        if (!message.isParticipant(buyer) || !message.isParticipant(seller))
            throw new IllegalMessageException();
        conversation.add(message);
    }

    /**
     * Add a message to the conversation and change the target read status to
     *
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
     * a method to update the read status by setting the RECEIVER new message flag to true
     * this assumes that the action User is already being checked that it's a participant
     *
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
    public boolean deleteMessage(User actionUser, int index) throws IllegalUserAccessException, IndexOutOfBoundsException {
        try {
            if (conversation.get(index).deleteMessage(actionUser))
                conversation.remove(index);
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException();
        }
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
     * allow the participant of the conversation to get the other User instance
     *
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
     *
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
     * send a message in the conversation that the user deleted his account
     *
     * @param requestingUser the that made the deletion
     */
    public void messageDestruction(User requestingUser) {
        if (requestingUser.equals(this.buyer) || requestingUser.equals(this.seller)) {
            if (requestingUser instanceof Seller) {
                //TODO should we empty the whole conversation history?
                addMessage(seller, buyer, "The user deleted his/her account!");
            } else {
                addMessage(buyer, seller, "The user deleted his/her account!");
            }
        } else {
            throw new IllegalUserAccessException();
        }
    }

    /**
     * Print out the conversation in csv format
     *
     * @param requestingUser the user requesting this action
     * @return the conversation in the format of "receiver,sender,timestamp,content"
     * @throws IllegalUserAccessException if the user is not a participant
     */
    public String toStringCSV(User requestingUser) {
        if (requestingUser.equals(this.buyer) || requestingUser.equals(this.seller)) {
            updateReadStatus(requestingUser);
            StringBuilder rawString = new StringBuilder();
            for (Message m : conversation) {
                rawString.append(m.fileToString(requestingUser)).append('\n');
            }
            rawString.deleteCharAt(rawString.length() - 1);
            return rawString.toString();
        } else {
            throw new IllegalUserAccessException();
        }
    }

    /**
     * Print out the conversation with each message labeled with its index
     * and update the read status
     *
     * @param requestingUser the user requesting this action
     * @return the conversation in the format of "sender: content \n time"
     * @throws IllegalUserAccessException if the user is not a participant
     */
    public String toStringConversation(User requestingUser) {
        if (requestingUser.equals(this.buyer) || requestingUser.equals(this.seller)) {
            StringBuilder rawString = new StringBuilder();
            int index = 0;
            for (Message m : conversation) {
                String out;
                if ((out = m.toStringUser(requestingUser)) == null)
                    continue;
                if (m.isNew(requestingUser))
                    rawString.insert(0, (index++ + "\t" + out + "\n"));
                else
                    rawString.append(index++).append('\t').append(out).append('\n');
            }
            if (requestingUser.equals(this.buyer))
                this.newMessageBuyer = false;
            else if (requestingUser.equals(this.seller))
                this.newMessageSeller = false;
            if (!rawString.isEmpty())
                rawString.deleteCharAt(rawString.length() - 1);
            return rawString.toString();
        } else {
            throw new IllegalUserAccessException();
        }
    }

    /**
     * return all words in a conversation
     *
     * @param requestingSeller the seller requesting this action
     * @return an array of all the words in the conversations, including duplicates
     * @throws IllegalUserAccessException when the requesting seller is not a participant of the conversation
     */
    public ArrayList<String> allWordsFromMessages(Seller requestingSeller) {
        if (requestingSeller.equals(this.seller)) {
            ArrayList<String> words = new ArrayList<>();
            for (Message m : conversation) {
                words.addAll(List.of(m.words()));
            }
            return words;
        } else
            throw new IllegalUserAccessException();
    }
}
