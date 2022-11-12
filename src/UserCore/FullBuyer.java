package UserCore;

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
    public FullBuyer(String userName, String email, String pwd) {
        super(new Buyer(userName, email, pwd));
        PublicInformation.addListOfBuyers(this);
    }

    public void messageStore(Store store, String content) {
        super.createMessage(Objects.requireNonNull(PublicInformation.findFullSellerFromStore(store)), content);
        store.incrementCounter(this);


        if (!storesMessaged.contains(store)) {
            storesMessaged.add(store);
            for (int i = 0; i < storesMessaged.size(); i++) {
                if (storesMessaged.get(i).equals(store)) {
                    timesStoresMessaged.set(i, 1);
                }
            }
        }

        for (int i = 0; i < storesMessaged.size(); i++) {
            if (storesMessaged.get(i).equals(store)) {
                timesStoresMessaged.set(i, timesStoresMessaged.get(i) + 1);
            }
        }
    }

    public void messageSeller(FullSeller seller, String content) {
        super.createMessage(seller, content);
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
