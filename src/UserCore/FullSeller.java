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
     * @param customerView if true then the sort will show the customers and their message count before the common words list, vise versa
     * @return
     */
    public String viewDashboard(Boolean customerView) {
        String dashboard = "";
        String customers = "Highest Messaging Customers - ";
        String mostPopularWords = "Most Popular Words";
        for (Store store : stores) {
            FullBuyer[] buyers = store.getAllMessagingBuyers().toArray(new FullBuyer[0]);
            Integer[] buyersMessageCount = store.getMessagingBuyersMessageCount().toArray(new Integer[0]);
            customers += PublicInformation.sortCorrespondingArrays(buyers, buyersMessageCount);
        }
        if (customerView) {
            dashboard = customers + "\n" + mostPopularWords;
        } else {
            dashboard = mostPopularWords + "\n" + customers;
        }
        return dashboard;
    }

}
