package Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Hashtable;
import java.util.Set;

public class TestGroundServer {
    private Selector selector;
    private final InetSocketAddress listenAddress;
    private final static int PORT = 4000;
    private Hashtable<SocketAddress, String> authenticateTable;

    public static void main(String[] args) {

    }

    public TestGroundServer(String address) {
        listenAddress = new InetSocketAddress(address, PORT);
        authenticateTable = new Hashtable<>();
    }

    private void startServer() throws IOException {
        selector = Selector.open();
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(listenAddress);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Opened port on port " + PORT);

            while (true) {
                int readyCount = selector.select();
                if (readyCount == 0)
                    continue;

                Set<SelectionKey> readKeys = selector.selectedKeys();
                for (SelectionKey key : readKeys) {
                    if (!key.isValid())
                        continue;
                    if (key.isAcceptable())
                        accept(key);
                    else if (key.isReadable())
                        read(key);
                    else if (key.isWritable())
                        write(key);
                }
            }
        }

    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();
        if (channel == null)
            return;
        channel.configureBlocking(false);
        Socket socket = channel.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
        System.out.println("Connected to: " + remoteAddr);

        // init, authentication, etc...
        String rawInitData = read(key);
        if (rawInitData != null) {
            String[] data = rawInitData.split("\n");
            if (data[0] == null || data[0].isBlank()) {
                authenticateTable.put(remoteAddr, null);
            } else if (!authenticateTable.contains(remoteAddr)) {
                authenticateTable.put(remoteAddr, data[0]);
            }
            if (data[1].equalsIgnoreCase("R") || data[1].equalsIgnoreCase("read")) {
                channel.register(selector, SelectionKey.OP_READ);
            } else if (data[1].equalsIgnoreCase("W") || data[1].equalsIgnoreCase("write")) {
                channel.register(selector, SelectionKey.OP_WRITE);
            }
        }


        channel.register(selector, SelectionKey.OP_READ);
    }

    private String read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int numRead = -1;
        numRead = channel.read(buffer);

        if (numRead == -1) {
            Socket socket = channel.socket();
            SocketAddress remoteAddr = socket.getRemoteSocketAddress();
            System.out.println("Connection closed by client: " + remoteAddr);
            channel.close();
            key.cancel();
            return null;
        }

        byte[] data = new byte[numRead];
        System.arraycopy(buffer.array(), 0, data, 0, numRead);
        String str = new String(data);
        System.out.println("Got: " + str);
        return str;
    }

    private void write(SelectionKey key) {

    }

}
