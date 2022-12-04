package Protocol;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

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
}
