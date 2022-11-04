package MessageCore;

import UserCore.*;

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
    private ArrayList<Message> conversation;

    public Conversation(Buyer buyer, Seller seller) {
        this.buyer = buyer;
        this.seller = seller;
    }
}
