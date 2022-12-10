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
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GUI extends JFrame {
    private String hashColor = "#f2f6ff";
    private String selectedMessage;
    private int selectedIndex;
    private boolean themeUpdate;
    private boolean isNewMessage = false;
    private String[] messages = {"loading"};
    private String currentSelectedMessage = null;
    private Profile profileFrame;
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

    private File exportPath = null;

    private String[] conversationTitles = {"Loading"};
    private String[] listOfUsernames;
    private String[] listOfStores = {"Loading"};
    private String[] listOfBuyers = {"Loading"};
    private String[] listOfSellers = {"loading"};
    private String dashboard = "loading";
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
        profileFrame = new Profile();
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
        client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.REQUEST_DASHBOARD, "true"));
        AsyncListener listener = new AsyncListener();
        listener.execute();
    }

    private class Profile extends JFrame {
        private JMenuBar profileMenuBar;
        private JMenu menu;
        private JPanel panelProfile;
        private JPanel panelBlockUser;
        private JPanel panelInvisUser;
        private JPanel panelCensorWords;
        private JPanel panelStore;
        private JPanel changeProfile;
        private JPanel dashBoard;
        private JTextField usernameField;
        private JTextField emailField;
        private JTextField roleTextField;
        private JLabel accountToBeDeleted;
        private JComboBox<String> censorList;
        private JTextField pattern;
        private JLabel censorModeLabel;
        private JComboBox<String> blockList;
        private JComboBox<String> availableUser;
        private JComboBox<String> invisList;
        private JList<String> listOfStores;
        private JButton deleteRecoverButton;
        private JTextArea textPane;
        private JCheckBox increasing;

        private String username;
        private String email;
        private String role;
        private boolean toBeDeleted;
        private String[] blockedUsers;
        private String[] invisUsers;
        private String censorMode;
        private String[] filterWords;
        private String censorPattern;
        private String[] stores;


        private void processServerResponse() {
            //processing the output from server
            String[] userInfo = userProfile[0].split("\n");
            username = userInfo[0];
            email = userInfo[1];
            role = userInfo[2];
            toBeDeleted = userInfo.length == 4;
            blockedUsers = userProfile[1].split("\n");
            invisUsers = userProfile[2].split("\n");
            censorMode = userProfile[3];
            filterWords = userProfile[4].replace("[", "").replace("]", "").split(", ");
            censorPattern = userProfile[5];
            stores = null;
            if (!buyer) {
                stores = userProfile[6].split("\n");
            }
        }

        public Profile() {
            setTitle("Profile");
            setSize(1200, 400);
            setLocationRelativeTo(null);
            Container contentPane = getContentPane();
            contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
            profileMenuBar = new JMenuBar();
            menu = new JMenu("Menu");
            profileMenuBar.add(menu);
            setJMenuBar(profileMenuBar);
            processServerResponse();

            panelProfile = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panelProfile.setName("Profile");
            contentPane.add(panelProfile);
            JLabel usernameLabel = new JLabel("Username:");
            panelProfile.add(usernameLabel);
            usernameField = new JTextField(username, username.length());
            usernameField.setEditable(false);
            panelProfile.add(usernameField);
            JLabel emailLabel = new JLabel("Email:");
            panelProfile.add(emailLabel);
            emailField = new JTextField(email, email.length());
            emailField.setEditable(false);
            panelProfile.add(emailField);
            JLabel roleLabel = new JLabel("Role:");
            panelProfile.add(roleLabel);
            roleTextField = new JTextField(role, role.length());
            roleTextField.setEditable(false);
            panelProfile.add(roleTextField);
            accountToBeDeleted = new JLabel();
            accountToBeDeleted.setForeground(new Color(224, 27, 36));
            panelProfile.add(accountToBeDeleted);
            if (toBeDeleted)
                accountToBeDeleted.setText("ACCOUNT WAITING TO BE DELETED!");
            else
                accountToBeDeleted.setText("");

            if (!buyer) {
                panelStore = new JPanel(new FlowLayout(FlowLayout.CENTER));
                panelStore.setName("Store");
                contentPane.add(panelStore);
                listOfStores = new JList<>(stores);
                panelStore.add(listOfStores);
                JTextField newStoreField = new JTextField(15);
                panelStore.add(newStoreField);
                JButton addStoreButton = new JButton("Add store");
                panelStore.add(addStoreButton);
                addStoreButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!newStoreField.getText().isBlank())
                            client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.CREATE_STORE,
                                    newStoreField.getText()));
                    }
                });
            }

            panelCensorWords = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panelCensorWords.setName("censor");
            contentPane.add(panelCensorWords);
            JLabel censorLabel = new JLabel("Censored Words:");
            panelCensorWords.add(censorLabel);
            censorList = new JComboBox<>(filterWords);
            panelCensorWords.add(censorList);
            JButton removeCensoredWord = new JButton("Remove Word");
            panelCensorWords.add(removeCensoredWord);
            JTextField addCensoredWord = new JTextField(15);
            panelCensorWords.add(addCensoredWord);
            JButton addCensorship = new JButton("Add Censored Word");
            panelCensorWords.add(addCensorship);
            JLabel censorPatternLabel = new JLabel("Censor pattern:");
            panelCensorWords.add(censorPatternLabel);
            pattern = new JTextField(1);
            pattern.setEditable(true);
            pattern.setText(censorPattern);
            panelCensorWords.add(pattern);
            JButton changeCensorPattern = new JButton("change pattern");
            panelCensorWords.add(changeCensorPattern);
            censorModeLabel = new JLabel();
            censorModeLabel.setText(censorMode);
            panelCensorWords.add(censorModeLabel);
            JButton toggleCensorMode = new JButton("Toggle censor mode");
            panelCensorWords.add(toggleCensorMode);

            panelBlockUser = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panelBlockUser.setName("Block User");
            contentPane.add(panelBlockUser);
            JLabel blockLabel = new JLabel("Blocked User:");
            panelBlockUser.add(blockLabel);
            blockList = new JComboBox<>(blockedUsers);
            panelBlockUser.add(blockList);
            JButton removeBlockedUser = new JButton("Unblock user");
            panelBlockUser.add(removeBlockedUser);
            JLabel separatorBlocked = new JLabel("     ");
            panelBlockUser.add(separatorBlocked);
            JButton addBlocked = new JButton("Add block user");
            panelBlockUser.add(addBlocked);

            availableUser = new JComboBox<>((buyer ? listOfSellers : listOfBuyers));
            availableUser.setName("list of available user");
            contentPane.add(availableUser);

            panelInvisUser = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panelInvisUser.setName("Invisible User");
            contentPane.add(panelInvisUser);
            JLabel invisLabel = new JLabel("Made Invisible User:");
            panelInvisUser.add(invisLabel);
            invisList = new JComboBox<>(invisUsers);
            panelInvisUser.add(invisList);
            JButton removeInvisUser = new JButton("un-invisible user");
            panelInvisUser.add(removeInvisUser);
            JLabel separatorInvis = new JLabel("     ");
            panelInvisUser.add(separatorInvis);
            JButton addInvisible = new JButton("Add invisible user");
            panelInvisUser.add(addInvisible);

            changeProfile = new JPanel(new FlowLayout(FlowLayout.CENTER));
            changeProfile.setName("Profile change");
            contentPane.add(changeProfile);
            JTextField usernameToChange = new JTextField(15);
            changeProfile.add(usernameToChange);
            JButton changeUsername = new JButton("Change Username");
            changeProfile.add(changeUsername);
            JTextField emailToChange = new JTextField(15);
            changeProfile.add(emailToChange);
            JButton changeEmail = new JButton("Change Email");
            changeProfile.add(changeEmail);
            if (toBeDeleted) {
                deleteRecoverButton.setText("Recover Account");
            } else {
                deleteRecoverButton = new JButton("Delete Account");
            }
            changeProfile.add(deleteRecoverButton);

            textPane = new JTextArea();
            increasing = new JCheckBox("Sort in increasing order?");
            contentPane.add(textPane);
            contentPane.add(increasing);
            menu.addMenuListener(new MenuListener() {
                @Override
                public void menuSelected(MenuEvent e) {
                    setVisible(false);
                }

                @Override
                public void menuDeselected(MenuEvent e) {
                }

                @Override
                public void menuCanceled(MenuEvent e) {
                }
            });

            removeCensoredWord.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (censorList.getSelectedItem() == null || censorList.getSelectedItem().equals("No censor word!")) {
                        JOptionPane.showMessageDialog(null, "Invalid censor word", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.UNFILTER_WORD,
                                (String) censorList.getSelectedItem()));
                    }
                }
            });

            addCensorship.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println(addCensoredWord.getText());
                    if (addCensoredWord.getText().matches("[^[\\w']]+")) {
                        JOptionPane.showMessageDialog(null, "Not a word!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.FILTER_WORD,
                                addCensoredWord.getText()));
                    }
                }
            });

            changeCensorPattern.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (pattern.getText().length() != 1) {
                        JOptionPane.showMessageDialog(null, "Invalid character", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        client.addByteBufferToWrite(PacketAssembler.assemblePacket(
                                ProtocolRequestType.CHANGE_CENSOR_PATTERN, pattern.getText()));
                    }
                }
            });

            toggleCensorMode.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (censorMode.equals("ON"))
                        client.addByteBufferToWrite(PacketAssembler.assemblePacket(
                                ProtocolRequestType.TURN_OFF_CENSOR_MODE));
                    else
                        client.addByteBufferToWrite(PacketAssembler.assemblePacket(
                                ProtocolRequestType.TURN_ON_CENSOR_MODE));
                }
            });

            removeBlockedUser.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (blockList.getSelectedItem() == null ||
                            blockList.getSelectedItem().equals("you don't have anyone blocked")) {
                        JOptionPane.showMessageDialog(null, "Invalid username to unblock", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.UNBLOCK_USER,
                                (String) blockList.getSelectedItem()));
                    }
                }
            });

            addBlocked.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (availableUser.getSelectedItem() == null ||
                            availableUser.getSelectedItem().equals("There are no sellers!") ||
                            availableUser.getSelectedItem().equals("There are no buyers!")) {
                        JOptionPane.showMessageDialog(null, "Invalid username to block", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.BLOCK_USER,
                                (String) availableUser.getSelectedItem()));
                    }
                }
            });

            removeInvisUser.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (invisList.getSelectedItem() == null ||
                            invisList.getSelectedItem().equals("you don't have anyone invisible")) {
                        JOptionPane.showMessageDialog(null, "Invalid username to unblock", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.UNINVIS_USER,
                                (String) invisList.getSelectedItem()));
                    }
                }
            });

            addInvisible.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (availableUser.getSelectedItem() == null ||
                            availableUser.getSelectedItem().equals("There are no sellers!") ||
                            availableUser.getSelectedItem().equals("There are no buyers!")) {
                        JOptionPane.showMessageDialog(null, "Invalid username to make invisible",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.INVIS_USER,
                                (String) availableUser.getSelectedItem()));
                    }
                }
            });
            
            changeUsername.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (usernameToChange.getText().isBlank()) {
                        return;
                    } else if (Arrays.asList(listOfUsernames).contains(usernameToChange.getText())) {
                        JOptionPane.showMessageDialog(null, "username already taken!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        String password = JOptionPane.showInputDialog(null,
                                "please put in your password to confirm", "password", JOptionPane.QUESTION_MESSAGE);
                        if (password.isBlank())
                            return;
                        client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.CHANGE_USERNAME,
                                usernameToChange.getText(), password));
                    }
                }
            });

            changeEmail.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (emailToChange.getText().isBlank()) {
                        return;
                    } else if (emailToChange.getText().matches("\\b[\\w.%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\\b")) {
                        JOptionPane.showMessageDialog(null, "invalid email",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        String password = JOptionPane.showInputDialog(null,
                                "please put in your password to confirm", "password", JOptionPane.QUESTION_MESSAGE);
                        if (password.isBlank())
                            return;
                        client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.CHANGE_EMAIL,
                                emailToChange.getText(), password));
                    }
                }
            });

            deleteRecoverButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (deleteRecoverButton.getText().equals("Delete Account")) {
                        String password = JOptionPane.showInputDialog(null,
                                "please put in your password to confirm", "password", JOptionPane.QUESTION_MESSAGE);
                        if (password.isBlank())
                            return;
                        client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.DELETE_ACCOUNT,
                                emailToChange.getText()));
                    } else {
                        client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.RECOVER_ACCOUNT));
                    }
                }
            });

            changeUsername.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                }
            });

            increasing.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.REQUEST_DASHBOARD,
                            String.valueOf(increasing.isSelected())));
                }
            });
        }

        public void showProfile() {
            //changes to the screen
            processServerResponse();

            //profile
            usernameField.setText(username);
            usernameField.setColumns(username.length());
            emailField.setText(email);
            emailField.setColumns(email.length());
            roleTextField.setText(role);
            roleTextField.setColumns(role.length());
            accountToBeDeleted.setText("");
            if (toBeDeleted) {
                deleteRecoverButton.setText("Recover Account");
                accountToBeDeleted.setText("ACCOUNT WAITING TO BE DELETED!");
            } else {
                deleteRecoverButton.setText("Delete Account");
                accountToBeDeleted.setText("");
            }

            //store
            if (!buyer) {
                DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(stores);
                listOfStores.setModel(model);
            }

            //censor words
            DefaultComboBoxModel<String> censorModel = new DefaultComboBoxModel<>(filterWords);
            censorList.setModel(censorModel);
            pattern.setText(censorPattern);
            censorModeLabel.setText(censorMode);

            //block user
            DefaultComboBoxModel<String> blockedModel = new DefaultComboBoxModel<>(blockedUsers);
            blockList.setModel(blockedModel);

            //available user
            DefaultComboBoxModel<String> availableModel = new DefaultComboBoxModel<>
                    (buyer ? listOfSellers : listOfBuyers);
            availableUser.setModel(availableModel);

            //invis user
            DefaultComboBoxModel<String> invisModel = new DefaultComboBoxModel<>(invisUsers);
            invisList.setModel(invisModel);

            //dashboard
            textPane.setText(dashboard);

            setVisible(true);
        }
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
                profileFrame.showProfile();
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
        JButton exportConversation = new JButton("Export Conversation");
        JButton themes = new JButton("Themes");
        JSeparator separator = new JSeparator();
        buttonPanel.add(name);
        buttonPanel.add(separator);
        buttonPanel.add(currentTheme);
        buttonPanel.add(newMessage);
        buttonPanel.add(editMessage);
        buttonPanel.add(deleteMessage);
        buttonPanel.add(exportConversation);
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
                if (selectedMessage == null) {
                    JOptionPane.showMessageDialog(null, "Please select the message you want " +
                            "to edit.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (messages[selectedIndex * 2].substring(0, messages[selectedIndex * 2].indexOf(": ")).
                        replace("*", "").equals(currentSelectedMessage)) {
                    JOptionPane.showMessageDialog(null, "You can only edit your own message!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    EditMessage(selectedMessage);
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
        exportConversation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentSelectedMessage != null) {
                    String[] option = new String[]{"export all", "export conversation with " + currentSelectedMessage};
                    int selection = JOptionPane.showOptionDialog(null, "what do you want to export?",
                            "export", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                            option, option[1]);
                    if (selection == JOptionPane.CLOSED_OPTION)
                        return;
                    System.out.println(selection);
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    fileChooser.setToolTipText("Please choose the directory you want to the export csv file in");
                    int answer = fileChooser.showOpenDialog(null);
                    if (answer == JFileChooser.APPROVE_OPTION) {
                        exportPath = fileChooser.getSelectedFile();
                        if (selection == 0)
                            client.addByteBufferToWrite(PacketAssembler.assemblePacket(
                                    ProtocolRequestType.EXPORT_ALL_CONVERSATION));
                        else if (selection == 1)
                            client.addByteBufferToWrite(PacketAssembler.assemblePacket(
                                    ProtocolRequestType.EXPORT_CONVERSATION, currentSelectedMessage));
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select the message you want " +
                            "to export.", "Error", JOptionPane.ERROR_MESSAGE);

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
                    case PROFILE:
                        userProfile = responsePacket.args;
                        if (profileFrame.isVisible()) {
                            profileFrame.showProfile();
                        }
                        break;
                    case USER_NAMES:
                        listOfUsernames = responsePacket.args;
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
                        Messages(responsePacket.args);
                        break;
                    case CSV_EXPORT:
                        assert exportPath != null;
                        File csvExport = new File(exportPath.getAbsolutePath() + File.separator
                                + "export_conversation.csv");
                        try (PrintWriter pw = new PrintWriter(csvExport)) {
                            pw.write(responsePacket.args[0]);
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(null, "IO error while exporting " +
                                    "the conversation", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        JOptionPane.showMessageDialog(null, "Successfully export csv file to - " +
                                csvExport.getAbsolutePath(), "Success", JOptionPane.INFORMATION_MESSAGE);
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
                    case DASHBOARD:
                        dashboard = responsePacket.args[0];
                        if (profileFrame.isVisible()) {
                            profileFrame.showProfile();
                        }
                        break;
                }
            } else if (packet instanceof ErrorPacket) {
                ErrorPacket errorPacket = (ErrorPacket) packet;
                JOptionPane.showMessageDialog(null, String.format("%s: %s", errorPacket.requestType,
                        errorPacket.errorMessage), "Error", JOptionPane.ERROR_MESSAGE);
            }
            AsyncListener asyncListener = new AsyncListener();
            asyncListener.execute();
        }
    }
}
