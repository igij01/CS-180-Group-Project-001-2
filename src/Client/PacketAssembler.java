package Client;

import Protocol.DataPacket;
import Protocol.Request;

import java.nio.ByteBuffer;
import java.util.Objects;

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
    public static ByteBuffer assemblePacket(Request instruction, String... args) {
        DataPacket packet = new DataPacket(instruction, args);
        return ByteBuffer.wrap(Objects.requireNonNull(DataPacket.serialize(packet)));
    }
}
