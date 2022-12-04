package UserCore;

import MessageCore.Conversation;
import MessageCore.IllegalMessageException;
import MessageCore.IllegalTargetException;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

/**
 * FullSeller
 * <p>
 * <p>
 * A FullSeller class where the FullUser takes the role of a seller has operations specific to its class
 *
 * @author Samson Tesfagiorgis, Yulin Lin, Teresa Wan, 001
 * @version 11/3/2022
 */
public class FullSeller extends FullUser implements Serializable {

    private static final ArrayList<String> stopWords = new ArrayList<>();

    static {
        try (BufferedReader bfr = new BufferedReader(new FileReader("stop_words_english.txt"))) {
            String words;
            while ((words = bfr.readLine()) != null) {
                stopWords.add(words);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // update this field everytime you update the field of the class
    // put in a random number or just increment the number
    @Serial
    private static final long serialVersionUID = 2L;

    private ArrayList<Store> stores;

    /**
     * create a FullSeller instance
     *
     * @param username the name of the user
     * @param email    the email assoc. with the account
     * @param pwd      the pwd set by the user
     * @throws EmailFormatException     when email address are not put in the right format
     * @throws IllegalUserNameException when the passed in username is already taken
     */
    public FullSeller(String username, String email, String pwd) {
        super(new Seller(username, email, pwd));
        PublicInformation.addListOfSellers(this);
        stores = new ArrayList<>();
    }

    /**
     * recreate a Full Seller instance after server shutdown
     *
     * @param userName the name of the original user
     * @param email    the email address
     * @param pwd      the pwd
     * @param stores   the array stores of stores
     */
    protected FullSeller(String userName, String email, String pwd, ArrayList<Store> stores) {
        super(new Buyer(userName, email, pwd));
        this.stores = stores;
    }

    /**
     * create a new store for this seller
     *
     * @param storeName the name of the store
     * @throws IllegalStoreNameException when the store name is already taken
     */
    public void createStore(String storeName) throws IllegalStoreNameException {
        Store store = new Store(storeName, (Seller) getUser());
        stores.add(store);
    }

    /**
     * message a specific buyer
     *
     * @param buyer   the buyer you want to message
     * @param content the content of the message
     */
    public void messageBuyer(FullBuyer buyer, String content) throws
            IllegalTargetException, IllegalMessageException {
        super.createMessage(buyer, content);
    }

    /**
     * message a specific buyer
     *
     * @param buyer   the buyer you want to message
     * @param txtFile the content of the message
     * @throws IOException when io exception occurs
     */
    public void messageBuyer(FullBuyer buyer, File txtFile) throws
            IllegalTargetException, IllegalMessageException, IOException {
        super.createMessage(buyer, txtFile);
    }

    /**
     * find the most frequent words in overall messages
     *
     * @return the first most frequent word in all messages send and received
     */
    public String mostCommonWordsOverall() {
        Hashtable<String, Integer> hashtable = new Hashtable<>();
        for (Conversation c : super.getConversations()) {
            for (String w : c.allWordsFromMessages((Seller) this.getUser())) {
                if (!stopWords.contains(w)) {
                    if (hashtable.containsKey(w))
                        hashtable.put(w, hashtable.get(w) + 1);
                    else
                        hashtable.put(w, 1);
                }
            }
        }
        String result = null;
        int mostFrequent = 0;
        for (Map.Entry<String, Integer> entry : hashtable.entrySet()) {
            if (mostFrequent < entry.getValue()) {
                mostFrequent = entry.getValue();
                result = entry.getKey();
            }
        }
        return result;
    }

    /**
     * @param increasingOrder if true then the sort will go in order from the highest message count to lowest,
     *                        else just in order of history
     * @return String of dashboard
     */
    public String viewDashboard(boolean increasingOrder) {
        StringBuilder customers = new StringBuilder("Highest Messaging Customers\n");
        String word = mostCommonWordsOverall();
        if (word == null)
            word = "You haven't received nor send any messages";
        String mostPopularWords = "Most Popular meaningful word\n" + word;
        for (Store store : stores) {
            FullBuyer[] buyers = store.getAllMessagingBuyers().toArray(new FullBuyer[0]);
            Integer[] buyersMessageCount = store.getMessagingBuyersMessageCount().toArray(new Integer[0]);
            customers.append(PublicInformation.correspondingArraysToString(buyers, buyersMessageCount, increasingOrder));
        }
        return customers + "\n" + mostPopularWords + "\n";
    }

    protected ArrayList<Store> getStores() {
        return this.stores;
    }

    @Override
    public String toString() {
        return super.toString() + "\nStore: " + stores;
    }
}
