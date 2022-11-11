package UserCore;

import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;
import java.util.Arrays;

public class FullSeller extends FullUser {

    private final ArrayList<Store> stores;

    public FullSeller(String username, String email, String pwd) {
        super(new Seller(username, email, pwd));
        PublicInformation.addListOfSellers(this);
        stores = new ArrayList<>();
    }

    public boolean createStore(String storeName) throws IllegalStoreNameException {
        if (!PublicInformation.findDuplicateStoreName(storeName)) {
            Store store = new Store(storeName, (Seller) getUser());
            stores.add(store);
            return true;
        }
        throw new IllegalStoreNameException("This store name is already taken!");
    }


    /**
     * @param increasingOrder if true then the sort will go in order from highest message count to lowest, else just in order of history
     * @return String of dashboard
     */
    public String viewDashboard(boolean increasingOrder) {
        String customers = "Highest Messaging Customers\n";
        String mostPopularWords = "Most Popular Words\n";
        for (Store store : stores) {
            FullBuyer[] buyers = store.getAllMessagingBuyers().toArray(new FullBuyer[0]);
            Integer[] buyersMessageCount = store.getMessagingBuyersMessageCount().toArray(new Integer[0]);
            customers += PublicInformation.correspondingArraysToString(buyers, buyersMessageCount, increasingOrder);
        }

        //MOST POPULAR WORDS GO HERE



        return customers + "\n" + mostPopularWords;
    }

}
