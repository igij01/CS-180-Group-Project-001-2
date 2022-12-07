package Protocol;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class DataPacket implements Externalizable {
    public ProtocolRequestType protocolRequestType;
    public String[] args;
    public static byte[] remainingBytes = new byte[0];

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
    public static ArrayList<Object> packetDeserialize(ByteBuffer buffer) {
        ArrayList<Object> objects = new ArrayList<>();
        byte[] packet = buffer.array();
        if (remainingBytes.length > 0) {
            System.arraycopy(remainingBytes, 0, packet, 0, remainingBytes.length);
        }
        try (ByteArrayInputStream in = new ByteArrayInputStream(packet);
             BufferedInputStream bin = new BufferedInputStream(in);
             ObjectInputStream oin = new ObjectInputStream(bin)) {
            while (true) {
                bin.mark(0xFFFFF);
                remainingBytes = oin.readAllBytes();
                bin.reset();
                objects.add(oin.readObject());
            }
        } catch (EOFException e) {
            remainingBytes = new byte[0];
            return objects;
        } catch (StreamCorruptedException e) {
            System.out.println("incomplete");
            return objects;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
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
