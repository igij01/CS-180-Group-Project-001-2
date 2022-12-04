package Server;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Queue;

public class NotificationFactory {
    private NotificationFactory() {
    }

    /**
     * creates a thread and start it to notify the target
     *
     * @param selector     the selector
     * @param target       the target of the message
     * @param updatePacket the update packet
     */
    public static void runNotificationThread(Selector selector, SelectionKey target, ByteBuffer updatePacket) {
        Notification notification = new Notification(selector, target, updatePacket);
        notification.start();
    }

    /**
     * creates a thread and start it to notify everyone of a packet to write
     *
     * @param selector     the selector
     * @param updatePacket the update packet
     */
    public static void runNotificationThread(Selector selector, ByteBuffer updatePacket) {
        Notification notification = new Notification(selector, updatePacket);
        notification.start();
    }

    private static class Notification extends Thread {
        private final Selector selector;
        private final SelectionKey target;
        private final ByteBuffer updatePacket;

        private Notification(Selector selector, SelectionKey target, ByteBuffer updatePacket) {
            this.selector = selector;
            this.target = target;
            this.updatePacket = updatePacket;
        }

        private Notification(Selector selector, ByteBuffer updatePacket) {
            this(selector, null, updatePacket);
        }

        @Override
        public void run() {
            if (selector.isOpen()) {
                if (target != null && selector.keys().contains(target)) {
                    if (target.channel() instanceof SocketChannel) {
                        ((Queue<ByteBuffer>) target.attachment()).add(updatePacket);
                        target.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE); //enable write flag
                    }
                } else {
                    for (SelectionKey key : selector.keys()) {
                        if (key.channel() instanceof SocketChannel) {
                            ((Queue<ByteBuffer>) key.attachment()).add(updatePacket);
                            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE); //enable write flag
                        }
                    }
                }
                selector.wakeup();
            }
        }
    }

}
