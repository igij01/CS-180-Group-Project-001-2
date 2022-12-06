package GUI;

import Client.PacketAssembler;
import Protocol.ProtocolRequestType;
import UserCore.FullBuyer;
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

public class GUI extends Thread {
    static JPanel buttonPanel = new JPanel();
    static String selectedMessage;
    static JFrame frame;
    static ArrayList<String> items = new ArrayList<>();
    static ArrayList<String> messages = new ArrayList<>();
    static JMenuBar menuBar = new JMenuBar();
    static JMenuBar searchBar = new JMenuBar();
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
        menuBar = new JMenuBar();
        //List();
        //List("Arthur\nLincoln\nSamson\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nsomething\nelse");
        //List(user.printConversationTitles()); a user needs to be created from logging in first
    }

    public static void Profile() {
        frame = new JFrame();
        frame.setSize(600, 450);
        frame.setLocationRelativeTo(null);
        menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        frame.add(panel);
        ArrayList<String> sampleList = new ArrayList<>(Arrays.asList("bad","disgust","Stop it", "wtf"));

        JLabel usernameLabel = new JLabel("Username:");
        panel.add(usernameLabel);
        JTextField username = new JTextField("Ericoco", 12);
        username.setEditable(false);
        panel.add(username);
        JLabel passwordLabel = new JLabel("Password:");
        panel.add(passwordLabel);
        JPasswordField password = new JPasswordField(15);
        password.setText("passwordtesting");
        password.setEchoChar('*');
        password.setEditable(false);
        final Boolean[] passwordVisible = {false};
        panel.add(password);
        JButton showPass = new JButton("Show");
        panel.add(showPass);
        JLabel emailLabel = new JLabel("Email:");
        panel.add(emailLabel);
        JTextField email = new JTextField("sampleemail@gmail.com", 20);
        email.setEditable(false);
        panel.add(email);
        JLabel censorLabel = new JLabel("Censored Words:");
        panel.add(censorLabel);
        JComboBox censorList = new JComboBox(sampleList.toArray());
        panel.add(censorList);
        JTextField addCensoredWord = new JTextField(10);
        panel.add(addCensoredWord);
        JButton addCensorship = new JButton("Add Censored Word");
        panel.add(addCensorship);
        JTextField usernameToChange = new JTextField(10);
        panel.add(usernameToChange);
        JButton changeUsername = new JButton("Change Username");
        panel.add(changeUsername);
        JTextField emailToChange = new JTextField(10);
        panel.add(emailToChange);
        JButton changeEmail = new JButton("Change Email");
        panel.add(changeEmail);

        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
        menu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                frame.setVisible(false);
                Menu();
            }
            @Override
            public void menuDeselected(MenuEvent e) {
            }
            @Override
            public void menuCanceled(MenuEvent e) {
            }
        });
        addCensorship.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        changeUsername.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        showPass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!passwordVisible[0]) {
                    password.setEchoChar((char) 0);
                    passwordVisible[0] = true;
                } else {
                    password.setEchoChar('*');
                    passwordVisible[0] = false;
                }
            }
        });
    }

    /**
     * passwordCheck
     *
     * will prompt user to enter password to verify they can change an email or username. Verification method
     *
     * @return true if the password they entered is correct, false otherwise
     */
    public static void passwordCheck() {

    }
    public static void Menu() {
        Setup();
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

    public static void Messages(String username) {
        buttonPanel.setVisible(false);
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
        buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.black));
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
        frame.add(buttonPanel, BorderLayout.NORTH);

        JList messagesList = new JList(messages.toArray());
        messagesList.setLayoutOrientation(JList.VERTICAL);
        scrollMessage.setPreferredSize(new Dimension(535, 400));
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                selectedMessage = (String) messagesList.getSelectedValue();
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
    public static void EditMessage(String username, String message) {
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
        frame.add(textPanel, BorderLayout.NORTH);

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
                frame.remove(textPanel);
                Messages(username);
            }
        });






    }

    public static void NewMessage(String username) {
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
        frame.add(textPanel, BorderLayout.NORTH);

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
                frame.remove(textPanel);
                Messages(username);
            }
        });


    }

    public static void Search() {
        menuBar.setVisible(false);
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
                    System.out.println(String.valueOf(PacketAssembler.assemblePacket(ProtocolRequestType.REQUEST_PUBLIC_INFO)));
                }
            }
        });
    }

    public static void ClearList() {
        items.clear();
    }

    public static void List(String elements) {
        if (elements == null) {
            elements = "Nothing to see here";
        }
        String[] added = elements.split("\n", -2);
        items.addAll(List.of(added));
        Messages(items.get(0));
        JList list = new JList(items.toArray());
        list.setLayoutOrientation(JList.VERTICAL);
        scrollPane.setPreferredSize(new Dimension(200, 750));
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String selectedItem = (String) list.getSelectedValue();
                Messages(selectedItem);
                System.out.println(selectedItem);
                String[] part = selectedItem.split(":", 2);
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
                PacketAssembler.assemblePacket(ProtocolRequestType.LOGIN, userText.getText(), String.valueOf(passText.getPassword()));

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
