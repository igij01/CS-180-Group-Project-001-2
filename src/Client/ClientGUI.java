package Client;

import javax.swing.*;
import java.awt.*;

public class ClientGUI extends JFrame {

    private JPanel contentPane;
    private JList<String> list;
    private String[] names = {"Bird", "Cat", "Dog", "Rabbit", "Pig", "dukeWaveRed"};

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ClientGUI frame = new ClientGUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ClientGUI() {
        setTitle("Main Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);

        list = new JList<>(names);
        list.setToolTipText("select a conversation from the list to view");
        list.setForeground(new Color(28, 113, 216));
        list.setFont(new Font("Dialog", Font.PLAIN, 12));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        getContentPane().add(tabbedPane);

        JSplitPane mainPane = new JSplitPane();
        tabbedPane.addTab("Main", null, mainPane, null);

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setToolTipText("");
        mainPane.setLeftComponent(scrollPane);

        JPanel profile = new JPanel();
        tabbedPane.addTab("profile", null, profile, null);
        profile.setLayout(new GridLayout(1, 0, 0, 0));

        JPanel dashboard = new JPanel();
        tabbedPane.addTab("dashboard", null, dashboard, null);
    }

}
