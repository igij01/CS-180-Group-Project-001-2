package GUI;

import UserCore.FullUser;
import UserCore.InvalidPasswordException;
import UserCore.PublicInformation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends Thread implements ActionListener{
    JButton login;
    JButton createAcc;
    JTextField userText;
    JPasswordField passText;
    FullUser user;

    public static void Menu() {
        JFrame frame = new JFrame("Basically Facebook");
        frame.setSize(750,500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);
        JMenuItem space = new JMenuItem("");

        ImageIcon imageIcon = new ImageIcon("profile.png");
        Image image = imageIcon.getImage();
        Image img = image.getScaledInstance(15, 15,  java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(img);
        JMenuItem profile = new JMenuItem("profile",
                imageIcon);
        menuBar.add(profile);

        ImageIcon imageIcon1 = new ImageIcon("search.png");
        Image image1 = imageIcon1.getImage();
        Image img1 = image1.getScaledInstance(15, 15,  java.awt.Image.SCALE_SMOOTH);
        imageIcon1 = new ImageIcon(img1);
        JMenuItem search = new JMenuItem("search  ",
                imageIcon1);
        menuBar.add(search);

        ImageIcon imageIcon2 = new ImageIcon("logout.png");
        Image image2 = imageIcon2.getImage();
        Image img2 = image2.getScaledInstance(15, 15,  java.awt.Image.SCALE_SMOOTH);
        imageIcon2 = new ImageIcon(img2);
        JMenuItem logout = new JMenuItem("logout",
                imageIcon2);
        menuBar.add(logout);
        menuBar.add(space);
        frame.setJMenuBar(menuBar);

        profile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                // set the profile window to visible
            }
        });

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuBar.setVisible(false);
                //Search(); to be implemented
            }
        });

        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Login(); //return them to the login page
            }
        });


    }



    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == login) {
                if (isValidLogin()) {
                    Menu();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Password or Username", "Try Again", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (e.getSource() == createAcc) {

            }
        }
    };
    private boolean isValidLogin() {
        try {
            String username = userText.getText();
            String password = String.valueOf(passText.getPassword());
            user = PublicInformation.login(username, password);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public void run() {
        JFrame loginFrame = new JFrame("Login");
        JPanel panel = new JPanel();
        loginFrame.setSize(350,200);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setLayout(null);
        loginFrame.add(panel);

        JLabel username = new JLabel("Username");
        username.setBounds(10,20,80,25);
        panel.add(username);
        userText = new JTextField();
        userText.setBounds(100,20,150,25);
        panel.add(userText);

        JLabel password = new JLabel("Password");
        password.setBounds(10,50,150,25);
        panel.add(password);
        passText = new JPasswordField();
        passText.setBounds(100,50,150,25);
        panel.add(passText);

        login = new JButton("Login");
        login.setBounds(100,80,80,25);
        login.addActionListener(actionListener);
        panel.add(login);

        createAcc = new JButton("Create Account");
        createAcc.setBounds(175,80,125,25);
        panel.add(createAcc);
        loginFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new GUI());
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
