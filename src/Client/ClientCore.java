package Client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class ClientCore extends Thread {

    private SocketChannel channel;
    private SocketAddress address;
    private ArrayBlockingQueue<ByteBuffer> writeQueue;
    private ArrayBlockingQueue<ByteBuffer> readQueue;
    private Selector selector = Selector.open();

    public ClientCore(SocketAddress server) throws IOException {
        this.address = server;
        this.writeQueue = new ArrayBlockingQueue<>(1000);
        this.readQueue = new ArrayBlockingQueue<>(1000);

    }

    @Override
    public void run() {
        final ByteBuffer readBuffer = ByteBuffer.allocate(0x1000);
        try {
            this.channel = SocketChannel.open(address);
            this.channel.configureBlocking(false);
            this.channel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while (selector.isOpen()) {
                int select = selector.select();
                if (select > 0) {
                    for (SelectionKey key : selector.selectedKeys()) {
                        if (key.isValid() && key.isReadable()) {
                            SocketChannel socket = ((SocketChannel) key.channel());
                            readBuffer.clear();
                            int read = socket.read(readBuffer);
                            if (read == -1) {
                                System.out.println("Socket Closed " + key.channel());
                                socket.close();
                                continue; //socket is closed. continue loop
                            }

                            //we will add what the client sent to the queue to echo it back
                            if (read > 0) {
                                readBuffer.flip();
                                ByteBuffer buffer = ByteBuffer.allocate(readBuffer.remaining());
                                buffer = buffer.put(readBuffer);
                                //readQueue.add(buffer);
                            }
                            key.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ); //enable write flag
                        }

                        if (key.isValid() && key.isWritable()) {
                            System.out.println("Writable: " + key.channel());
                            SocketChannel socket = (SocketChannel) key.channel();

                            //retrieve attachment(ArrayBlockingQueue<ByteBuffer>)
                            Queue<Buffer> dataToWrite = (Queue<Buffer>) key.attachment();

                            //only remove from queue once we have completely written
                            //this is why we call peek first, and only remove once (buffer.remaining() == 0)
                            for (ByteBuffer buffer; (buffer = (ByteBuffer) dataToWrite.peek()) != null; ) {
                                socket.write(buffer);
                                if (buffer.remaining() == 0) dataToWrite.remove();
                                else break; //can not write anymore. Wait for next write event
                            }

                            //once queue is empty we need to stop watching for write events
                            if (dataToWrite.isEmpty()) key.interestOps(SelectionKey.OP_READ);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addByteBufferToWrite(ByteBuffer bfr) {
        writeQueue.add(bfr);
        for (SelectionKey key : selector.keys()) {
            if (key.channel() instanceof SocketChannel) {
                key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE); //enable write flag
            }
        }
        selector.wakeup();
    }

    public static void main(String[] args) {

    }
}
