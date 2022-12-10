package Protocol;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class DataPacket implements Externalizable {
    public ProtocolRequestType protocolRequestType;
    public String[] args;
    private static byte[] bytesLeft = new byte[0];

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
        for (String s : args) {
            out.writeUTF(s);
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
     * <p>
     * Note: <b>This is designed for server deserialization ONLY, for deserialization on the client side,
     * please use PacketDeserializer</b>
     *
     * @param buffer the buffer that contains the serialized packet
     * @return the deserialized packet as {@code Object}
     * @see PacketDeserializer#packetDeserialize(ByteBuffer)
     */
    public static DataPacket packetDeserializeServer(ByteBuffer buffer) {
        byte[] packet;
        if (buffer == null)
            packet = new byte[bytesLeft.length];
        else {
            byte[] dataPacket = buffer.array();
            packet = new byte[dataPacket.length + bytesLeft.length];
            System.arraycopy(dataPacket, 0, packet, 0, dataPacket.length);
        }
        if (bytesLeft.length > 0) {
            System.arraycopy(bytesLeft, 0, packet, 0, bytesLeft.length);
        }
        try (ByteArrayInputStream in = new ByteArrayInputStream(packet);
             ObjectInputStream oin = new ObjectInputStream(in)) {
            DataPacket o = (DataPacket) oin.readObject();
            bytesLeft = in.readAllBytes();
            return o;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "DataPacket{" +
                "protocolRequestType=" + protocolRequestType +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
