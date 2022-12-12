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
/**
 * Register
 * <br>
 * A register screen that prompt the user to register for a new account
 *
 * @author Yulin Lin, Samson Tesfagiorgis
 * @version 12/11/2022
 */
public class Register extends JFrame {

    private JPanel contentPane;
    private JLabel lblUsername;
    private JTextField usernameField;
    private JLabel lblPwd;
    private JTextField pwdField;
    private JLabel lblMessage;

    private JTextField emailField;
    private JLabel lblEmail;
    private JCheckBox buyerCheckBox;
    private JCheckBox sellerCheckBox;

    private ClientCore client;
    private String[] listOfUsernames;
    private Listener listener;

    /**
     * Create the frame.
     */
    public Register(ClientCore client, String[] listOfUsernames) {
        this.client = client;
        this.listOfUsernames = listOfUsernames;
        setResizable(false);
        setTitle("Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        lblUsername = new JLabel("username");
        lblUsername.setBounds(12, 70, 101, 20);
        contentPane.add(lblUsername);

        usernameField = new JTextField();
        usernameField.setColumns(10);
        usernameField.setBounds(144, 70, 250, 20);
        contentPane.add(usernameField);

        lblPwd = new JLabel("password");
        lblPwd.setBounds(12, 100, 100, 20);
        contentPane.add(lblPwd);

        pwdField = new JTextField();
        pwdField.setColumns(10);
        pwdField.setBounds(144, 100, 250, 20);
        contentPane.add(pwdField);

        lblEmail = new JLabel("Email");
        lblEmail.setBounds(12, 130, 101, 20);
        contentPane.add(lblEmail);

        emailField = new JTextField();
        emailField.setColumns(10);
        emailField.setBounds(144, 130, 250, 20);
        contentPane.add(emailField);

        lblMessage = new JLabel("");
        lblMessage.setForeground(new Color(224, 27, 36));
        lblMessage.setBounds(44, 151, 350, 20);
        contentPane.add(lblMessage);

        buyerCheckBox = new JCheckBox("buyer");
        buyerCheckBox.setBounds(12, 227, 66, 28);
        contentPane.add(buyerCheckBox);

        sellerCheckBox = new JCheckBox("seller");
        sellerCheckBox.setBounds(109, 227, 66, 28);
        contentPane.add(sellerCheckBox);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Login login = new Login(client, listOfUsernames);
                dispose();
                login.setVisible(true);
            }
        });
        btnLogin.setBounds(219, 226, 83, 25);
        contentPane.add(btnLogin);

        JButton btnRegister = new JButton("Register");
        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                if (username.isBlank()) {
                    lblMessage.setText("Please enter a valid username");
                    return;
                }
                if (Arrays.asList(listOfUsernames).contains(username)) {
                    lblMessage.setText("Username already taken!");
                    return;
                }
                String password = pwdField.getText();
                if (password.isBlank()) {
                    lblMessage.setText("Please enter a valid password");
                    return;
                }
                String email = emailField.getText();
                if (!email.matches("\\b[\\w.%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\\b")) {
                    lblMessage.setText("Please enter a valid email");
                    return;
                }
                if (buyerCheckBox.isSelected() ^ sellerCheckBox.isSelected()) {
                    register(username, password, email, buyerCheckBox.isSelected());
                } else
                    lblMessage.setText("Please select either buyer or seller!");
            }
        });
        btnRegister.setBounds(314, 226, 94, 25);
        contentPane.add(btnRegister);
    }

    public void register(String username, String password, String email, boolean buyer) {
        System.out.println(username + ", " + password + ", " + email +
                ", " + (buyer ? "buyer" : "seller"));
        client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.REGISTER,
                (buyer ? "buyer" : "seller"), username, email, password));
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
                GUI clientGUI = new GUI(client, listOfUsernames, packet.args,
                        packet.args.length == 6);
            }
        } else {
            ErrorPacket errorPacket = listener.getErrorPacket();
            switch (errorPacket.errorType) {
                case EMAIL_FORMAT:
                    lblMessage.setText("Wrong email format - from server!");
                    return;
                case ILLEGAL_USER_NAME:
                    lblMessage.setText("Username already taken - from Server!");
                    return;
                case ILLEGAL_PARAMETER:
                    lblMessage.setText("tell the guy programmed it that he spelled buyer/seller wrong!");
                case ILLEGAL_REQUEST_FORMAT:
                    lblMessage.setText("The guy programmed this made a mistake :((");
                    return;
                default:
                    System.out.println(errorPacket.errorType);
                    System.out.println("That's was unexpected....");
            }
        }
    }
}
