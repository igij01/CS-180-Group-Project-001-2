package UserCore;

import MessageCore.IllegalUserAccessException;

public class Store {

    private String storeName;
    private Seller owner;

    public Store (String storeName,Seller owner) {
        this.storeName = storeName;
        this.owner = owner;
    }

    public String getStoreName() {
        return storeName;
    }

    protected void setStoreName(String storeName, Seller reqSeller) throws IllegalUserAccessException {
        if (reqSeller.equals(owner)) {
            this.storeName = storeName;
        } else {
            throw new IllegalUserAccessException("You are not the owner of this store!");
        }
    }

}
