package GUI;

import UserCore.*;
import com.sun.source.tree.NewArrayTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static UserCore.PublicInformation.findBuyerBasedOnLetters;
import static UserCore.PublicInformation.storeList;

public class GUI extends Thread {
    static JFrame frame;
    static ArrayList<String> items = new ArrayList<>();
    static JMenuBar menuBar;
    static JMenuBar searchBar;
    static ScrollPane scrollPane = new ScrollPane();
    static JButton login;
    static JButton createAcc;
    static JTextField userText;
    static JPasswordField passText;
    static FullUser user;

    public static void Setup() {
        frame = new JFrame("Basically Facebook");
        frame.setSize(750,500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(scrollPane, BorderLayout.WEST);
        //List(user.printConversationTitles()); a user needs to be created from logging in first
    }

    public static void Profile() {

    }
    public static void Menu() {
        Setup();
        menuBar = new JMenuBar();
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
        JMenuItem search = new JMenuItem("search",
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

    public static void Search() {
        menuBar.setVisible(false);
        searchBar = new JMenuBar();
        ImageIcon searchImage = new ImageIcon("search.png");
        Image image1 = searchImage.getImage();
        Image img1 = image1.getScaledInstance(15, 15,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(img1);
        JMenuItem SearchIcon = new JMenuItem("Search",
                image);
        JTextField searchText = new JTextField();
        searchBar.add(SearchIcon);
        searchBar.add(searchText);
        ImageIcon clear = new ImageIcon("clear.png");
        Image image2 = clear.getImage();
        Image img2 = image2.getScaledInstance(15, 15,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon clearImage = new ImageIcon(img2);
        JMenuItem clearIcon = new JMenuItem("",
                clearImage);
        searchBar.add(clearIcon);
        frame.setJMenuBar(searchBar);

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
                    List(PublicInformation.findSellerBasedOnLetters(searchText.getText(), (FullBuyer) user));
                //List("woah\nwoah\nwoah\nwoah\nwoah\nwoah\nwoah\nwoah\nwoah\nwoah\nwoah\nboat");
                    if (storeList((FullBuyer) user) == null) {
                        System.out.println("There are no stores!");
                    } else {
                        List(Objects.requireNonNull(storeList((FullBuyer) user)));
                    }
                } else {
                    List(findBuyerBasedOnLetters(searchText.getText(), (FullSeller) user));
                }
            }
        });
    }
    public static void ClearList() {items.clear();}
    public static void List(String elements) {
        if (elements == null) {
            elements = "Nothing here to see";
        }
        String[] added = elements.split("\n", -2);
        items.addAll(List.of(added));
        System.out.println(items);
        System.out.println(Arrays.asList(items));
        JList list = new JList(items.toArray());
        System.out.println(list);
        list.setLayoutOrientation(JList.VERTICAL);
        scrollPane.setPreferredSize(new Dimension(200,500));
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String selectedItem = (String) list.getSelectedValue();
                System.out.println(selectedItem);
                String[] part = selectedItem.split(":",2);
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
        List("Arthur\nLincoln\nSamson\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nelse");
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
    public void run() {
        Login();
    }

    static ActionListener actionListener = new ActionListener() {
        //Change these to use Requests
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
