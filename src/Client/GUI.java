package Client;

import Protocol.ErrorPacket;
import Protocol.ProtocolRequestType;
import Protocol.ResponsePacket;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class GUI extends JFrame {
    private String hashColor = "#f2f6ff";
    private String selectedMessage;
    private int selectedIndex;
    private boolean themeUpdate;
    private boolean isNewMessage = false;
    private String[] messages = {"loading"};
    private String currentSelectedMessage = null;
    private JPanel themesPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();
    private JSplitPane splitPane = new JSplitPane();
    private ScrollPane scrollMessage = new ScrollPane();
    private String hashText1 = "#f06969";
    private String hashText2 = "#81ed7e";
    private String hashTextB = "#f2f6ff";
    private String theme = "Christmas Theme";

    private JMenuBar menuBar = new JMenuBar();
    private JMenuBar searchBar = new JMenuBar();
    private JComboBox<String> searchUser = new JComboBox<>();
    private JList<String> listUser = new JList<>();
    private ScrollPane scrollPane = new ScrollPane();
    private JButton login;
    private JButton createAcc;
    private JTextField userText;
    private JPasswordField passText;
    private JLabel name = new JLabel(this.currentSelectedMessage);
    private JLabel currentTheme = new JLabel(theme + "    ");

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
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(splitPane);
        splitPane.setLeftComponent(scrollPane);
        splitPane.setRightComponent(scrollMessage);
        buttonPanel();
        add(buttonPanel, BorderLayout.NORTH);
        Menu();
        displayList();
        Search();
        setVisible(true);
        client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.DISPLAY_CONVERSATION_TITLES));
        client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.REQUEST_PUBLIC_INFO));
        AsyncListener listener = new AsyncListener();
        listener.execute();
    }

    public void Profile() {
        setSize(500, 600);
        setLocationRelativeTo(null);
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
            public void menuDeselected(MenuEvent e) {
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
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
                displaySearch();
            }
        });

        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.LOGOUT));
            }
        });
    }

    public void displaySearch() {
        scrollMessage.setVisible(false);
        buttonPanel.setVisible(false);
        menuBar.setVisible(false);
        setJMenuBar(searchBar);
        searchBar.setVisible(true);
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(buyer ? listOfSellers : listOfBuyers);
        searchUser.setModel(model);
        setDisplayListToUsername((buyer ? listOfStores : listOfBuyers));
    }

    public void Search() {
        ImageIcon searchImage = new ImageIcon("search.png");
        Image image1 = searchImage.getImage();
        Image img1 = image1.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(img1);
        JMenuItem SearchIcon = new JMenuItem("Search",
                image);
        searchUser.setEditor(new BasicComboBoxEditor());
        searchUser.setEditable(true);
        searchBar.add(SearchIcon);
        searchBar.add(searchUser);
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
        JButton buttonSeller = new JButton("Create New Message to seller");
        JButton buttonStore = new JButton("Create New Message to store");
        JButton buttonBuyerList = new JButton("Create New Message to buyer from list");
        JButton buttonBuyerSearch = new JButton("Create New Message to buyer from search");
        if (buyer) {
            searchBar.add(buttonSeller);
            searchBar.add(buttonStore);
        } else {
            searchBar.add(buttonBuyerList);
            searchBar.add(buttonBuyerSearch);
        }

        backIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeSearchBar();
            }
        });

        clearIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((JTextField) searchUser.getEditor().getEditorComponent()).setText("");
            }
        });
        SearchIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (buyer) {
                    ArrayList<String> userFound = new ArrayList<>();
                    for (String seller : listOfSellers) {
                        if (seller.contains(((JTextField) searchUser.getEditor().getEditorComponent()).getText())) {
                            userFound.add(seller);
                        }
                    }
                    if (userFound.isEmpty())
                        userFound.add("No Seller found");
                    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(userFound.toArray(new String[0]));
                    searchUser.setModel(model);
                } else {
                    ArrayList<String> userFound = new ArrayList<>();
                    for (String buyer : listOfBuyers) {
                        if (buyer.contains(((JTextField) searchUser.getEditor().getEditorComponent()).getText())) {
                            userFound.add(buyer);
                        }
                    }
                    if (userFound.isEmpty())
                        userFound.add("No Buyer found");
                    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(userFound.toArray(new String[0]));
                    searchUser.setModel(model);
                }
            }
        });
        buttonSeller.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (searchUser.getSelectedItem() == null ||
                        searchUser.getSelectedItem().equals("There are no sellers!") ||
                        searchUser.getSelectedItem().equals("No Seller found"))
                    JOptionPane.showMessageDialog(null, "invalid seller to message",
                            "Error", JOptionPane.ERROR_MESSAGE);
                else
                    searchCreateNewMessage((String) searchUser.getSelectedItem(), false);
            }
        });

        buttonStore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listUser.getSelectedValue() == null ||
                        listUser.getSelectedValue().equals("There are no stores!"))
                    JOptionPane.showMessageDialog(null, "invalid store to message",
                            "Error", JOptionPane.ERROR_MESSAGE);
                else
                    searchCreateNewMessage((String) searchUser.getSelectedItem(), true);
            }
        });

        buttonBuyerList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listUser.getSelectedValue() == null ||
                        listUser.getSelectedValue().equals("There are no buyers!"))
                    JOptionPane.showMessageDialog(null, "invalid buyer to message",
                            "Error", JOptionPane.ERROR_MESSAGE);
                else
                    searchCreateNewMessage((String) searchUser.getSelectedItem(), false);
            }
        });

        buttonBuyerSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (searchUser.getSelectedItem() == null ||
                        searchUser.getSelectedItem().equals("There are no buyers!") ||
                        searchUser.getSelectedItem().equals("No Buyer found"))
                    JOptionPane.showMessageDialog(null, "invalid buyer to message",
                            "Error", JOptionPane.ERROR_MESSAGE);
                else
                    searchCreateNewMessage((String) searchUser.getSelectedItem(), false);
            }
        });
    }

    public void searchCreateNewMessage(String selectedString, boolean isStore) {
        JPanel textPanel = new JPanel();
        textPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));
        textPanel.setPreferredSize(new Dimension(100, 50));
        JTextArea textArea = new JTextArea();
        JScrollPane textPane = new JScrollPane(textArea);
        textPane.setPreferredSize(new Dimension(600, 40));
        JButton name = new JButton("Send to " + selectedString + "  ");
        ImageIcon clear = new ImageIcon("clear.png");
        Image image2 = clear.getImage();
        Image img2 = image2.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
        ImageIcon clearImage = new ImageIcon(img2);
        JMenuItem clearIcon = new JMenuItem("",
                clearImage);
        ImageIcon upload = new ImageIcon("upload.png");
        Image uploadImage = upload.getImage();
        Image uploadImg = uploadImage.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
        ImageIcon uploadImageScale = new ImageIcon(uploadImg);
        JMenuItem uploadIcon = new JMenuItem("",
                uploadImageScale);
        textPanel.add(name);
        textPanel.add(textPane);
        textPanel.add(uploadIcon);
        textPanel.add(clearIcon);
        splitPane.setRightComponent(textPanel);

        name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.addByteBufferToWrite(PacketAssembler.assemblePacket(
                        (isStore ? ProtocolRequestType.SEND_MESSAGE_STORE : ProtocolRequestType.SEND_MESSAGE_USER),
                        selectedString, textArea.getText()));
                closeSearchBar();
            }
        });
        uploadIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText(uploadFile());
            }
        });
        clearIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
            }
        });
    }

    private void closeSearchBar() {
        searchBar.setVisible(false);
        menuBar.setVisible(true);
        setJMenuBar(menuBar);
        buttonPanel.setVisible(true);
        add(buttonPanel, BorderLayout.NORTH);
        scrollPane.removeAll();
        scrollMessage.setVisible(true);
        splitPane.setRightComponent(scrollMessage);
        displayList();
    }

    public void setDisplayListToUsername(String[] usernames) {
        scrollPane.removeAll();
        listUser = new JList<>(usernames);
        listUser.setLayoutOrientation(JList.VERTICAL);
        scrollPane.add(listUser);
    }

    public void displayList() {
        if (!noConversation && currentSelectedMessage == null) {
            client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.DISPLAY_CONVERSATION,
                    conversationTitles[0]));
            currentSelectedMessage = conversationTitles[0].replace("\n", "");
        }
        String[] colorManipulate = Arrays.copyOf(conversationTitles, conversationTitles.length);
        for (int i = 0; i < colorManipulate.length; i++) {
            if (colorManipulate[i].contains("\n")) {
                conversationTitles[i] = conversationTitles[i].replace("\n", "");
                colorManipulate[i] = "<html><FONT style=\"BACKGROUND-COLOR: #bccce3\">" + colorManipulate[i] + "</FONT></html>";
            }
        }
        JList list = new JList(colorManipulate);
        list.setLayoutOrientation(JList.VERTICAL);
        scrollPane.setBackground(Color.decode(hashColor));
        list.setBackground(Color.decode(hashColor));
        scrollPane.setPreferredSize(new Dimension(200, 500));
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String selectedItem = conversationTitles[list.getSelectedIndex()];
                if (!currentSelectedMessage.equals(selectedItem) || messages[0].equals("loading")) {
                    currentSelectedMessage = selectedItem.replace("\n", "");
                    client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.DISPLAY_CONVERSATION,
                            selectedItem));
                }
                if (isNewMessage) {
                    NewMessage();
                }
            }
        };
        list.addMouseListener(mouseListener);
        scrollPane.add(list);
    }

    public void buttonPanel() {
        buttonPanel.setBackground(Color.decode(hashColor));
        menuBar.setBackground(Color.decode(hashColor));
        searchBar.setBackground(Color.decode(hashColor));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setPreferredSize(new Dimension(100, 25));
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
        themes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                themes();
            }
        });
        newMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewMessage();
            }
        });
        editMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedMessage != null) {
                    EditMessage(selectedMessage);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select the message you want " +
                            "to edit.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        deleteMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedMessage != null) {
                    client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.DELETE_MESSAGE,
                            currentSelectedMessage, String.valueOf(selectedIndex)));
                } else {
                    JOptionPane.showMessageDialog(null, "Please select the message you want " +
                            "to delete.", "Error", JOptionPane.ERROR_MESSAGE);

                }

            }
        });
    }

    public void Messages(String[] messageFromServer) {
        menuBar.setVisible(true);
        name.setText(this.currentSelectedMessage);
        currentTheme.setText(theme + "    ");
        currentTheme.setBackground(Color.decode(hashColor));
        buttonPanel.setVisible(true);
        add(buttonPanel, BorderLayout.NORTH);
        if (messageFromServer != null) {
            if (!messageFromServer[0].equals(this.currentSelectedMessage)) {
                return;
            }
            this.messages = messageFromServer;
            this.messages = Arrays.copyOfRange(this.messages, 1, this.messages.length);
        }
        if (noConversation) {
            messages[1] = "You have no Messages!";
        }
        String[] manipulateColor = Arrays.copyOf(messages, messages.length);
        ArrayList<String> editFormat = new ArrayList<>();
        for (int i = 0; i < messages.length; i += 2) {
            if (i % 4 == 0) {
                editFormat.add(messages[i].substring(messages[i].indexOf(": ") + 2));
                manipulateColor[i] = "<html><FONT style=\"BACKGROUND-COLOR: " + hashText1 + "\">" + manipulateColor[i] + "</FONT></html>";
                manipulateColor[i + 1] = "<html><FONT style=\"BACKGROUND-COLOR: " + hashText1 + "\">" + manipulateColor[i + 1] + "</FONT></html>";
            } else {
                editFormat.add(messages[i].substring(messages[i].indexOf(": ") + 2));
                manipulateColor[i] = "<html><FONT style=\"BACKGROUND-COLOR: " + hashText2 + "\">" + manipulateColor[i] + "</FONT></html>";
                manipulateColor[i + 1] = "<html><FONT style=\"BACKGROUND-COLOR: " + hashText2 + "\">" + manipulateColor[i + 1] + "</FONT></html>";
            }
        }
        themeUpdate = false;
        JList<String> messagesList = new JList<>(manipulateColor);
        messagesList.setBackground(Color.decode(hashTextB));
        messagesList.setLayoutOrientation(JList.VERTICAL);
        scrollMessage.setPreferredSize(new Dimension(700, 400));
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int index = messagesList.getSelectedIndex();
                selectedMessage = editFormat.get(index / 2);
                selectedIndex = index / 2;
            }
        };
        messagesList.addMouseListener(mouseListener);
        scrollMessage.add(messagesList);
        pack();
    }

    public void EditMessage(String message) {
        menuBar.setVisible(false);
        buttonPanel.setVisible(false);
        JPanel textPanel = new JPanel();
        textPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));
        textPanel.setPreferredSize(new Dimension(100, 50));
        JTextArea textArea = new JTextArea(message);
        JScrollPane textPane = new JScrollPane(textArea);
        textPane.setPreferredSize(new Dimension(600, 40));
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
                client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.EDIT_MESSAGE,
                        currentSelectedMessage, Integer.toString(selectedIndex), textArea.getText()));
                remove(textPanel);
                Messages(null);
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
                remove(textPanel);
                Messages(null);
            }
        });
    }

    public void themes() {
        buttonPanel.setVisible(false);
        themeUpdate = true;
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
                Messages(null);
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
                Messages(null);
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
                Messages(null);
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
                Messages(null);
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
                Messages(null);
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
                Messages(null);
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
                Messages(null);
            }
        });
        add(themesPanel, BorderLayout.NORTH);
    }

    public void NewMessage() {
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
        JButton name = new JButton("Send to " + this.currentSelectedMessage + "  ");
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
        ImageIcon upload = new ImageIcon("upload.png");
        Image uploadImage = upload.getImage();
        Image uploadImg = uploadImage.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
        ImageIcon uploadImageScale = new ImageIcon(uploadImg);
        JMenuItem uploadIcon = new JMenuItem("",
                uploadImageScale);
        textPanel.add(name);
        textPanel.add(textPane);
        textPanel.add(uploadIcon);
        textPanel.add(clearIcon);
        textPanel.add(backIcon);
        add(textPanel, BorderLayout.NORTH);

        name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.SEND_MESSAGE_USER, currentSelectedMessage,
                        textArea.getText()));
                menuBar.setVisible(true);
                buttonPanel.setVisible(true);
                remove(textPanel);
                isNewMessage = false;
                Messages(null);
            }
        });
        uploadIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText(uploadFile());
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
                buttonPanel.setVisible(true);
                remove(textPanel);
                isNewMessage = false;
                Messages(null);
            }
        });


    }

    //allows users to upload files
    public String uploadFile() {
        try {
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(filter);
            fc.setAcceptAllFileFilterUsed(false);
            int answer = fc.showOpenDialog(null); // (JFrame.this) when in JFrame
            if (answer == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                byte[] content = Files.readAllBytes(file.toPath());
                String text = new String(content); // Using default encoding
                return text;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Invalid File!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
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
            while (!isCancelled()) {
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
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
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
                        displayList();
                        break;
                    case PUBLIC_INFO:
                        assert buyer ^ responsePacket.args.length != 2;
                        if (responsePacket.args.length == 2) {
                            listOfStores = (responsePacket.args[0].replace("[", "")
                                    .replace("]", "").split(", "));
                            listOfSellers = (responsePacket.args[1].replace("[", "")
                                    .replace("]", "").split(", "));
                            System.out.println(Arrays.toString(listOfStores));
                            System.out.println(Arrays.toString(listOfSellers));
                        } else
                            listOfBuyers = (responsePacket.args[0].replace("[", "")
                                    .replace("]", "").split(", "));
                        break;
                    case CONVERSATION:
                        Messages(((ResponsePacket) packet).args);
                        break;
                    case LOGOUT_SUCCESS:
                        JOptionPane.showMessageDialog(null, responsePacket.args[0], "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        //TODO someone fix this please
                        System.exit(0);
                        break;
                    case ACCOUNT_DELETION:
                        int selection = JOptionPane.showConfirmDialog(null, responsePacket.args[0], "Warning", JOptionPane.YES_NO_OPTION);
                        if (selection == JOptionPane.YES_OPTION) {
                            PacketAssembler.assemblePacket(ProtocolRequestType.FORCE_LOGOUT);
                        }
                        break;


                }
            } else if (packet instanceof ErrorPacket) {

            }
            AsyncListener asyncListener = new AsyncListener();
            asyncListener.execute();
        }
    }
}
