package Client;

import Protocol.ErrorPacket;
import Protocol.ProtocolRequestType;
import Protocol.ResponsePacket;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class ClientGUI extends JFrame {

    private JPanel contentPane;
    private JList<String> list;
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

    /**
     * Create the frame.
     */
    public ClientGUI(ClientCore client, String[] listOfUsernames, String[] userProfile, boolean buyer) {
        this.listOfUsernames = listOfUsernames;
        this.client = client;
        this.userProfile = userProfile;
        this.buyer = buyer;
        client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.DISPLAY_CONVERSATION_TITLES));
        client.addByteBufferToWrite(PacketAssembler.assemblePacket(ProtocolRequestType.REQUEST_PUBLIC_INFO));
        AsyncListener listener = new AsyncListener();
        listener.execute();
        setTitle("Main Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);

        list = new JList<>(conversationTitles);
        list.setToolTipText("select a conversation from the list to view");
        list.setForeground(new Color(0, 0, 0));
        list.setFont(new Font("Dialog", Font.PLAIN, 12));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list,
                                                          Object value, int index, boolean isSelected,
                                                          boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index,
                        isSelected, cellHasFocus);
                DefaultListModel<String> model = new DefaultListModel<>();

                String username = (String) value;

                if (username.contains("\n")) {
                    list.setModel(model);
                    model.set(index, username.replace("\n", ""));
                    setBackground(new Color(34, 202, 215));
                } else {
                    setBackground(new Color(255, 255, 255));
                }
                return this;
            }
        });

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
                        list.setListData(conversationTitles);
                        break;
                    case PUBLIC_INFO:

                }
            } else if (packet instanceof ErrorPacket) {

            }
            AsyncListener asyncListener = new AsyncListener();
            asyncListener.execute();
        }
    }
}
