package Protocol;

import java.io.*;
import java.nio.ByteBuffer;

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
     * deserialize serialized response packet
     *
     * @param buffer the buffer that contains the serialized packet
     * @return the deserialized packet
     */
    public static ResponsePacket packetDeserialize(ByteBuffer buffer) {
        byte[] packet = buffer.array();
        try (ByteArrayInputStream in = new ByteArrayInputStream(packet);
             ObjectInputStream oin = new ObjectInputStream(in)) {
            return (ResponsePacket) oin.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
