package UserCore;

import MessageCore.Conversation;
import MessageCore.Message;

public class FullUser {
    private User user;

    /**
     *
     * Creates a new Full User instance
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

    public void createMessage(Buyer buyer, Seller seller, String messageBody) {
        Conversation conversation = new Conversation(buyer, seller);
        Message message = new Message(buyer, seller, messageBody);


    }




}
