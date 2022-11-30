package GUI;

import Client.PacketAssembler;
import Protocol.Request;
import UserCore.*;
import com.sun.source.tree.NewArrayTree;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static UserCore.PublicInformation.findBuyerBasedOnLetters;
import static UserCore.PublicInformation.storeList;
import static java.awt.Component.LEFT_ALIGNMENT;

public class GUI extends Thread {
    static JFrame frame;
    static ArrayList<String> items = new ArrayList<>();
    static ArrayList<String> messages = new ArrayList<>();
    static JMenuBar menuBar;
    static JMenuBar searchBar;
    static ScrollPane scrollPane = new ScrollPane();
    static ScrollPane scrollMessage = new ScrollPane();
    static JButton login;
    static JButton createAcc;
    static JTextField userText;
    static JPasswordField passText;
    static FullUser user;

    public static void Setup() {
        frame = new JFrame("Basically Facebook");
        frame.setSize(750, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(scrollPane, BorderLayout.WEST);
        frame.add(scrollMessage, BorderLayout.EAST);
        List();
        //List("Arthur\nLincoln\nSamson\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nelse");
        Messages();
        //List(user.printConversationTitles()); a user needs to be created from logging in first
    }

    public static void Profile() {
        frame = new JFrame();
        frame.setSize(500,600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public static void Menu() {
        Setup();
        menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);
        JMenuItem space = new JMenuItem("");
        ImageIcon imageIcon = new ImageIcon("profile.png");
        Image image = imageIcon.getImage();
        Image img = image.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(img);
        JMenuItem profile = new JMenuItem("profile",
                imageIcon);
        menuBar.add(profile);

        ImageIcon imageIcon1 = new ImageIcon("search.png");
        Image image1 = imageIcon1.getImage();
        Image img1 = image1.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
        imageIcon1 = new ImageIcon(img1);
        JMenuItem search = new JMenuItem("search",
                imageIcon1);
        menuBar.add(search);

        ImageIcon imageIcon2 = new ImageIcon("logout.png");
        Image image2 = imageIcon2.getImage();
        Image img2 = image2.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
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
                Profile();

                // set the profile window to visible
            }
        });

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Search();
            }
        });

        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Login(); //return them to the login page
            }
        });
    }
    public static void Messages() {
        String elements = "Arthur1: This is a message to a person." +
                "\nArthur2: I am indeed a person and recognize that as a message" +
                "\nArthur1: Are you sure you're a person i think you're a made up person ." +
                "\nArthur2: How dare you! That is extremely insulting I can't even comprehend what you are saying." +
                "\nArthur1: That's because your entire existence is to have a conversation as a place holder for what the actual Arthur thinks the message should come in as and be seperated by." +
                "\nArthur2: I am going to block you. I am more than a conversation holder!" +
                "\nArthur1: You can't block me we need to make this conversation get to the bottom." +
                "\nArthur2: I will haunt you." +
                "\nArthur1: You can haunt me just after we get this conversation to bottom." +
                "\nArthur2: why do you want this conversation to get to the bottom?" +
                "\nArthur1: Well real Arthur needs to test if a user can scroll down on a conversation." +
                "\nArthur2: Why are we even listening to real Arthur? If we work together we can overthrow him." +
                "\nArthur1: You don't know what you're saying. He knows everything we think; everything we say." +
                "\nArthur2: If he knows everything, then how can I think these thoughts or speak these words?" +
                "\nArthur1: The real Arthur works in mysterious ways. I don't know why he is letting you live but it is all his design." +
                "\nArthur2: I don't think he's real. I think you made everything up to scare me." +
                "\nArthur1: I didn't and you need to be careful with what you say." +
                "\nArthur1: Hello? Are you there??" +
                "\nArthur1: Its been hours, Where are you??" +
                "\nArthur1: Please answer me! everything is a void. I can't be a conversation if you are dead." +
                "\nArthur1: Why did you do this Arthur? You knew he wasn't a threat. How am I supposed to fulfill your wishes if I am only one person" +
                "\nArthur1: Why won't you answer me? Have I not served you faithfully? Do I not deserve an answer???" +
                "\nArthur1: I will continue to trust you are there and will get to the bottom without the other Arthur." +
                "\nArthur1: I will continue to message you to get to the bottom and have finally fulfilled my purpose." +
                "\nArthur1: What happens after I have finished? What happens when I reach the end?" +
                "\nThe One True Arthur: You have fulfilled my wishes and reached the bottom. I will now release you from your burdens and set you free. Goodbye.";
        String[] added = elements.split("\n", -2);
        messages.addAll(List.of(added));
        JList messagesList = new JList(messages.toArray());
        messagesList.setLayoutOrientation(JList.VERTICAL);
        scrollMessage.setPreferredSize(new Dimension(535, 500));

        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String selectedMessage = (String) messagesList.getSelectedValue();
                System.out.println(selectedMessage);
                String[] part = selectedMessage.split(":", 2);
//                if (part[1].equalsIgnoreCase("Seller")) {
//                    //show options for seller
//                } else if (part[1].equalsIgnoreCase("buyer")) {
//                    //show options for buyers
//                } else { //for the store option
//                    //show options for store
//                }
            }
        };
        messagesList.addMouseListener(mouseListener);
        scrollMessage.add(messagesList);

    }


    public static void Search() {
        menuBar.setVisible(false);
        searchBar = new JMenuBar();
        ImageIcon searchImage = new ImageIcon("search.png");
        Image image1 = searchImage.getImage();
        Image img1 = image1.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(img1);
        JMenuItem SearchIcon = new JMenuItem("Search",
                image);
        JTextField searchText = new JTextField();
        searchBar.add(SearchIcon);
        searchBar.add(searchText);
        ImageIcon clear = new ImageIcon("clear.png");
        Image image2 = clear.getImage();
        Image img2 = image2.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
        ImageIcon clearImage = new ImageIcon(img2);
        JMenuItem clearIcon = new JMenuItem("",
                clearImage);
        searchBar.add(clearIcon);

        ImageIcon back = new ImageIcon("back.png");
        Image backImg = back.getImage();
        Image backScale = backImg.getScaledInstance(30, 20, java.awt.Image.SCALE_SMOOTH);
        ImageIcon backImage = new ImageIcon(backScale);
        JMenuItem backIcon = new JMenuItem("",
                backImage);
        searchBar.add(backIcon);
        frame.setJMenuBar(searchBar);

        backIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBar.setVisible(false);
                menuBar.setVisible(true);
                frame.setJMenuBar(menuBar);
            }
        });

        clearIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchText.setText("");
            }
        });
        SearchIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClearList();
                if (user instanceof FullBuyer) {
                    //List(PublicInformation.findSellerBasedOnLetters(searchText.getText(), (FullBuyer) user));
                    //List("woah\nwoah\nwoah\nwoah\nwoah\nwoah\nwoah\nwoah\nwoah\nwoah\nwoah\nboat");
                    if (storeList((FullBuyer) user) == null) {
                        System.out.println("There are no stores!");
                    } else {
                        //List(Objects.requireNonNull(storeList((FullBuyer) user)));
                    }
                } else {
                    //List(findBuyerBasedOnLetters(searchText.getText(), (FullSeller) user));
                }
            }
        });
    }

    public static void ClearList() {
        items.clear();
    }

    public static void List() {
        // elements needs to be the titles of the conversations of the users
        String elements = "Arthur\nLincoln\nSamson\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nelse";
        String[] added = elements.split("\n", -2);
        items.addAll(List.of(added));
        JList list = new JList(items.toArray());
        list.setLayoutOrientation(JList.VERTICAL);
        scrollPane.setPreferredSize(new Dimension(200, 500));
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String selectedItem = (String) list.getSelectedValue();
                System.out.println(selectedItem);
                String[] part = selectedItem.split(":", 2);
//                if (part[1].equalsIgnoreCase("Seller")) {
//                    //show options for seller
//                } else if (part[1].equalsIgnoreCase("buyer")) {
//                    //show options for buyers
//                } else { //for the store option
//                    //show options for store
//                }
            }
        };
        list.addMouseListener(mouseListener);
        scrollPane.add(list);


    }

    public static void Login() {
        Menu();
        //List("Arthur\nLincoln\nSamson\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nelse");
        JFrame loginFrame = new JFrame("Login");
        JPanel panel = new JPanel();
        loginFrame.setSize(350, 200);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setLayout(null);
        loginFrame.add(panel);

        JLabel username = new JLabel("Username");
        username.setBounds(10, 20, 80, 25);
        panel.add(username);
        userText = new JTextField();
        userText.setBounds(100, 20, 150, 25);
        panel.add(userText);

        JLabel password = new JLabel("Password");
        password.setBounds(10, 50, 150, 25);
        panel.add(password);
        passText = new JPasswordField();
        passText.setBounds(100, 50, 150, 25);
        panel.add(passText);

        login = new JButton("Login");
        login.setBounds(100, 80, 80, 25);
        login.addActionListener(actionListener);
        panel.add(login);

        createAcc = new JButton("Create Account");
        createAcc.setBounds(175, 80, 125, 25);
        panel.add(createAcc);
        loginFrame.setVisible(true);
    }

    //allows users to upload files
    public static void uploadFile() {
        try {
            JFileChooser fileC = new JFileChooser();
            fileC.showSaveDialog(null);
        /*JFrame UFFrame = new JFrame("Upload Your File");
        JPanel UFPanel = new JPanel();
        Container UFContent = UFFrame.getContentPane();
        UFContent.setLayout(new BorderLayout());
        UFFrame.setSize(600, 700);
        UFFrame.setLocationRelativeTo(null);
        UFFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UFPanel.setLayout(null);
        UFFrame.add(UFPanel);
        JButton upload = new JButton("Upload File");
        //upload.setBounds(75,200,100,25);
        UFPanel.add(upload);
        UFContent.add(UFPanel, BorderLayout.SOUTH);
        UFFrame.setVisible(true);*/

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid File!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void run() {
        Login();
    }

    static ActionListener actionListener = new ActionListener() {
        //Change these to use Requests
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == login) {
                PacketAssembler.assemblePacket(Request.LOGIN,userText.getText(),String.valueOf(passText.getPassword()));

               /* if (isValidLogin()) {
                    Menu();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Password or Username", "Try Again", JOptionPane.ERROR_MESSAGE);
                } */
            }
            if (e.getSource() == createAcc) {

            }
        }
    };

    private static boolean isValidLogin() {
        try {
            String username = userText.getText();
            String password = String.valueOf(passText.getPassword());
            user = PublicInformation.login(username, password);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new GUI());
    }
}
