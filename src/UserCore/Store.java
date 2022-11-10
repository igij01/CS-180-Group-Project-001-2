package UserCore;

import MessageCore.IllegalUserAccessException;

import java.util.ArrayList;

public class Store {

    private String storeName;
    private final Seller owner;
    private int convoCounter = 0;

    private final ArrayList<FullBuyer> allMessagingBuyers = new ArrayList<>();
    private final ArrayList<Integer> messagingBuyersMessageCount = new ArrayList<>();

    protected Store(String storeName, Seller owner) {
        this.storeName = storeName;
        this.owner = owner;
        PublicInformation.addListOfStores(this);
    }

    public String getStoreName() {
        return storeName;
    }

    public Seller getOwner() {
        return this.owner;
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
    
    public int getCounter() {
        return convoCounter;
    }

    protected synchronized void incrementCounter() {
        convoCounter++;
    }

    protected ArrayList<FullBuyer> getAllMessagingBuyers() {
        return allMessagingBuyers;
    }
    protected ArrayList<Integer> getMessagingBuyersMessageCount() {
        return messagingBuyersMessageCount;
    }
    protected void addMessagingBuyer(FullBuyer fullBuyer) {
        allMessagingBuyers.add(fullBuyer);
        int index = PublicInformation.findMatchingObjectIndex(allMessagingBuyers, fullBuyer);
        messagingBuyersMessageCount.set(index, 1);
    }

    protected void incrementBuyerMessageCount(FullBuyer fullBuyer) {
        int index = PublicInformation.findMatchingObjectIndex(allMessagingBuyers, fullBuyer);
        messagingBuyersMessageCount.set(index, messagingBuyersMessageCount.get(index) + 1);
    }


}
