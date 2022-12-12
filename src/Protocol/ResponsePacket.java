package Protocol;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
/**
 * ErrorPacket
 * <p>
 * The mean for the server to pass the response to the client
 *
 * @author Yulin Lin, 001
 * @version 12/3/2022
 */
public class ResponsePacket implements Externalizable {
    public ProtocolResponseType protocolResponseType;
    public String[] args;

    /**
     * create a response packet to send across to clients
     *
     * @param protocolResponseType the type of the response packet
     * @param args                 the arguments
     */
    public ResponsePacket(ProtocolResponseType protocolResponseType, String... args) {
        this.protocolResponseType = protocolResponseType;
        this.args = args;
    }

    public ResponsePacket() {

    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(protocolResponseType);
        out.writeInt(args.length);
        if (args.length > 0) {
            for (String s : args) {
                out.writeUTF(s);
            }
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.protocolResponseType = (ProtocolResponseType) in.readObject();
        int length = in.readInt();
        this.args = new String[length];
        for (int i = 0; i < length; i++) {
            this.args[i] = in.readUTF();
        }
    }

    /**
     * Utility method that convert Object to its serialized form
     *
     * @param packet the packet to be serialized
     * @return a byte array
     */
    public static byte[] serialize(Object packet) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(out)) {
            oos.writeObject(packet);
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "ResponsePacket{" +
                "protocolResponseType=" + protocolResponseType +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
