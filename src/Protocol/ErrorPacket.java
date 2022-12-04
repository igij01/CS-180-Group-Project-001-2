package Protocol;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * ErrorPacket
 * <p>
 * The mean for the server to pass the exception to the client to handle
 *
 * @author Yulin Lin, 001
 * @version 12/3/2022
 */
public class ErrorPacket implements Externalizable {
    private ProtocolErrorType errorType;
    private ProtocolRequestType requestType;
    private String errorMessage;

    public ErrorPacket() {

    }

    /**
     * create an Error Packet to be sent to the client
     *
     * @param type the type of the error
     * @param e    the exception threw
     */
    public ErrorPacket(ProtocolRequestType type, Exception e) {
        this.errorType = ProtocolErrorType.getExceptionsFromException(e);
        this.requestType = type;
        this.errorMessage = e.getMessage();
    }

    /**
     * create an Error Packet that is not caused by a specific request type
     *
     * @param e the exception threw
     */
    public ErrorPacket(Exception e) {
        this(null, e);
    }

    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeObject(errorType);
        objectOutput.writeObject(requestType);
        objectOutput.writeUTF(errorMessage);
    }

    @Override
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        this.errorType = (ProtocolErrorType) objectInput.readObject();
        this.requestType = (ProtocolRequestType) objectInput.readObject();
        this.errorMessage = objectInput.readUTF();
    }
}
