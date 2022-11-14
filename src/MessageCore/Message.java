package MessageCore;

import UserCore.Role;
import UserCore.User;

import java.io.Serial;
import java.io.Serializable;
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

    private boolean readByTarget;
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
        this.readByTarget = false;
        setTimeToNow();
    }

    public String getMessage() {
        return message;
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
        this.message = newMessage + "\n\t  (edited)";
        readByTarget = false;
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

    protected boolean visibleToUser(User requestUser) {
        if (!isParticipant(requestUser))
            throw new IllegalUserAccessException("User is not a participant of the message!");
        if (isSender(requestUser))
            return visibilitySender;
        return visibilityReceiver;
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
     * call this method when the user have read the message.
     * If the user passed in is the sender, it will be ignored.
     *
     * @param requestingUser the user that read the message
     * @throws IllegalUserAccessException when the user is not a participant
     */
    protected void readByTarget(User requestingUser) {
        if (!isParticipant(requestingUser))
            throw new IllegalUserAccessException("User is not a participant of the message!");
        else if (!isSender(requestingUser))
            this.readByTarget = true;
    }

    /**
     * check whether the message is new to this user
     * if the user is the sender, it will always return false
     *
     * @param requestingUser the user requesting this action
     * @return true if the message is new and the requesting user is the receiver
     */
    protected boolean isNew(User requestingUser) {
        if (!isParticipant(requestingUser))
            throw new IllegalUserAccessException("User is not a participant of the message!");
        else if (!isSender(requestingUser))
            return !readByTarget;
        return false;
    }

    /**
     * @return the sender of the message
     */
    protected User getSender() {
        return this.sender;
    }

    /**
     * @return a list of all the words in the message, including duplicates
     */
    protected String[] words() {
        return this.message.toLowerCase().replaceAll("[^\\w']", " ")
                .split("\\s+");
    }


    /**
     * method that format each field for csv export
     *
     * @param requestingUser the user requesting this action
     * @return string of receiver,sender,timestamp,content
     * @throws IllegalUserAccessException if the user is not a participant of the message
     */
    protected String fileToString(User requestingUser) {
        if (!isParticipant(requestingUser))
            throw new IllegalUserAccessException();
        return String.format("%s,%s,%s,%s", User.userName(target), User.userName(sender), dtf.format(time), message);
    }

    /**
     * @param requestingUser the user requesting this action
     * @return the conversation in the format of "*sender: content \n time" if there is a new message
     * null if the request user cannot see the message
     * @throws IllegalUserAccessException if the user is not a participant of the message
     */
    public String toStringUser(User requestingUser) {
        String toString = String.format("%c%s: %s\n %s", ((readByTarget || isSender(requestingUser)) ? ' ' : '*'),
                User.userName(sender), message, dtf.format(time));
        if (!isParticipant(requestingUser))
            throw new IllegalUserAccessException();
        if (!isSender(requestingUser))
            readByTarget = true;
        if ((isSender(requestingUser) && !visibilitySender) || (!isSender(requestingUser) && !visibilityReceiver))
            return null;
        return toString;
    }
}
