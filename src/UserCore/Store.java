package UserCore;

import MessageCore.IllegalUserAccessException;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class Store implements Serializable {
    // update this field everytime you update the field of the class
    // put in a random number or just increment the number
    @Serial
    private static final long serialVersionUID = 2L;
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

    /**
     *
     * @return the counter of messages sends to this store
     */
    public int getCounter() {
        return convoCounter;
    }

    /**
     * increment the counter when a costumer send a message to this store
     */
    protected synchronized void incrementCounter() {
        convoCounter++;
    }

    /**
     *
     * @return an {@code ArrayList} of {@code FullBuyer} that send to this store
     */
    protected ArrayList<FullBuyer> getAllMessagingBuyers() {
        return allMessagingBuyers;
    }

    /**
     *
     * @return an {@code ArrayList} of {@code Integer} of # of messages each costumer send
     */
    protected ArrayList<Integer> getMessagingBuyersMessageCount() {
        return messagingBuyersMessageCount;
    }

    /**
     * add a FullBuyer to the list
     * @param fullBuyer the FullBuyer requesting this action
     */
    private void addMessagingBuyer(FullBuyer fullBuyer) {
        allMessagingBuyers.add(fullBuyer);
        int index = PublicInformation.findMatchingObjectIndex(allMessagingBuyers, fullBuyer);
        messagingBuyersMessageCount.set(index, 1);
    }

    /**
     * increment the FullBuyer message count
     * @param fullBuyer the FullBuyer requesting this action
     */
    private void incrementBuyerMessageCount(FullBuyer fullBuyer) {
        int index = PublicInformation.findMatchingObjectIndex(allMessagingBuyers, fullBuyer);
        messagingBuyersMessageCount.set(index, messagingBuyersMessageCount.get(index) + 1);
    }


}
