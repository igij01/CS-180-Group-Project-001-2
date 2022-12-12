package Protocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * PacketDeserializer
 * <br>
 * A class for deserializing the packet
 *
 * @author Yulin Lin, 001
 * @version 12/11/2022
 */
public class PacketDeserializer {
    private byte[] bytesLeft = new byte[0];

    public PacketDeserializer() {}

    /**
     * deserialize serialized packet
     * <p>
     * Note: technically it can stay static as long as each machine only run one instance of client,
     * however, for development purposes which tests multiple instances on the same machine, each instance MUST
     * have their own bytesLeft array to avoid data leak
     *
     * @param buffer the buffer that contains the serialized packet
     * @return the deserialized packet as {@code Object}
     */
    public Object packetDeserialize(ByteBuffer buffer) {
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
            Object o = oin.readObject();
            bytesLeft = in.readAllBytes();
            System.out.println("Bytes left: " + Arrays.toString(bytesLeft));
            return o;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}
