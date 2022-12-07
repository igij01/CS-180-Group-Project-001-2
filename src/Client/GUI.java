package Client;

import Client.ClientCore;
import Client.PacketAssembler;
import Protocol.ProtocolRequestType;
import UserCore.FullBuyer;
import UserCore.FullSeller;
import UserCore.FullUser;
import UserCore.PublicInformation;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static UserCore.PublicInformation.findBuyerBasedOnLetters;
import static UserCore.PublicInformation.storeList;

public class GUI extends JFrame {
    private ArrayList<String> items = new ArrayList<>();
    private JMenuBar menuBar;
    private JMenuBar searchBar;
    private ScrollPane scrollPane = new ScrollPane();
    private JButton login;
    private JButton createAcc;
    private JTextField userText;
    private JPasswordField passText;

    private String[] conversationTitles = {"Loading"};
    private String[] listOfUsernames;
    private String[] listOfStores = {"Loading"};
    private String[] listOfBuyers = {"Loading"};
    private String[] listOfSellers;
    private ClientCore client;
    private String[] userProfile;
    private boolean buyer;
    private boolean noConversation = true;
    private boolean noBuyer = true;
    private boolean noSeller = true;
    private boolean noStore = true;

    public GUI(ClientCore client, String[] listOfUsernames, String[] userProfile, boolean buyer) {
        this.listOfUsernames = listOfUsernames;
        this.client = client;
        this.userProfile = userProfile;
        this.buyer = buyer;
        setTitle("Basically Facebook");
        setSize(750,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        add(scrollPane, BorderLayout.WEST);
        //List(user.printConversationTitles()); a user needs to be created from logging in first
        Menu();
    }

    public void Profile() {
        setSize(500,600);
        setLocationRelativeTo(null);
        menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);

        JPanel panel = new JPanel();
        JLabel email = new JLabel("Email: user.getUser().getUserName()");
        JLabel password = new JLabel("Password: *******");
        panel.add(email);
        panel.add(password);

        setJMenuBar(menuBar);
        setVisible(true);
        menu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                setVisible(false);
                Menu();
            }
            @Override
            public void menuDeselected(MenuEvent e) {}
            @Override
            public void menuCanceled(MenuEvent e) {}
        });

    }
    public void Menu() {
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
        setJMenuBar(menuBar);

        profile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
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

    public void Search() {
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
        setJMenuBar(searchBar);

        clearIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchText.setText("");
            }
        });
        SearchIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                ClearList();
//                if (user instanceof FullBuyer) {
//                    List(PublicInformation.findSellerBasedOnLetters(searchText.getText(), (FullBuyer) user));
//                //List("woah\nwoah\nwoah\nwoah\nwoah\nwoah\nwoah\nwoah\nwoah\nwoah\nwoah\nboat");
//                    if (storeList((FullBuyer) user) == null) {
//                        System.out.println("There are no stores!");
//                    } else {
//                        List(Objects.requireNonNull(storeList((FullBuyer) user)));
//                    }
//                } else {
//                    List(findBuyerBasedOnLetters(searchText.getText(), (FullSeller) user));
//                }
            }
        });
    }
    public void ClearList() {items.clear();}
    public void List(String elements) {
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

    
    //allows users to upload files
    public void uploadFile() {
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

    public ActionListener actionListener = new ActionListener() {
        //Change these to use Requests
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == login) {
                PacketAssembler.assemblePacket(ProtocolRequestType.LOGIN, userText.getText(), String.valueOf(passText.getPassword()));

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
    private boolean isValidLogin() {
        try {
            String username = userText.getText();
            String password = String.valueOf(passText.getPassword());
//            user = PublicInformation.login(username, password);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
