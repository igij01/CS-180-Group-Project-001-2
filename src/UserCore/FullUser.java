package UserCore;

import MessageCore.Conversation;
import MessageCore.IllegalMessageException;
import MessageCore.IllegalTargetException;
import MessageCore.IllegalUserAccessException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class FullUser implements Serializable {

    // update this field everytime you update the field of the class
    // put in a random number or just increment the number
    @Serial
    private static final long serialVersionUID = 2L;

    private final User user; //an instance of user
    private final ArrayList<Conversation> conversations; //a list of the conversation a user has
    private final ArrayList<User> blocked; //a list of blocked users
    private final ArrayList<User> invisible;
    private final ArrayList<String> filterWords;
    private char replaceChar;
    private boolean ignoreFiltering;

    /**
     * the user is set to the user field
     * and the conversations list is initialized
     * <p>
     * Note: pass in a Buyer/Seller instead of User.
     * User is just a placeholder
     *
     * @param user a previously created buyer/seller
     */
    protected FullUser(User user) {
        this.user = user;
        conversations = new ArrayList<>();
        blocked = new ArrayList<>();
        invisible = new ArrayList<>();
        filterWords = new ArrayList<>();
        replaceChar = '*';
        ignoreFiltering = false;
    }

    public ArrayList<Conversation> getConversations() {
        return conversations;
    }
    
    /**
     * creates a new message to the conversation between user and receiver
     * If there's no existing conversation between user and the receiver, it will be created
     * <p>
     * If the user is blocked by the target, it will not create the message and return false
     *
     * @param receiver    The receiver of the message
     * @param messageBody the Message the seller wants to send
     * @return true if the message is not blocked
     * @throws IllegalTargetException  when the target and the sender are the same role
     * @throws IllegalMessageException when either the sender or the receiver is not a participant of the conversation
     */
    public boolean createMessage(FullUser receiver, String messageBody) throws IllegalTargetException,
            IllegalMessageException {
        if (!receiver.checkBlocked(this.user)) {
            for (Conversation c : conversations) {
                if (c.getOtherUser(this.user).equals(receiver.user)) {
                    c.addMessage(this.user, receiver.user, messageBody);
                    return true;
                }
            }
            Conversation tempConversation = new Conversation(this.user, receiver.user);
            tempConversation.addMessage(this.user, receiver.user, messageBody);
            receiver.receiveConversation(tempConversation);
            conversations.add(tempConversation);
            return true;
        }
        return false;
    }

    /**
     * creates a new message to the conversation between user and receiver
     * If there's no existing conversation between user and the receiver, it will be created
     * <p>
     * If the user is blocked by the target, it will not create the message and return false
     *
     * @param receiver The receiver of the message
     * @param txtFile  the Message the seller wants to send in the text file
     * @return true if the message is not blocked
     * @throws IllegalTargetException  when the target and the sender are the same role
     * @throws IllegalMessageException when either the sender or the receiver is not a participant of the conversation
     * @throws IOException             when IO exception occurs
     */
    public boolean createMessage(FullUser receiver, File txtFile) throws IllegalTargetException,
            IllegalMessageException, IOException {
        return this.createMessage(receiver, convertFileToString(txtFile));
    }

    /**
     * a utility method that convert the content of a txt file to a string
     *
     * @param txtFile the txt file that contains the information needed
     * @return the content in {@code String}
     * @throws IOException when IO exception occurs
     */
    private static String convertFileToString(File txtFile) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bfr = new BufferedReader(new FileReader(txtFile))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
            return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
        }
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
     * call this method IF AND ONLY IF the FullUser is going to be deconstructed and not recoverable
     *
     * @param user the user deleted his/her account
     */
    protected void receiveUserDestruction(FullUser user) {
        for (Conversation i : conversations) {
            if (i.getOtherUser(this.user).equals(user.user))
                i.messageDestruction(user.user);
        }
        blocked.remove(user.user);
        invisible.remove(user.user);
    }

    /**
     * Deletes the specific message in a specific conversation of a user
     * if all messages are deleted the conversation is removed.
     *
     * @param conversationIndex Which conversation they want to edit
     * @param messageIndex      Which message they want to delete
     * @throws IllegalUserAccessException is thrown if the user is not authorized to delete message
     * @throws IndexOutOfBoundsException  is throw if either an invalid index for conversation or message is given
     */
    public void deleteMessage(int conversationIndex, int messageIndex)
            throws IllegalUserAccessException, IndexOutOfBoundsException {
        if (this.conversations.get(conversationIndex).deleteMessage(this.user, messageIndex)) {
            this.conversations.remove(conversationIndex);
        }
    }

    /**
     * edit the messages in the specific conversation
     *
     * @param conversationIndex where the conversation is located in conversations list
     * @param messageIndex      where the message is located in the specified conversation
     * @param newMessage        the message that will replace the previous message
     * @throws IndexOutOfBoundsException  is thrown from editMessage when an invalid index is specified
     * @throws IllegalUserAccessException is thrown from editMessage when user is not allowed to access method
     */
    public void editMessage(int conversationIndex, int messageIndex, String newMessage)
            throws IndexOutOfBoundsException, IllegalUserAccessException {
        conversations.get(conversationIndex).editMessage(this.user, messageIndex, newMessage);
    }

    /**
     * edit the messages in the specific conversation
     *
     * @param conversationIndex where the conversation is located in conversations list
     * @param messageIndex      where the message is located in the specified conversation
     * @param newMessage        the message that will replace the previous message
     * @throws IndexOutOfBoundsException  is thrown from editMessage when an invalid index is specified
     * @throws IllegalUserAccessException is thrown from editMessage when user is not allowed to access method
     * @throws IOException                when IO exception occurs
     */
    public void editMessage(int conversationIndex, int messageIndex, File newMessage)
            throws IndexOutOfBoundsException, IllegalUserAccessException, IOException {
        this.editMessage(conversationIndex, messageIndex, convertFileToString(newMessage));
    }

    public String viewDashboard() {
        return "Username: " + this.user.getUserName() +
                "\nEmail: " + this.user.getEmail();
    }

    /**
     * Print the list of conversations by titles and place the conversations
     * that have
     * <p>
     * creates NewConversation list that hold new conversations
     * removes new conversations from Conversations list
     * adds new conversations list to start of conversations list
     * builds a string that puts asterisks for new conversations
     *
     * @return String of conversations to be printed, null if there are no conversations
     */
    public String printConversationTitles() {
        ArrayList<Conversation> newConversations = new ArrayList<>();
        for (Conversation c : conversations) {
            if (c.newMessageStatus(this.user)) {
                newConversations.add(c);
            }
        }
        for (Conversation c : newConversations) // remove the new conversation from conversation,
            // has to be done this way since you can't remove elements of a list that's currently looping
            conversations.remove(c);
        conversations.addAll(0, newConversations);
        StringBuilder sdr = new StringBuilder();
        for (int i = 0; i < conversations.size(); i++) {
            sdr.append(String.format("%3d", i));
            if (conversations.get(i).newMessageStatus(this.user)) {
                sdr.append("    ").append("*").append("    ");
                sdr.append(conversations.get(i).getOtherUser(this.user).getUserName());
            } else {
                sdr.append("    ").append("     ");
                sdr.append(conversations.get(i).getOtherUser(this.user).getUserName());
            }
            sdr.append(System.lineSeparator());
        }
        if (sdr.isEmpty())
            return null;
        return filter(sdr.toString());
    }

    /**
     * print the content of the conversation by index the user select
     * @param index index
     * @return the conversation content
     * @throws IndexOutOfBoundsException is thrown from editMessage when an invalid index is specified
     */
    public String printConversation(int index) throws IndexOutOfBoundsException {
        String out = conversations.get(index).toStringConversation(this.user);
        return filter(out);
    }

    /**
     * filter the input according to user settings
     * @param strToBeFiltered string to be filtered
     * @return the filtered string
     */
    private String filter(String strToBeFiltered) {
        if (ignoreFiltering)
            return strToBeFiltered;
        for (String str : filterWords) {
            char[] replacement = new char[str.length()];
            Arrays.fill(replacement, this.replaceChar);
            strToBeFiltered = strToBeFiltered.replaceAll(String.format("\\b%s\\b", str), String.valueOf(replacement));
        }
        return strToBeFiltered;
    }


    /**
     * checks if the specified password matched password of user.
     * If it does, set user login status to true
     *
     * @param pwd specified password
     * @return true if specified password matches user's password
     */
    public boolean passwordCheck(String pwd) {
        if (this.user.getPwd().equals(pwd)) {
            this.user.setLoginStatus(true);
            return true;
        }
        return false;
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
     * Check if this user is invisible to the user passed in
     *
     * @param user the user that wants to see whether he's invisible to the caller instance
     * @return true if the user is made invisible
     */
    protected boolean checkInvisible(User user) {
        return invisible.contains(user);
    }

    /**
     * adds specified user to list of blocked users
     *
     * @param fullUser specified user
     */
    public void block(FullUser fullUser) {
        blocked.add(fullUser.user);
    }

    /**
     * adds specified user to list of invisible users
     *
     * @param fullUser specified user
     */
    public void makeInvisible(FullUser fullUser) {
        invisible.add(fullUser.user);
    }

    /**
     * add a word for filtering
     * @param filterWord the word to be filtered
     */
    public void addFilterWord(String filterWord) {
        filterWords.add(filterWord);
    }

    /**
     * remove a word for filtering
     * @param filterWord the filtered word
     * @return true if the filterWord was in the list
     */
    public boolean removeFilteredWord(String filterWord) {
        return filterWords.remove(filterWord);
    }

    /**
     * change the replacement characters
     * @param replaceChar the replacement characters
     */
    public void changeReplacedChar(char replaceChar) {
        this.replaceChar = replaceChar;
    }

    /**
     * change the filter mode. true if you want to turn it off
     * @param off whether you want to turn it off
     */
    public void changeFilteringMode(boolean off) {
        this.ignoreFiltering = off;
    }

    /**
     * removes specified user from list of blocked user
     *
     * @param fullUser specified user
     * @return true if the user was blocked
     */
    public boolean unblock(FullUser fullUser) {
        return blocked.remove(fullUser.user);
    }

    /**
     * removes specified user from list of invisible user
     *
     * @param fullUser specified user
     * @return true if the user was in invisible list
     */
    public boolean unInvisible(FullUser fullUser) {
        return invisible.remove(fullUser.user);
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
     * if a user is already logged out, it will be ignored
     */
    public void logout() {
        this.user.setLoginStatus(false);
    }

    @Override
    public String toString() {
        return "user: " + user;
    }
}
