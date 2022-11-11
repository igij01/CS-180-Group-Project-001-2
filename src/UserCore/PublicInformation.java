package UserCore;

import javax.swing.*;
import java.util.ArrayList;

public class PublicInformation { //Add an ArrayList of FullBuyer/FullSeller instead of Buyer/Seller. It will be used to send messages between 2 users - Lincoln

    public static ArrayList<Store> listOfStores = new ArrayList<>();
    public static ArrayList<String> listOfUsersNames = new ArrayList<>();

    public static ArrayList<FullSeller> listOfSellers = new ArrayList<>();

    public static ArrayList<FullBuyer> listOfBuyers = new ArrayList<>();

    private PublicInformation() {}

    /**
     * Add store into the public list of stores
     * Always call this when you create a store(Already added in the last line of {@code Store} constructor)
     *
     * @param store the store instance to be added
     */
    protected static void addListOfStores(Store store) {
        listOfStores.add(store);
    }

    /**
     * Add a seller into the public list of sellers
     * Always call this when you create a seller(Already added in the last line of {@code FullSeller} constructor)
     *
     * @param seller the seller to be added
     */
    protected static void addListOfSellers(FullSeller seller) {
        listOfSellers.add(seller);
    }

    /**
     * Add username into the public list of user ame
     * Always call this when you create a user(Already added in the last line of {@code User} constructor)
     *
     * @param name the name chosen by the user
     */
    protected static void addListOfUsersNames(String name) {
        listOfUsersNames.add(name);
    }

    /**
     * Adds a buyer to the public list of buyers
     * @param buyer the buyer to be added
     */
    protected static void addListOfBuyers(FullBuyer buyer) {
        listOfBuyers.add(buyer);
    }

    public static FullSeller findFullSellerFromStore(Store store) {
        for (FullSeller seller : listOfSellers) {
            if (seller.getUser().equals(store.getOwner()))
                return seller;
        }
        return null;
    }

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
    /**
     * Goes through array and finds the matching full buyer and then
     * @return matching index
     */
    public static int findMatchingObjectIndex(ArrayList<FullBuyer> fbArray, FullBuyer fb) {
        int index = -1;
        for (int i = 0; i < fbArray.size(); i++) {
            if (fb.equals(fbArray.get(i))) {
                index = i;
            }
        }
        return index;
    }

    /**
     * Checks to see if a store being created has a matching name with another store already made
     * @param name name of the store being create
     *
     * @return true if there is a duplicate, false if no duplicate
     */
    public static boolean findDuplicateStoreName(String name) {
        for (Store s : listOfStores) {
            if (s.getStoreName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static String sortCorrespondingArrays(ArrayList<FullUser> users, ArrayList<Integer> usersCount) {
        FullUser[] users1 = users.toArray(new FullUser[0]);
        Integer[] usersCount1 = usersCount.toArray(new Integer[0]);
        for (int i = 0; i < users1.length; i++) {
            for (int j = i + 1; j <users1.length; j++) {
                FullUser tempUsr;
                Integer tempInt;
                if (usersCount1[i] < usersCount1[j]) {
                    tempUsr = users1[i];
                    tempInt = usersCount1[i];
                    users1[i] = users1[j];
                    usersCount1[i] = usersCount1[j];
                    users1[j] = tempUsr;
                    usersCount1[j] = tempInt;
                }
            }
        }
        String s = "| ";
        for (int i = 0; i < users1.length; i++) {
            if (i == users1.length - 1) {
                s += users1[i].getUser().getUserName() + ": " + usersCount1[i] + "|";
            } else {
                s += users1[i].getUser().getUserName() + ": " + usersCount1[i] + " ";
            }
        }
        return s;
    }


    public static void main(String[] args) {
        Store store = new Store("A", new Seller("fads", "samsonates@gmail.com", "asdf"));
        System.out.println(listOfStores.get(0).getStoreName());
    }
}
