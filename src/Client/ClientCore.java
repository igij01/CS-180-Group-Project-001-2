package Client;

import Protocol.DataPacket;
import Protocol.ErrorPacket;
import Protocol.ProtocolRequestType;
import Protocol.ResponsePacket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;

public class ClientCore extends Thread {

    private SocketChannel channel;
    private SocketAddress address;
    private ArrayBlockingQueue<ByteBuffer> writeQueue;
    private ArrayBlockingQueue<Object> readQueue;
    private Selector selector = Selector.open();

    public ClientCore(SocketChannel channel) throws IOException {
        this.channel = channel;
        this.channel.register(selector, SelectionKey.OP_WRITE);
        this.writeQueue = new ArrayBlockingQueue<>(1000);
        this.readQueue = new ArrayBlockingQueue<>(1000);

    }

    public ClientCore(SocketAddress server) throws IOException {
        this.address = server;
        this.writeQueue = new ArrayBlockingQueue<>(1000);
        this.readQueue = new ArrayBlockingQueue<>(1000);
        final ByteBuffer readBuffer = ByteBuffer.allocate(0x1000);
        try {
            this.channel = SocketChannel.open(address);
            readBuffer.clear();
            int read = channel.read(readBuffer);
            if (read == -1) {
                System.out.println("Socket Closed ");
                channel.close();
            } else if (read > 0) {
                readBuffer.flip();
                ByteBuffer buffer = ByteBuffer.allocate(readBuffer.remaining());
                buffer = buffer.put(readBuffer);
                ResponsePacket packet = (ResponsePacket) ResponsePacket.packetDeserialize(buffer);
                if (packet != null) {
                    System.out.println(packet.args[0]);
                }
            }
            this.channel.configureBlocking(false);
            this.channel.register(selector, SelectionKey.OP_WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        final ByteBuffer readBuffer = ByteBuffer.allocate(0x1000);
        try {
            while (selector.isOpen()) {
                int select = selector.select();
                if (select > 0) {
                    for (SelectionKey key : selector.selectedKeys()) {
                        if (key.isValid() && key.isReadable()) {
                            System.out.println("Readable: " + key.channel());
                            SocketChannel socket = ((SocketChannel) key.channel());
                            readBuffer.clear();
                            int read = socket.read(readBuffer);
                            if (read == -1) {
                                System.out.println("Socket Closed " + key.channel());
                                socket.close();
                                continue; //socket is closed. continue loop
                            }

                            if (read > 0) {
                                readBuffer.flip();
                                ByteBuffer buffer = ByteBuffer.allocate(readBuffer.remaining());
                                buffer = buffer.put(readBuffer);
                                boolean repeat = true;
                                Object responsePacket = ResponsePacket.packetDeserialize(buffer);
                                if (responsePacket != null) {
                                    readQueue.add(responsePacket);
                                    if (responsePacket instanceof ResponsePacket) {
                                        System.out.println(Arrays.toString(((ResponsePacket) responsePacket).args));
                                    } else if (responsePacket instanceof ErrorPacket) {
                                        System.out.println(((ErrorPacket) responsePacket).errorMessage);
                                    }
                                }
                                else
                                    repeat = false;

                                while(repeat) {
                                    responsePacket = ResponsePacket.packetDeserialize(null);
                                    if (responsePacket != null) {
                                        readQueue.add(responsePacket);
                                        if (responsePacket instanceof ResponsePacket) {
                                            System.out.println(Arrays.toString(((ResponsePacket) responsePacket).args));
                                        } else if (responsePacket instanceof ErrorPacket) {
                                            System.out.println(((ErrorPacket) responsePacket).errorMessage);
                                        }
                                    }
                                    else
                                        repeat = false;
                                }
                                //readQueue.add(buffer);
                            }
                            key.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ); //enable write flag
                        }

                        if (key.isValid() && key.isWritable()) {
                            System.out.println("Writable: " + key.channel());
                            SocketChannel socket = (SocketChannel) key.channel();

                            //only remove from queue once we have completely written
                            //this is why we call peek first, and only remove once (buffer.remaining() == 0)
                            for (ByteBuffer buffer; (buffer = this.writeQueue.peek()) != null; ) {
                                socket.write(buffer);
                                System.out.println("send");
                                if (buffer.remaining() == 0) this.writeQueue.remove();
                                else break; //can not write anymore. Wait for next write event
                            }

                            //once queue is empty we need to stop watching for write events
                            if (this.writeQueue.isEmpty()) key.interestOps(SelectionKey.OP_READ);
                        }
                    }
                    selector.selectedKeys().clear();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addByteBufferToWrite(ByteBuffer bfr) {
        writeQueue.add(bfr);
        System.out.println("added");
        for (SelectionKey key : selector.keys()) {
            if (key.channel() instanceof SocketChannel) {
                key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE); //enable write flag
            }
        }
        selector.wakeup();
    }

    public Iterator<Object> getReadQueueIterator() {
        return this.readQueue.iterator();
    }

    public Object readFromQueue() {
        return readQueue.peek();
    }

    public Object popFromQueue() {
        return readQueue.poll();
    }

    public boolean removeFromQueue(Object o) {
        System.out.println(Thread.currentThread().getStackTrace()[0]);
        return readQueue.remove(o);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ClientCore client = new ClientCore(new InetSocketAddress("localhost", 5050));
        client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.REGISTER, "buyer", "buyer",
                "mail@mail.com", "12345"));
        client.start();
        Thread.sleep(1000);
        client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.DISPLAY_PROFILE));
        Thread.sleep(500);
        client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.LOGOUT));
        Thread.sleep(500);
        client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.DISPLAY_PROFILE));
        client.join();
    }
}
