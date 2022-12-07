package Client;

import Protocol.ProtocolRequestType;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GUI extends JFrame {
    private ArrayList<String> items = new ArrayList<>();
    private String hashColor = "#f2f6ff";
    private String selectedMessage;

    private boolean isNewMessage = false;
    private int selectedIndex;
    private ArrayList<String> messages = new ArrayList<>();
    private JPanel buttonPanel = new JPanel();
    private ScrollPane scrollMessage = new ScrollPane();

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
    public void clearList() {items.clear();}
    public void list(String elements) {
        if (elements == null) {
            elements = "Nothing to see here";
        }
        String[] added = elements.split("\n", -2);
        items.addAll(List.of(added));
        Messages(items.get(0));
        JList list = new JList(items.toArray());
        list.setLayoutOrientation(JList.VERTICAL);
        scrollPane.setBackground(Color.decode(hashColor));
        list.setBackground(Color.decode(hashColor));
        scrollPane.setPreferredSize(new Dimension(200, 750));
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String selectedItem = (String) list.getSelectedValue();
                Messages(selectedItem);
                if (isNewMessage) {
                    NewMessage(selectedItem);
                }
                System.out.println(selectedItem);
                String[] part = selectedItem.split(":", 2);
            }
        };
        list.addMouseListener(mouseListener);
        scrollPane.add(list);
    }

    public void Messages(String username) {
        buttonPanel.setVisible(false);
        String elements = "String of Conversations taken from the selected user.";
        String[] added = elements.split("\n", -2);
        //messages.addAll(List.of(added));
        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.decode(hashColor));
        menuBar.setBackground(Color.decode(hashColor));
        searchBar.setBackground(Color.decode(hashColor));

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setPreferredSize(new Dimension(100, 25));
        JLabel name = new JLabel(username);
        JButton newMessage = new JButton("New Message");
        JButton editMessage = new JButton("Edit Message");
        JButton deleteMessage = new JButton("Delete Message");
        JSeparator separator = new JSeparator();
        buttonPanel.add(name);
        buttonPanel.add(separator);
        buttonPanel.add(newMessage);
        buttonPanel.add(editMessage);
        buttonPanel.add(deleteMessage);
        add(buttonPanel, BorderLayout.NORTH);
        // I will change this to match the user instead of every other one once I get the input formatting right.
        for (int i = 0; i < added.length; i++) {
            if (i%2 == 0) {
                messages.add("<html><FONT style=\"BACKGROUND-COLOR: #f06969\">" + added[i] + "</FONT></html>");
            } else {
                messages.add("<html><FONT style=\"BACKGROUND-COLOR: #81ed7e\">" + added[i] + "</FONT></html>");
            }
        }
        JList messagesList = new JList(messages.toArray());
        messagesList.setBackground(Color.decode(hashColor));

        messagesList.setLayoutOrientation(JList.VERTICAL);
        scrollMessage.setPreferredSize(new Dimension(535, 400));
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                selectedMessage = (String) messagesList.getSelectedValue();
                for (int i = 0; i < messages.size(); i++) {
                    if (messages.get(i).equals(selectedMessage)) {
                        selectedIndex = i;
                        selectedMessage = added[i];
                        break;
                    }
                }
                System.out.println(selectedMessage);
            }
        };
        messagesList.addMouseListener(mouseListener);


        scrollMessage.add(messagesList);

        newMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewMessage(username);
            }
        });
        editMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedMessage != null) {
                    EditMessage(username, selectedMessage);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select the message you want to edit.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        deleteMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedMessage != null) {
                    //PacketAssembler.assemblePacket(ProtocolRequestType.DELETE_MESSAGE,)
                    //if this traces to null does that mean its not made yet? and can I use the message directly?
                    Messages(username);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select the message you want to delete.", "Error", JOptionPane.ERROR_MESSAGE);

                }

            }
        });
    }
    public void EditMessage(String username, String message) {
        menuBar.setVisible(false);
        buttonPanel.setVisible(false);
        JPanel textPanel = new JPanel();
        textPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));
        textPanel.setPreferredSize(new Dimension(100,50));
        JTextArea textArea = new JTextArea(message);
        JScrollPane textPane = new JScrollPane(textArea);
        textPane.setPreferredSize(new Dimension(600,40));
        JButton finalize = new JButton("Finalize Edited Message");
        ImageIcon back = new ImageIcon("back.png");
        Image backImg = back.getImage();
        Image backScale = backImg.getScaledInstance(30, 20, java.awt.Image.SCALE_SMOOTH);
        ImageIcon backImage = new ImageIcon(backScale);
        JMenuItem backIcon = new JMenuItem("",
                backImage);
        ImageIcon clear = new ImageIcon("clear.png");
        Image image2 = clear.getImage();
        Image img2 = image2.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
        ImageIcon clearImage = new ImageIcon(img2);
        JMenuItem clearIcon = new JMenuItem("",
                clearImage);
        textPanel.add(finalize);
        textPanel.add(textPane);
        textPanel.add(clearIcon);
        textPanel.add(backIcon);
        add(textPanel, BorderLayout.NORTH);

        finalize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //  PacketAssembler.assemblePacket(ProtocolRequestType.EDIT_MESSAGE, )
            }
        });
        clearIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
            }
        });
        backIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuBar.setVisible(true);
                remove(textPanel);
                Messages(username);
            }
        });






    }

    public void NewMessage(String username) {
        isNewMessage = true;
        buttonPanel.setVisible(false);
        menuBar.setVisible(false);
        JPanel textPanel = new JPanel();
        textPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));
        textPanel.setPreferredSize(new Dimension(100, 50));
        JTextArea textArea = new JTextArea();
        JScrollPane textPane = new JScrollPane(textArea);
        textPane.setPreferredSize(new Dimension(600, 40));
        JButton name = new JButton("Send to " + username + "  ");
        ImageIcon back = new ImageIcon("back.png");
        Image backImg = back.getImage();
        Image backScale = backImg.getScaledInstance(30, 20, java.awt.Image.SCALE_SMOOTH);
        ImageIcon backImage = new ImageIcon(backScale);
        JMenuItem backIcon = new JMenuItem("",
                backImage);
        ImageIcon clear = new ImageIcon("clear.png");
        Image image2 = clear.getImage();
        Image img2 = image2.getScaledInstance(15, 15,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon clearImage = new ImageIcon(img2);
        JMenuItem clearIcon = new JMenuItem("",
                clearImage);
        textPanel.add(name);
        textPanel.add(textPane);
        textPanel.add(clearIcon);
        textPanel.add(backIcon);
        add(textPanel, BorderLayout.NORTH);

//        name.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if () // needs to find out if the selected user is a buyer seller or store. Still need to understand formate the list is coming in from.
//                    // this will be used when the selected user type is determined
//                    PacketAssembler.assemblePacket(ProtocolRequestType.SEND_MESSAGE_BUYER, userText.getText(), String.valueOf(passText.getPassword()));
//            }
//        });

        clearIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
            }
        });
        backIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuBar.setVisible(true);
                remove(textPanel);
                Messages(username);
                isNewMessage = false;
            }
        });


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
