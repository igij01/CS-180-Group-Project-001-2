package UserCore;

import MessageCore.IllegalUserAccessException;

public class Store {

    private String storeName;
    private Seller owner;
    private int convoCounter = 0;

    public Store (String storeName,Seller owner) {
        this.storeName = storeName;
        this.owner = owner;
        PublicInformation.addListOfStores(this);
    }

    public String getStoreName() {
        return storeName;
    }



    /**
     * Changes name of Store
     *
     * @param storeName new name of the store
     * @param reqSeller who is requesting to change the name
     * @throws IllegalUserAccessException if the Seller is not the owner of the store as owners are only allowed
     */
    protected void setStoreName(String storeName, Seller reqSeller) throws IllegalUserAccessException {
        if (reqSeller.equals(owner)) {
            this.storeName = storeName;
        } else {
            throw new IllegalUserAccessException("You are not the owner of this store!");
        }
    }
    
    public int getConvoCounter() {
        return convoCounter;
    }
    protected synchronized void incrementCounter() {
        convoCounter++;
    }

}
