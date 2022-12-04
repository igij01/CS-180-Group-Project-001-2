package Protocol;

import java.io.*;
import java.nio.ByteBuffer;

public class DataPacket implements Externalizable {
    public ProtocolRequestType protocolRequestType;
    public String[] args;

    /**
     * create a data packet to send to the server
     *
     * @param protocolRequestType the type of the request
     * @param args                the arguments
     * @throws IllegalParameterCount when the arguments count is not the same as expected
     */
    public DataPacket(ProtocolRequestType protocolRequestType, String... args) throws IllegalParameterCount {
        this.protocolRequestType = protocolRequestType;
        if (args.length != protocolRequestType.getParamCount())
            throw new IllegalParameterCount(protocolRequestType);
        this.args = args;
    }

    public DataPacket() {

    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(protocolRequestType);
        out.writeInt(args.length);
        if (args.length > 0) {
            for (String s : args) {
                out.writeUTF(s);
            }
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.protocolRequestType = (ProtocolRequestType) in.readObject();
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

    /**
     * deserialize serialized packet
     *
     * @param buffer the buffer that contains the serialized packet
     * @return the deserialized packet as {@code Object}
     */
    public static Object packetDeserialize(ByteBuffer buffer) {
        byte[] packet = buffer.array();
        try (ByteArrayInputStream in = new ByteArrayInputStream(packet);
             ObjectInputStream oin = new ObjectInputStream(in)) {
            return oin.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
