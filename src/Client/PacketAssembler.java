package Client;

import Protocol.DataPacket;
import Protocol.ProtocolRequestType;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * PacketAssembler
 * <br>
 * provides an abstraction to form a request in the form of DataPacket and serialize so it's ready to be send to the
 * server
 *
 * @author Yulin Lin, 001
 * @version 12/11/2022
 */
public class PacketAssembler {
    private PacketAssembler() {
    }

    /**
     * assemble the packet based on the instruction and parameters
     *
     * @param instruction the instruction/request
     * @param args        the params assoc. with that instructions
     * @return the ByteBuffer that's ready to be sent to the server
     */
    public static ByteBuffer assemblePacket(ProtocolRequestType instruction, String... args) {
        DataPacket packet = new DataPacket(instruction, args);
        return ByteBuffer.wrap(Objects.requireNonNull(DataPacket.serialize(packet)));
    }

    /**
     * split a string in the form of [a,b,c...]into an array of string
     * <br>
     * return {@code new String[0]} if the array is empty
     *
     * @param s the string that contains the string of an array
     * @return the array of string
     */
    public static String[] convertStringToStringArray(String s) {
        s = s.replace("[", "");
        s = s.replace("]", "");
        if (s.isBlank())
            return new String[0];
        return s.split(", ");
    }
}
