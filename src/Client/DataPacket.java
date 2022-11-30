package Client;

import java.io.*;

public class DataPacket implements Externalizable {
    Request request;
    String[] args;
    public DataPacket(Request request, String... args) {
        this.request = request;
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
