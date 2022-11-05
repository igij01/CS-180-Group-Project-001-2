package MessageCore;

import UserCore.Role;
import UserCore.User;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Message
 * <p>
 * A basic unit of messaging system which comprise a sender, a receiver, the message, and a timestamp,
 * with additional features
 *
 * @author Yulin Lin, 001
 * @version 11/3/2022
 */
public class Message implements Serializable {
    //specifically for csv export
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    // update this field everytime you update the field of the class
    // put in a random number or just increment the number
    @Serial
    private static final long serialVersionUID = 2L;

    private final User sender;
    private final User target;
    private String message;
    private boolean visibilitySender;
    private boolean visibilityReceiver;
    private LocalDateTime time;

    /**
     * Create an instance of message. The message time would be set as the time when the instance is created
     *
     * @param sender  sender of the message
     * @param target  target of the message
     * @param message the message body
     * @throws IllegalTargetException when both sender and target are the same role
     */
    public Message(User sender, User target, String message) throws IllegalTargetException {
        //check the sender target status
        if (User.checkRole(sender) == Role.BUYER ^ User.checkRole(target) == Role.SELLER)
            throw new IllegalTargetException("Cannot have both Buyers/Sellers in a message");
        this.sender = sender;
        this.target = target;
        this.message = message;
        this.visibilityReceiver = true;
        this.visibilitySender = true;
        setTimeToNow();
    }

    /**
     * Create an instance of message. The message time would be set as the time when the instance is created
     *
     * @param sender   sender of the message
     * @param target   target of the message
     * @param textFile the message body
     * @throws IOException            when IOException occurs(including file not found)
     * @throws IllegalTargetException when both sender and target are the same role
     */
    public Message(User sender, User target, File textFile) throws IOException {
        this(sender, target, "");
        try {
            editMessage(sender, textFile);
        } catch (IllegalUserAccessException e) {
            System.err.printf("IllegalUserAccessException: %s @ Message constructor. That shouldn't happen...\n",
                    e.getMessage());
        }
    }

    /**
     * Update time field to now
     */
    private void setTimeToNow() {
        this.time = LocalDateTime.now(ZoneId.of("US/Eastern"));
    }

    /**
     * edit the body of the message and update the timestamp
     *
     * @param newMessage  the new message
     * @param requestUser the user requesting the change
     * @throws IllegalUserAccessException when the user requesting the action is not the sender
     */
    public void editMessage(User requestUser, String newMessage) throws IllegalUserAccessException {
        if (!isSender(requestUser))
            throw new IllegalUserAccessException("User is not a sender therefore cannot edit the message");
        this.message = newMessage;
        setTimeToNow();
    }

    /**
     * check to see if the user is the sender of the message
     * Provide an exception-free way to check edit permission
     *
     * @param requestUser the user requesting the action
     * @return true if the user is authorized(is the sender)
     */
    protected boolean isSender(User requestUser) {
        return sender.equals(requestUser);
    }

    /**
     * check to see if the user is a participant of the message
     * Provide an exception-free way to check access permission
     *
     * @param requestUser the user requesting the action
     * @return true if the user is authorized(is a participant)
     */
    protected boolean isParticipant(User requestUser) {
        return requestUser.equals(sender) || requestUser.equals(target);
    }

    /**
     * edit the body of the message and update the timestamp
     *
     * @param textFile    the .txt file that contains the message
     * @param requestUser the user requesting the change
     * @throws IOException                when IOException occurs(including file not found)
     * @throws IllegalUserAccessException when the user requesting the action is not the sender
     */
    public void editMessage(User requestUser, File textFile) throws IOException, IllegalUserAccessException {
        if (!isSender(requestUser))
            throw new IllegalUserAccessException("User is not a sender therefore cannot edit the message");
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bfr = new BufferedReader(new FileReader(textFile))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
            this.message = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
            setTimeToNow();
        }
    }

    /**
     * set the visible status of the message
     *
     * @param actionUser the user that's performing the deletion action
     * @return true if the instance can be deleted(if it's invisible to both parties)
     * @throws IllegalUserAccessException when the action user is not a participant of the message
     */
    public boolean deleteMessage(User actionUser) throws IllegalUserAccessException {
        if (actionUser.equals(sender))
            visibilitySender = false;
        else if (actionUser.equals(target))
            visibilityReceiver = false;
        else
            throw new IllegalUserAccessException("User is not a participant of the message!");
        return !visibilityReceiver && !visibilitySender;
    }

    /**
     * @return the content of the message
     * @throws IllegalUserAccessException when a requesting user is not a participant of the message
     */
    protected String getMessage(User requestingUser) {
        if (!isParticipant(requestingUser))
            throw new IllegalUserAccessException("User is not a participant of the message!");
        return this.message;
    }

    /**
     *
     * @return the sender of the message
     */
    protected User getSender() {
        return this.sender;
    }


    /**
     * method that format each field for csv export
     *
     * @return string of Participants,Message sender,timestamp,content
     */
    protected String fileToString() {
        return String.format("%s,%s,%s,%s", User.userName(target), User.userName(sender), dtf.format(time), message);
    }

    /**
     * @return the conversation in the format of "sender: content \n time"
     */
    @Override
    public String toString() {
        return String.format("%s: %s\n%s", User.userName(sender), message, dtf.format(time));
    }
}
