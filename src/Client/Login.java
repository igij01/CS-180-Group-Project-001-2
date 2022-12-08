package Client;

import Protocol.ErrorPacket;
import Protocol.ProtocolRequestType;
import Protocol.ProtocolResponseType;
import Protocol.ResponsePacket;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class Login extends JFrame {

    private JPanel contentPane;
    private JLabel lblUsername;
    private JTextField usernameField;
    private JLabel lblPwd;
    private JTextField pwdField;
    private JLabel lblMessage;

    private ClientCore client;
    private String[] listOfUsernames;
    private Listener listener;

    /**
     * Create the frame.
     */
    public Login(ClientCore client, String[] listOfUsernames) {
        this.client = client;
        this.listOfUsernames = listOfUsernames;
        setResizable(false);
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        lblUsername = new JLabel("username");
        lblUsername.setBounds(12, 76, 101, 20);
        contentPane.add(lblUsername);

        usernameField = new JTextField();
        usernameField.setColumns(10);
        usernameField.setBounds(144, 77, 250, 20);
        contentPane.add(usernameField);

        lblPwd = new JLabel("password");
        lblPwd.setBounds(12, 108, 101, 20);
        contentPane.add(lblPwd);

        pwdField = new JTextField();
        pwdField.setColumns(10);
        pwdField.setBounds(144, 109, 250, 20);
        contentPane.add(pwdField);

        lblMessage = new JLabel("");
        lblMessage.setForeground(new Color(224, 27, 36));
        lblMessage.setBounds(44, 151, 350, 20);
        contentPane.add(lblMessage);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                if (username.isBlank()) {
                    lblMessage.setText("Please enter a valid username");
                    return;
                } else if (!Arrays.asList(listOfUsernames).contains(username)) {
                    lblMessage.setText("Username does not exists!");
                    return;
                }
                String password = pwdField.getText();
                if (password.isBlank()) {
                    lblMessage.setText("Please enter a valid password");
                    return;
                }
                login(username, password);
            }
        });
        btnLogin.setBounds(30, 226, 117, 25);
        contentPane.add(btnLogin);

        JButton btnRegister = new JButton("Register");
        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Register register = new Register(client, listOfUsernames);
                dispose();
                register.setVisible(true);
            }
        });
        btnRegister.setBounds(291, 226, 117, 25);
        contentPane.add(btnRegister);
    }

    public void login(String username, String password) {
        System.out.println(username + ", " + password);
        client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.LOGIN, username, password));
        listener = new Listener(client, ProtocolResponseType.LOGIN_SUCCESS, null);
        listener.start();
        try {
            listener.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
        if (listener.getResponsePacket() != null) {
            System.out.println(listener.getResponsePacket().args[0]);
            dispose();
            client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.DISPLAY_PROFILE));
            listener = new Listener(client, ProtocolResponseType.PROFILE, ProtocolRequestType.DISPLAY_PROFILE);
            listener.start();
            try {
                listener.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
            ResponsePacket packet;
            if ((packet = listener.getResponsePacket()) != null) {
                String[] raw = packet.args;
                GUI clientGUI = new GUI(client, listOfUsernames, raw, Boolean.parseBoolean(raw[0]));
            }
        } else {
            ErrorPacket errorPacket = listener.getErrorPacket();
            switch (errorPacket.errorType) {
                case WRONG_PASSWORD:
                    lblMessage.setText("Wrong password!");
                    return;
                case ILLEGAL_USER_NAME:
                    lblMessage.setText("Username doesn't exists - from Server!");
                    return;
                case ILLEGAL_REQUEST_FORMAT:
                    lblMessage.setText("The guy programmed this made a mistake :((");
                    return;
                default:
                    System.out.println(errorPacket.errorType);
                    System.out.println("That's was unexpected....");
                    return;
            }
        }
    }
}
