package UserCore;

import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;

public class FullSeller extends FullUser {

    private ArrayList<Store> stores;
    public FullSeller (String username, String email, String pwd) {
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

    public void viewDashboard() {

    }

}
