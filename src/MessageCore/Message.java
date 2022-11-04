package MessageCore;

import UserCore.*;

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
     * @param sender sender of the message
     * @param target target of the message
     * @param message the message body
     * @throws IllegalArgumentException when both sender and target are the same role
     */
    public Message(User sender, User target, String message) {
        //check the sender target status
        if (User.checkRole(sender) == Role.BUYER ^ User.checkRole(target) == Role.SELLER)
            throw new IllegalArgumentException("Cannot have both Buyers/Sellers in a message");
        this.sender = sender;
        this.target = target;
        this.message = message;
        this.visibilityReceiver = true;
        this.visibilitySender = true;
        setTimeToNow();
    }

    /**
     * Update time field to now
     */
    private void setTimeToNow() {
        this.time = LocalDateTime.now(ZoneId.of("US/Eastern"));
    }

    /**
     * edit the body of the message and update the timestamp
     * @param newMessage the new message
     */
    public void editMessage(String newMessage) {
        this.message = newMessage;
        setTimeToNow();
    }

    /**
     * edit the body of the message and update the timestamp
     * @param textFile the .txt file that contains the message
     * @throws FileNotFoundException when the text file is not found
     */
    public void editMessage(File textFile) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bfr = new BufferedReader(new FileReader(textFile))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
            this.message = stringBuilder.toString();
            setTimeToNow();
        }
    }

    /**
     * set the visible status of the message
     * @param actionUser the user that's performing the deletion action
     * @return true if the instance can be deleted(if it's invisible to both parties)
     */
    public boolean deleteMessage(User actionUser) {
        if (actionUser.equals(sender))
            visibilitySender = false;
        else if (actionUser.equals(target))
            visibilityReceiver = false;
        else
            throw new IllegalArgumentException("User is not a participant of the message!");
        return !visibilityReceiver && !visibilitySender;
    }

    /**
     * @return the content of the message
     */
    protected String getMessage() {
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
     * @return string of Participants,Message sender,timestamp,content
     */
    protected String fileToString() {
        return String.format("%s,%s,%s,%s", User.userName(target), User.userName(sender), dtf.format(time), message);
    }


    //debugging purposes for now
    @Override
    public String toString() {
        return "Message{" +
                "sender=" + sender +
                ", target=" + target +
                ", message='" + message + '\'' +
                ", visibilitySender=" + visibilitySender +
                ", visibilityReceiver=" + visibilityReceiver +
                ", time=" + dtf.format(time) +
                '}';
    }
}
