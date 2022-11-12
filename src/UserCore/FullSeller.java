package UserCore;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class FullSeller extends FullUser implements Serializable {

    // update this field everytime you update the field of the class
    // put in a random number or just increment the number
    @Serial
    private static final long serialVersionUID = 2L;

    private final ArrayList<Store> stores;

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
     * @param increasingOrder if true then the sort will go in order from the highest message count to lowest,
     *                        else just in order of history
     * @return String of dashboard
     */
    @Override
    public String viewDashboard(boolean increasingOrder) {
        StringBuilder customers = new StringBuilder("Highest Messaging Customers\n");
        String mostPopularWords = "Most Popular Words\n";
        for (Store store : stores) {
            FullBuyer[] buyers = store.getAllMessagingBuyers().toArray(new FullBuyer[0]);
            Integer[] buyersMessageCount = store.getMessagingBuyersMessageCount().toArray(new Integer[0]);
            customers.append(PublicInformation.correspondingArraysToString(buyers, buyersMessageCount, increasingOrder));
        }

        //MOST POPULAR WORDS GO HERE



        return customers + "\n" + mostPopularWords;
    }

}
