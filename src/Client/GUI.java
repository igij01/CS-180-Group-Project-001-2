package Client;

import Protocol.ErrorPacket;
import Protocol.ProtocolRequestType;
import Protocol.ResponsePacket;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GUI extends JFrame {
    private String hashColor = "#f2f6ff";
    private ArrayList<String> editFormat = new ArrayList<>();
    private String selectedMessage;

    private boolean isNewMessage = false;
    private String[] messages = {"Arthur", "Hello There", "Hello", "HI"};
    private JPanel themesPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();
    private ScrollPane scrollMessage = new ScrollPane();
    private String hashText1 = "#f06969";
    private String hashText2 = "#81ed7e";
    private String hashTextB = "#f2f6ff";
    private String theme = "Christmas Theme";

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
        client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.DISPLAY_CONVERSATION_TITLES));
        client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.REQUEST_PUBLIC_INFO));
        AsyncListener listener = new AsyncListener();
        listener.execute();
        setTitle("Basically Facebook");
        setSize(750,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        add(scrollPane, BorderLayout.WEST);
        add(buttonPanel, BorderLayout.NORTH);
        menuBar = new JMenuBar();
        Menu();
        list();
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
        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);
        JMenuItem space = new JMenuItem("");
        ImageIcon imageIcon = new ImageIcon("profile.png");
        Image image = imageIcon.getImage();
        Image img = image.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(img);
        JMenuItem profile = new JMenuItem("profile",
                imageIcon);
        profile.setBackground(Color.decode(hashColor));
        menuBar.add(profile);

        ImageIcon imageIcon1 = new ImageIcon("search.png");
        Image image1 = imageIcon1.getImage();
        Image img1 = image1.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
        imageIcon1 = new ImageIcon(img1);
        JMenuItem search = new JMenuItem("search",
                imageIcon1);
        search.setBackground(Color.decode(hashColor));
        menuBar.add(search);

        ImageIcon imageIcon2 = new ImageIcon("logout.png");
        Image image2 = imageIcon2.getImage();
        Image img2 = image2.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
        imageIcon2 = new ImageIcon(img2);
        JMenuItem logout = new JMenuItem("logout",
                imageIcon2);
        logout.setBackground(Color.decode(hashColor));
        menuBar.add(logout);
        space.setBackground(Color.decode(hashColor));
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
    //public void clearList() {items.clear();}
    public void list() {
        if (!noConversation)
            Messages(conversationTitles[0]);
        JList list = new JList(conversationTitles);
        list.setLayoutOrientation(JList.VERTICAL);
        scrollPane.setBackground(Color.decode(hashColor));
        list.setBackground(Color.decode(hashColor));
        scrollPane.setPreferredSize(new Dimension(200, 750));
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String selectedItem = (String) list.getSelectedValue();
                client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.DISPLAY_CONVERSATION,
                        selectedItem));
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
        if (noConversation) {
            messages[1] = "You have no Messages!";
        }
        if (!username.equals(messages[0])) {
            return;
        }
        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.decode(hashColor));
        menuBar.setBackground(Color.decode(hashColor));
        searchBar.setBackground(Color.decode(hashColor));

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setPreferredSize(new Dimension(200, 25));
        JLabel name = new JLabel(username);
        JLabel currentTheme = new JLabel(theme + "    ");
        currentTheme.setBackground(Color.decode(hashColor));
        JButton newMessage = new JButton("New Message");
        JButton editMessage = new JButton("Edit Message");
        JButton deleteMessage = new JButton("Delete Message");
        JButton themes = new JButton("Themes");
        JSeparator separator = new JSeparator();
        buttonPanel.add(name);
        buttonPanel.add(separator);
        buttonPanel.add(currentTheme);
        buttonPanel.add(newMessage);
        buttonPanel.add(editMessage);
        buttonPanel.add(deleteMessage);
        buttonPanel.add(themes);
        for (int i = 1; i < messages.length; i++) {
            if (i%2 == 0) {
                editFormat.add(messages[i].substring(messages[i].indexOf(": ")));
                messages[i] = "<html><FONT style=\"BACKGROUND-COLOR: " + hashText1 + "\">" + messages[i] + "</FONT></html>";
            } else {
                editFormat.add(messages[i].substring(messages[i].indexOf(": ")));
                messages[i] = "<html><FONT style=\"BACKGROUND-COLOR: " + hashText2 + "\">" + messages[i] + "</FONT></html>";
            }
        }
        JList messagesList = new JList(messages);
        messagesList.setBackground(Color.decode(hashTextB));
        messagesList.setLayoutOrientation(JList.VERTICAL);
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                selectedMessage = (String) messagesList.getSelectedValue();
                for (int i = 1; i < messages.length; i++) {
                    if (messages[i].equals(selectedMessage)) {
                        selectedMessage = editFormat.get(i);
                        break;
                    }
                }
                System.out.println(selectedMessage);
            }
        };
        messagesList.addMouseListener(mouseListener);
        scrollMessage.setPreferredSize(new Dimension(700, 400));
        scrollMessage.add(messagesList);
        add(scrollMessage, BorderLayout.EAST);
        themes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                themes(username);
            }
        });
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
    public void themes(String username) {
        buttonPanel.setVisible(false);
        themesPanel = new JPanel();
        JButton christmas = new JButton("Christmas");
        JButton plants = new JButton("Nature");
        JButton ocean = new JButton("Ocean");
        JButton reef = new JButton("Coastal Reef");
        JButton blood = new JButton("Blood");
        JButton lavender = new JButton("Lavender Haze");
        JButton sunshine = new JButton("Sunshine");
        themesPanel.add(christmas);
        christmas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hashText1 = "#f06969";
                hashText2 = "#81ed7e";
                hashTextB = "#f2f6ff";
                theme = "Christmas Theme";
                themesPanel.setVisible(false);
                Messages(username);
            }
        });
        themesPanel.add(plants);
        plants.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hashText1 = "#78e856";
                hashText2 = "#528045";
                hashTextB = "#abf794";
                theme = "Nature Theme";
                themesPanel.setVisible(false);
                Messages(username);
            }
        });
        themesPanel.add(ocean);
        ocean.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hashText1 = "#6f81f2";
                hashText2 = "#426bc9";
                hashTextB = "#a3b6e3";
                theme = "Ocean Theme";
                themesPanel.setVisible(false);
                Messages(username);
            }
        });
        themesPanel.add(reef);
        reef.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hashText1 = "#9fd2fc";
                hashText2 = "#53f5e5";
                hashTextB = "#dcfcfa";
                theme = "Coastal Reef Theme";
                themesPanel.setVisible(false);
                Messages(username);
            }
        });
        themesPanel.add(blood);
        blood.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hashText1 = "#91272e";
                hashText2 = "#e34d57";
                hashTextB = "#1f0204";
                theme = "Blood Theme";
                themesPanel.setVisible(false);
                Messages(username);
            }
        });
        themesPanel.add(lavender);
        lavender.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hashText1 = "#ee82fa";
                hashText2 = "#ae67b5";
                hashTextB = "#f8d4fc";
                theme = "Lavender Haze Theme";
                themesPanel.setVisible(false);
                Messages(username);
            }
        });
        themesPanel.add(sunshine);
        sunshine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hashText1 = "#fafc5b";
                hashText2 = "#edd445";
                hashTextB = "#f9fab4";
                theme = "Sunshine Theme";
                themesPanel.setVisible(false);
                Messages(username);
            }
        });
        add(themesPanel, BorderLayout.NORTH);
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

    private class AsyncListener extends SwingWorker<Object, Void> {
        @Override
        protected Object doInBackground() throws Exception {
            while (!isCancelled()){
                Object packet = client.popFromQueue();
                if (packet != null)
                    return packet;
            }
            return null;
        }

        @Override
        protected void done() {
            Object packet = null;
            try {
                packet = get();
            } catch (InterruptedException e) {}
            catch (ExecutionException e) {
                String why = null;
                Throwable cause = e.getCause();
                if (cause != null) {
                    why = cause.getMessage();
                } else {
                    why = e.getMessage();
                }
                System.err.println("Error retrieving file: " + why);
                return;
            }
            if (packet instanceof ResponsePacket) {
                System.out.println(packet);
                ResponsePacket responsePacket = (ResponsePacket) packet;
                switch (responsePacket.protocolResponseType) {
                    case CONVERSATION_TITLES:
                        conversationTitles = responsePacket.args;
                        if (conversationTitles[0].equalsIgnoreCase("You have no conversation!"))
                            noConversation = true;
                        else
                            noConversation = false;
                        System.out.println(conversationTitles.length);
                        list();
                        break;
                    case PUBLIC_INFO:
                        break;
                    case CONVERSATION:
                        messages = responsePacket.args;


                }
            } else if (packet instanceof ErrorPacket) {

            }
            AsyncListener asyncListener = new AsyncListener();
            asyncListener.execute();
        }
    }
}
