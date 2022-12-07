package Server;

import Protocol.DataPacket;
import Protocol.ProtocolResponseType;
import UserCore.PublicInformation;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * ServerCore
 * <p>
 * handles the main server IO
 *
 * @author Yulin Lin, 001
 * @version 11/21/2022
 * @implNote This is inspired from <a href="https://stackoverflow.com/questions/43928247/java-socketchannel-selector-
 * combine-write-channel-with-blocking-queue">...</a>
 */
public class ServerCore {
    public static void main(String... args) throws IOException {
        final Selector selector = Selector.open();
        final HashMap<SocketChannel, MessageSystem> table = new HashMap<>();
        PublicInformation.init();

        new Thread(() -> {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String command = scanner.nextLine();
                if (command.equalsIgnoreCase("shutdown")) {
                    try {
                        selector.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    for (Map.Entry<SocketChannel, MessageSystem> entry: table.entrySet()) {
                        entry.getValue().userLogOut();
                    }
                    PublicInformation.serialize();
                    return;
                }
            }
        }).start();

        MessageSystem.setSelector(selector);

        //create server on port 5050
        ServerSocketChannel server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.bind(new InetSocketAddress(5050));
        server.register(selector, SelectionKey.OP_ACCEPT);

        //reusable buffer
        final ByteBuffer readBuffer = ByteBuffer.allocate(1024);

        while (selector.isOpen()) {
            int selected = selector.select();
            System.out.println("Selected " + selected + (selected == 1 ? " key." : " keys."));
            if (selected > 0) {
                for (SelectionKey key : selector.selectedKeys()) {
                    if (key.isValid() && key.isReadable()) {
                        System.out.println("Readable: " + key.channel());
                        SocketChannel socket = ((SocketChannel) key.channel());
                        readBuffer.clear();
                        int read;
                        try {
                            read = socket.read(readBuffer);
                        } catch (Exception e) {
                            System.out.println("Socket Closed " + key.channel());
                            socket.close();
                            table.get(socket).userLogOut();
                            table.remove(socket);
                            continue; //socket is closed. continue loop
                        }
                        if (read == -1) {
                            System.out.println("Socket Closed " + key.channel());
                            socket.close();
                            table.get(socket).userLogOut();
                            table.remove(socket);
                            continue; //socket is closed. continue loop
                        }

                        //we will add what the client sent to the queue to echo it back
                        if (read > 0) {
                            readBuffer.flip();
                            ByteBuffer buffer = ByteBuffer.allocate(readBuffer.remaining());
                            buffer = buffer.put(readBuffer);
                            if (table.get(socket) == null) {
                                try {
                                    table.put(socket, new MessageSystem(buffer, read, key));
                                    ((Queue<Buffer>) key.attachment()).
                                            add(MessageSystem.toByteBufferPacket(
                                                    ProtocolResponseType.LOGIN_SUCCESS, "login success!"));
                                    System.out.println("login success");
                                } catch (Exception e) {
                                    ((Queue<Buffer>) key.attachment())
                                            .add(MessageSystem.sendException(e));
                                }
                            } else {
                                ((Queue<Buffer>) key.attachment()).addAll(table.get(socket).processRequest(buffer));
                            }


                            key.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ); //enable write flag
                        }
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

                    if (key.isValid() && key.isAcceptable()) {
                        System.out.println("Acceptable: " + key.channel());
                        SocketChannel socket = ((ServerSocketChannel) key.channel()).accept();

                        //send the list of usernames for start
                        socket.write(PublicInfo.sendAllUsernames()); //ERROR PRONE!

                        socket.configureBlocking(false);

                        //add a ArrayBlockingQueue<ByteBuffer> as an attachment for the socket
                        socket.register(selector, SelectionKey.OP_READ, new ArrayBlockingQueue<ByteBuffer>(1000));
                        table.put(socket, null);
                    }
                }
                selector.selectedKeys().clear(); //must clear all when finished or loop will continue selecting nothing
            }
        }
    }
}
