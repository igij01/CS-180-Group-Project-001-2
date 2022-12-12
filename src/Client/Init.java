package Client;

import Protocol.DataPacket;
import Protocol.PacketDeserializer;
import Protocol.ResponsePacket;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.UnresolvedAddressException;
import java.nio.channels.UnsupportedAddressTypeException;
import java.util.Objects;

/**
 * Init
 * <br>
 * Start of client execution; show a screen for prompting the user to put in the server addr and the port
 *
 * @author Yulin Lin, 001
 * @version 12/11/2022
 */
public class Init extends JFrame {
    private JPanel contentPane;
    private JTextField serverField;
    private JTextField portField;
    private JLabel lblMessage;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Init frame = new Init();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public Init() {
        setResizable(false);
        setTitle("Init");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 200);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblServer = new JLabel("server");
        lblServer.setBounds(12, 12, 56, 20);
        contentPane.add(lblServer);

        serverField = new JTextField();
        serverField.setText("localhost");
        serverField.setBounds(144, 13, 250, 20);
        contentPane.add(serverField);
        serverField.setColumns(10);

        JLabel lblPort = new JLabel("port number");
        lblPort.setBounds(12, 44, 101, 20);
        contentPane.add(lblPort);

        portField = new JTextField();
        portField.setText("5050");
        portField.setColumns(10);
        portField.setBounds(144, 45, 250, 20);
        contentPane.add(portField);

        lblMessage = new JLabel("");
        lblMessage.setForeground(new Color(224, 27, 36));
        lblMessage.setBounds(44, 80, 350, 20);
        contentPane.add(lblMessage);

        JButton btnInit = new JButton("Connect");
        btnInit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String server = serverField.getText();
                if (server.isBlank()) {
                    lblMessage.setText("Please enter a valid server");
                    return;
                }
                int port = 0;
                try {
                    port = Integer.parseInt(portField.getText());
                } catch (NumberFormatException exception) {
                    lblMessage.setText("Please enter a number for the port!");
                    return;
                }
                try {
                    initialize(server, port);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        });
        btnInit.setBounds(30, 120, 117, 25);
        contentPane.add(btnInit);
    }

    public void initialize(String server, int port) throws IOException {
        final ByteBuffer readBuffer = ByteBuffer.allocate(0x1000);
        PacketDeserializer deserializer = new PacketDeserializer();
        SocketChannel channel;
        SocketAddress address;
        try {
            address = new InetSocketAddress(server, port);
        } catch (IllegalArgumentException e) {
            lblMessage.setText("Invalid server name or port number!");
            return;
        }

        ResponsePacket packet = null;

        try {
            channel = SocketChannel.open(address);
            readBuffer.clear();
            int read = channel.read(readBuffer);
            if (read == -1) {
                lblMessage.setText("Server closed!");
                return;
            } else if (read > 0) {
                readBuffer.flip();
                ByteBuffer buffer = ByteBuffer.allocate(readBuffer.remaining());
                buffer = buffer.put(readBuffer);
                packet = (ResponsePacket) deserializer.packetDeserialize(buffer);
                if (packet != null) {
                    System.out.println(packet.args[0]);
                }
            }
            channel.configureBlocking(false);
        } catch (IOException e) {
            lblMessage.setText("Server not opened!");
            return;
        } catch (UnresolvedAddressException | UnsupportedAddressTypeException e) {
            lblMessage.setText("Please enter a valid server name!");
            return;
        }
        assert packet != null;
        ClientCore client = new ClientCore(channel, deserializer);
        client.start();
        Login login = new Login(client, PacketAssembler.convertStringToStringArray(packet.args[0]));
        dispose();
        login.setVisible(true);
    }
}
