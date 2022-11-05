package UserCore;

import MessageCore.Conversation;
import MessageCore.Message;

import java.util.ArrayList;

public class FullUser {
    private User user;
    private ArrayList<Conversation> conversations = new ArrayList<>();

    /**
     *
     * Creates a new Full User instance that creates a user so that this can deal with messages
     * this allows for limited access to messages.
     *
     *
     * @param userName the name of the user
     * @param email the email address assoc. with the user
     * @param pwd the password of the user
     * @param role the role of the user
     * @throws EmailFormatException when email address are not put in the right format
     */
    public FullUser(String userName, String email, String pwd, Role role) {
        //TODO add checking mechanism to check if the name is already taken
        if (!email.matches("\\b[\\w.%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\\b"))
            throw new EmailFormatException(email);
        user = new User(userName, email, pwd, role);
    }

    /**
     *
     * creates a new conversation and a new message.
     * This method has the buyer as the sender and the seller as the target
     *
     * @param buyer The buyer in the conversation
     * @param seller The seller in the conversation
     * @param messageBody the Message the buyer wants to send
     */
    public void createMessage(Buyer buyer, Seller seller, String messageBody) {
        Conversation conversation = new Conversation(buyer, seller);
        Message message = new Message(buyer, seller, messageBody);
    }

    /**
     *
     * creates a new conversation and a new message.
     * This method has the seller as the sender and the buyer as the target
     *
     * @param buyer The buyer in the conversation
     * @param seller The seller in the conversation
     * @param messageBody the Message the seller wants to send
     */
    public void createMessage(Seller seller, Buyer buyer, String messageBody) {
        Conversation conversation = new Conversation(buyer, seller);
        Message message = new Message(seller, buyer, messageBody);
    }

    /**
     *
     * Deletes the specific message in a specific conversation of a user
     * if all messages are deleted the conversation is also removed.
     *
     * @param user The user that the conversation is being deleted from
     * @param convoIndex Which conversation they want to edit
     * @param messageIndex Which message they want to delte
     */
    public void deleteMessage(User user,int convoIndex, int messageIndex) /*throw IllegalUserAccessException*/ {
       /* if (this.conversations.get(convoIndex).removeMessage(user, messageIndex)) {
            this.conversations.remove(convoIndex);
        } */
    }




}
