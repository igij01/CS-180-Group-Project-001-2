package UserCore;

import MessageCore.Conversation;
import MessageCore.IllegalTargetException;

import java.util.ArrayList;

public class FullUser {
    //1. When creating a message, make sure to call the receiver receive method!
    //2. Need to create a reception method. Public Information will have a list of FullSeller, so you can call the FullSeller's receive method.
    private final User user;
    private ArrayList<Conversation> conversations;

//    /**
//     * Creates a new Full User instance that creates a user so that this can deal with messages
//     * this allows for limited access to messages.
//     *
//     * @param userName the name of the user
//     * @param email the email address assoc. with the user
//     * @param pwd the password of the user
//     * @param role the role of the user
//     * @throws EmailFormatException when email address are not put in the right format
//     * @throws IllegalUserNameException when the passed in username is already taken
//     */
//    protected FullUser(String userName, String email, String pwd, Role role) { //end user cannot instantiate FullUser
//        user = new User(userName, email, pwd, role);
//        conversations = new ArrayList<>();
//    }

    protected FullUser(User user) { //replace with this since full user cannot instantiate this class. Therefore, instantiation should occur in FullSeller/FullBuyer
        this.user = user;
        conversations = new ArrayList<>();
    }

    protected User getUser() {
        return this.user;
    }

    /**
     * creates a new message to the conversation between user and receiver
     * If there's no existing conversation between user and the receiver, it will be created
     *
     * @param receiver    The receiver of the message
     * @param messageBody the Message the seller wants to send
     */
    public void createMessage(FullUser receiver, String messageBody) throws IllegalTargetException {
        //since the sender will be one the user in the field. FullUser since it will have the corresponding reception method

        //need to check whether users are different role: check Message constructor
        // need to go through the ArrayList of conversation to find whether such conversation already exists
        // if not you can create conversation
        // you would also need to call the reception method of the receiver
        Conversation conversation = new Conversation(this.user, receiver.user); // check the conversation constructor for update
        //this is possible due to polymorphism. Since receiver/this will ultimately be created using FullBuyer/Seller
        //which will pass in a Buyer/Seller to user field.
        // MAKE SURE THAT'S ALWAYS TRUE. (for example, make constructor protected and accepting only an already created
        // User limit instantiation to only classes in the package)

//        Message message = new Message(seller, buyer, messageBody);
        conversations.add(conversation);
        receiver.receiveConversation(conversation);
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
     * @param user         The user that the conversation is being deleted from
     * @param convoIndex   Which conversation they want to edit
     * @param messageIndex Which message they want to delte
     */
    public void deleteMessage(User user,int convoIndex, int messageIndex) /*throw IllegalUserAccessException*/ {
       /* if (this.conversations.get(convoIndex).removeMessage(user, messageIndex)) {
            this.conversations.remove(convoIndex);
        } */
    }




}