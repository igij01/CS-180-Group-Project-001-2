package UserCore;

import MessageCore.IllegalMessageException;
import MessageCore.IllegalTargetException;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * FullBuyer
 * <p>
 * A FullBuyer class where the FullUser's role is a buyer and operates as a buyer
 *
 * @author Arthur Ruano, Samson Tesfagiorgis, Yulin Lin, 001
 * @version 11/5/2022
 */
public class FullBuyer extends FullUser implements Serializable {

    // update this field everytime you update the field of the class
    // put in a random number or just increment the number
    @Serial
    private static final long serialVersionUID = 2L;

    private ArrayList<Store> storesMessaged = new ArrayList<>();
    private ArrayList<Integer> timesStoresMessaged = new ArrayList<>();

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
     * recreate a Full Buyer instance after server shutdown
     *
     * @param userName            the name of the original user
     * @param email               the email address
     * @param pwd                 the pwd
     * @param storesMessaged      the array stores messaged
     * @param timesStoresMessaged the array times stores messaged
     */
    public FullBuyer(String userName, String email, String pwd, ArrayList<Store> storesMessaged,
                     ArrayList<Integer> timesStoresMessaged) {
        super(new Buyer(userName, email, pwd));
        this.storesMessaged = storesMessaged;
        this.timesStoresMessaged = timesStoresMessaged;
    }

    /**
     * Used to receive a store destructed and remove store from storeMessaged list
     *
     * @param store store destructed
     */
    private void notifyStoreDestruction(Store store) {
        if (storesMessaged.contains(store)) {
            int index = storesMessaged.indexOf(store);
            storesMessaged.remove(index);
            timesStoresMessaged.remove(index);
        }
    }

    /**
     * call this method IF AND ONLY IF the FullUser is going to be deconstructed and not recoverable
     * <p>
     * and remove store from storeMessaged list
     *
     * @param user the {@code FullSeller} that deleted his/her account
     */
    @Override
    protected void receiveUserDestruction(FullUser user) {
        if (user instanceof FullSeller) {
            for (Store store : ((FullSeller) user).getStores())
                notifyStoreDestruction(store);
            super.receiveUserDestruction(user);
        } else
            throw new IllegalTargetException("The user is a Buyer!");
    }

    /**
     * message the store
     * <p>
     * and update the message counter accordingly
     *
     * @param store   the store you want to message
     * @param content the content of the message
     * @return if the message is not blocked
     */
    public boolean messageStore(Store store, String content) {
        boolean blocked = super.createMessage(Objects.requireNonNull(PublicInformation.findFullSellerFromStore(store)),
                content);
        store.incrementCounter(this);


        if (!storesMessaged.contains(store)) {
            storesMessaged.add(store);
            timesStoresMessaged.add(1);
            //it will always append to last element
        } else {
            int index = storesMessaged.indexOf(store);
            timesStoresMessaged.set(index, timesStoresMessaged.get(index) + 1);
        }
        return blocked;
    }

    /**
     * message the store
     * <p>
     * and update the message counter accordingly
     *
     * @param store   the store you want to message
     * @param txtFile the txt file that contains the message content
     * @throws IOException when IO exception occurs
     */
    public void messageStore(Store store, File txtFile) throws IOException {
        super.createMessage(Objects.requireNonNull(PublicInformation.findFullSellerFromStore(store)), txtFile);
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

    protected ArrayList<Store> getStoresMessaged() {
        return storesMessaged;
    }

    /**
     * message a specific seller
     *
     * @param seller  the seller you want to message
     * @param content the content of the message
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
     * @throws IOException when IO exception occurs
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

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
