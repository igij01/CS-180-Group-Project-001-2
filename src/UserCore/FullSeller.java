package UserCore;

import MessageCore.IllegalMessageException;
import MessageCore.IllegalTargetException;

import java.io.File;
import java.io.IOException;
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
     * message a specific buyer
     *
     * @param buyer  the buyer you want to message
     * @param content the content of the message
     */
    public void messageBuyer(FullBuyer buyer, String content) throws
            IllegalTargetException, IllegalMessageException {
        super.createMessage(buyer, content);
    }

    /**
     * message a specific buyer
     *
     * @param buyer  the buyer you want to message
     * @param txtFile the content of the message
     * @throws IOException when io exception occurs
     */
    public void messageBuyer(FullBuyer buyer, File txtFile) throws
            IllegalTargetException, IllegalMessageException, IOException {
        super.createMessage(buyer, txtFile);
    }

    //returns list of buyers whose usernames contain search
    public ArrayList<FullBuyer> searchCustomers(String search) {
        ArrayList<FullBuyer> r = null;
        for(int i = 0; i < PublicInformation.getListOfBuyers().size(); i++){
            if(PublicInformation.getListOfBuyers().get(i).getUser().getUserName().contains(search)){
                r.add(PublicInformation.getListOfBuyers().get(i));
            }
        }
        return r;
    }
    
    //from PublicInformation
    public static Store[] sortStoresByPopularity(Store[] storeList) {
        for (int i = 0; i < storeList.length; i++) {
            for (int j = i + 1; j < storeList.length; j++) {
                Store temp;
                if (storeList[i].getCounter() < storeList[j].getCounter()) {
                    temp = storeList[i];
                    storeList[i] = storeList[j];
                    storeList[j] = temp;
                }
            }
        }
        return storeList;
    }
    
    //finds most common word, if multiple, returns all
    public ArrayList<String> mostCommonWords() {
        ArrayList<String> words = null;
        ArrayList<Integer> occurances = null;
        ArrayList<String> mcwords= null;
        //loops through conversations
        for(int i = 0; i < this.getConversations().size(); i++) {
            //loops through messages
            //added conversation getter to FullUser and Conversation, added message getter in Message
            for(int k = 0; k < this.getConversations().get(i).getConversation().size(); k++){
                //counts number of words in conversation
                int wordnum = 1;
                for(int j = 0; j < (this.getConversations().get(i).getConversation().get(k).getMessage().length()); j++){
                    if(this.getConversations().get(i).getConversation().get(k).getMessage().charAt(j) == ' '){
                        wordnum++;
                    }
                }
                //splits each conversation into array of words
                String[] temp = new String[wordnum];
                //loops through words
                for(int j = 0; j < wordnum; j++) {;
                    //if word already in words arraylist
                    boolean wiw = false;
                    for(int l = 0; l < words.size(); l++){
                        if(temp[j].equals(words.get(l))){
                            wiw = true;
                        }
                    }
                    if(wiw){
                        occurances.set(j, occurances.get(j) + 1);
                    } else {
                        words.add(temp[j]);
                        occurances.add(1);
                    }
                }
            }
        }

        //finds the max
        int max = 0;
        for(int i = 0; i < occurances.size(); i++){
            if(occurances.get(i) > max){
                mcwords= null;
                mcwords.add(words.get(i));
            } else if(occurances.get(i) == max){
                mcwords.add(words.get(i));
            }
        }

        return mcwords;
    }
    
    /**
     * @param increasingOrder if true then the sort will go in order from the highest message count to lowest,
     *                        else just in order of history
     * @return String of dashboard
     */
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

    protected ArrayList<Store> getStores() {
        return this.stores;
    }

}
