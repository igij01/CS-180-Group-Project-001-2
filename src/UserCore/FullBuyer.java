package UserCore;

import MessageCore.IllegalMessageException;
import MessageCore.IllegalTargetException;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class FullBuyer extends FullUser implements Serializable {

    // update this field everytime you update the field of the class
    // put in a random number or just increment the number
    @Serial
    private static final long serialVersionUID = 2L;

    private final ArrayList<Store> storesMessaged = new ArrayList<>();
    private final ArrayList<Integer> timesStoresMessaged = new ArrayList<>();

    /**
     * Creates a new Full Buyer instance
     *
     * @param userName the name of the user
     * @param email    the email address assoc. with the user
     * @param pwd      the password of the user
     * @throws EmailFormatException     when email address are not put in the right format
     * @throws IllegalUserNameException when the passed in username is already taken
     */
    public FullBuyer(String userName, String email, String pwd) throws IllegalUserNameException, EmailFormatException {
        super(new Buyer(userName, email, pwd));
        PublicInformation.addListOfBuyers(this);
    }

    /**
     * message the store
     * <p>
     * and update the message counter accordingly
     *
     * @param store   the store you want to message
     * @param content the content of the message
     */
    public void messageStore(Store store, String content) {
        super.createMessage(Objects.requireNonNull(PublicInformation.findFullSellerFromStore(store)), content);
        store.incrementCounter(this);


        if (!storesMessaged.contains(store)) {
            storesMessaged.add(store);
            timesStoresMessaged.add(1);
            //it will always append to last element
        } else {
            int index = storesMessaged.indexOf(store);
            timesStoresMessaged.set(index, timesStoresMessaged.get(index) + 1);
        }
    }

    /**
     * message a specific seller
     *
     * @param seller  the seller you want to message
     * @param content the content of the message
     * @throws IllegalTargetException  when the target and the sender are the same role
     * @throws IllegalMessageException when either the sender or the receiver is not a participant of the conversation
     */
    public void messageSeller(FullSeller seller, String content) throws
            IllegalTargetException, IllegalMessageException {
        super.createMessage(seller, content);
    }

    /**
     * message a specific seller
     *
     * @param seller the seller you want to message
     * @param file   the file that contains the content of the message
     * @throws IllegalTargetException  when the target and the sender are the same role
     * @throws IllegalMessageException when either the sender or the receiver is not a participant of the conversation
     * @throws IOException             when IO exception occurs
     */
    public void messageSeller(FullSeller seller, File file) throws
            IllegalTargetException, IllegalMessageException, IOException {
        super.createMessage(seller, file);
    }

    /**
     * Customer can view a dashboard with personal statistics
     *
     * @param increasing determines whether the dashboard shows the stores in terms of most messaged or not
     */
    @Override
    public String viewDashboard(boolean increasing) {
        String mostPopStores = "Most Popular Stores\n";
        String personalStores = "Your Most Messaged Stores\n";
        Store[] stores;
        if (increasing) {
            stores = PublicInformation.sortStoresByPopularity(PublicInformation.listOfStores.toArray(new Store[0]));
        } else {
            stores = PublicInformation.listOfStores.toArray(new Store[0]);
        }
        Store[] mostMessagedStores = storesMessaged.toArray(new Store[0]);
        Integer[] timesMessaged = timesStoresMessaged.toArray(new Integer[0]);
        for (Store store : stores) {
            mostPopStores += "(" + store.getStoreName() + ") : " + store.getCounter() + "\n";
        }
        personalStores += PublicInformation.correspondingArrayToString(mostMessagedStores, timesMessaged, increasing);
        return mostPopStores + personalStores;
    }


    public static void main(String[] args) {
        FullBuyer f1 = new FullBuyer("username1", "sample@gmail.com", "abc123");
        Seller s1 = new Seller("user2", "sample@gmail.com", "abc123");
        Seller s2 = new Seller("user3", "sample@gmail.com", "pwd123");
        Seller s3 = new Seller("user4", "sample@gmail.com", "pwd123");

        //FullSeller fs1 = new FullSeller("user2" , "sample@gmail.com", "abc123");
        //FullSeller fs2 = new FullSeller("user3", "sample@gmail.com", "pwd123");
        //FullSeller fs3 = new FullSeller("user4", "sample@gmail.com", "pwd123");

        Store store1 = new Store("Store1", s1);
        Store store2 = new Store("Store2", s2);
        Store store3 = new Store("Store3", s3);

        //f1.messageStore(store1, "Hi");
        //f1.messageStore(store2, "Hey");
        //f1.messageStore(store2, "MOst");
        System.out.println(f1.viewDashboard(true));
    }

}
