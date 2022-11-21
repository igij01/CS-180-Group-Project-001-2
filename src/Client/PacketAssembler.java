package Client;

import java.nio.ByteBuffer;

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
    private static ByteBuffer assemblePacket(String instruction, String... args) {
        StringBuilder str = new StringBuilder();
        str.append(instruction).append('#');
        for (String param : args) {
            str.append(param).append(',');
        }
        return ByteBuffer.wrap(str.toString().getBytes());
    }
}
