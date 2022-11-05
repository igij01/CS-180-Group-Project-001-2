package MessageCore;

import UserCore.Buyer;

import UserCore.IllegalTargetException;
import UserCore.Seller;
import UserCore.User;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Conversation
 * <p>
 * A class that contains a conversation between a buyer and a seller for a particular store
 *
 * @author Yulin Lin, 001
 * @version 11/3/2022
 */
public class Conversation implements Serializable {
    // update this field everytime you update the field of the class
    // put in a random number or just increment the number
    @Serial
    private static final long serialVersionUID = 2L;

    private final Buyer buyer;
    private final Seller seller;
    //TODO add store
    private final ArrayList<Message> conversation;

    /**
     * Create an instance of conversation
     * <b>Note: This constructor relies on the fact that sender and target will be Seller and Buyer. Therefore, make sure
     * the any instantiation statement knows ahead of time that sender and target will be Seller/Buyer</b>
     *
     * @param sender the user requesting the creation
     * @param target the target of the conversation
     * @throws IllegalTargetException when the target and the sender are the same role
     * @throws ClassCastException     when the note is violated; i.e. the sender/target is a User instead of Buyer/Seller
     */
    public Conversation(User sender, User target) throws IllegalTargetException {
        if (sender instanceof Buyer) {
            this.buyer = (Buyer) sender;
            if (target instanceof Seller)
                this.seller = (Seller) target;
            else
                throw new IllegalTargetException(target);
        } else {
            this.buyer = (Buyer) target;
            if (sender instanceof Seller)
                this.seller = (Seller) sender;
            else
                throw new IllegalTargetException(target);
        }
        conversation = new ArrayList<>();
    }

    /**
     * Add a message to the conversation if the buyer and seller are participant of the message
     *
     * @param message the message to be added
     * @return true if the message is added
     */
    public boolean AddMessage(Message message) {
        if (!message.isParticipant(buyer) || !message.isParticipant(seller))
            return false;
        conversation.add(message);
        return true;
    }


    /**
     * @return the conversation in the format of "sender: content \n time"
     */
    @Override
    public String toString() {
        StringBuilder rawString = new StringBuilder();
        for (Message m : conversation) {
            rawString.append(m.toString()).append('\n');
        }
        rawString.deleteCharAt(rawString.length() - 1);
        return rawString.toString();
    }
}
