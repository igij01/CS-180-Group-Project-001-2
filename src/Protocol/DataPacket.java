package Protocol;

import java.io.*;

public class DataPacket implements Externalizable {
    public Request request;
    public String[] args;

    /**
     * create a data packet to send across nio channels
     * @param request the type of the packet
     * @param args the arguments
     * @throws IllegalParameterCount when the arguments count is not the same as expected
     */
    public DataPacket(Request request, String... args) throws IllegalParameterCount {
        this.request = request;
        if (args.length != request.getParamCount())
            throw new IllegalParameterCount(request);
        this.args = args;
    }

    public DataPacket() {

    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(request);
        out.writeInt(args.length);
        if (args.length > 0) {
            for (String s : args) {
                out.writeUTF(s);
            }
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.request = (Request) in.readObject();
        int length = in.readInt();
        this.args = new String[length];
        for (int i = 0; i < length; i++) {
            this.args[i] = in.readUTF();
        }
    }

    public static byte[] serialize(DataPacket packet) {
        try(ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out)) {
            oos.writeObject(packet);
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
