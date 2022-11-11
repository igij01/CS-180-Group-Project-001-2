package UserCore;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class PublicInformation { //Add an ArrayList of FullBuyer/FullSeller instead of Buyer/Seller. It will be used to send messages between 2 users - Lincoln

    public static ArrayList<Store> listOfStores = new ArrayList<>();
    public static ArrayList<String> listOfUsersNames = new ArrayList<>();

    public static ArrayList<FullSeller> listOfSellers = new ArrayList<>();

    public static ArrayList<FullBuyer> listOfBuyers = new ArrayList<>();

    private static boolean unserialized = false;

    /**
     * initialize the lists from serialized files. Can only be called once per runtime
     * multiples calls will be ignored
     */
    @SuppressWarnings("unchecked")
    public static void init() {
        if (!unserialized) {
            try (ObjectInputStream oinBuyers = new ObjectInputStream
                    (new FileInputStream("src/UserCore/serialized_buyers"));
                 ObjectInputStream oinSellers = new ObjectInputStream
                         (new FileInputStream("src/UserCore/serialized_sellers"));
                 ObjectInputStream oinStores = new ObjectInputStream
                         (new FileInputStream("src/UserCore/serialized_stores"));
                 ObjectInputStream oinNames = new ObjectInputStream
                         (new FileInputStream("src/UserCore/serialized_usernames"))) {
                listOfBuyers = (ArrayList<FullBuyer>) oinBuyers.readObject();
                listOfSellers = (ArrayList<FullSeller>) oinSellers.readObject();
                listOfStores = (ArrayList<Store>) oinStores.readObject();
                listOfUsersNames = (ArrayList<String>) oinNames.readObject();
                unserialized = true;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is used for debugging purposes ONLY! If you need to call this,
     * use Reflection to get and invoke this method.
     * <p>
     * This method is used to initialize the lists using custom files to test
     * the data persistence. DO NOT call the serialize method as it will OVERWRITE
     * the existing serialized files. Use serializeToFiles instead
     * <p>
     * This will ignore the serialization flag,
     * so it's guaranteed that it will always have an effect.
     * @param buyers the file that has the buyers serialized
     * @param sellers the file that has the sellers serialized
     * @param stores the file that has the stores serialized
     * @param usernames the file that has the usernames serialized
     * @see #serializeToFiles(File, File, File, File)
     * @see java.lang.reflect.Method#setAccessible(boolean)
     * @see java.lang.reflect.Method#invoke(Object, Object...)
     */
    @SuppressWarnings({"unchecked", "unused"})
    private static void initFromFiles(File buyers, File sellers, File stores, File usernames) {
        try (ObjectInputStream oinBuyers = new ObjectInputStream
                (new FileInputStream(buyers));
             ObjectInputStream oinSellers = new ObjectInputStream
                     (new FileInputStream(sellers));
             ObjectInputStream oinStores = new ObjectInputStream
                     (new FileInputStream(stores));
             ObjectInputStream oinNames = new ObjectInputStream
                     (new FileInputStream(usernames))) {
            listOfBuyers = (ArrayList<FullBuyer>) oinBuyers.readObject();
            listOfSellers = (ArrayList<FullSeller>) oinSellers.readObject();
            listOfStores = (ArrayList<Store>) oinStores.readObject();
            listOfUsersNames = (ArrayList<String>) oinNames.readObject();
            unserialized = true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used for debugging purposes ONLY! If you need to call this,
     * use Reflection to get and invoke this method.
     * <p>
     * This method is used to generate a serialized files to check data persistence
     * Use in conjunction with initFromFiles
     * @param buyers the file that buyers serialize to
     * @param sellers the file that sellers serialize to
     * @param stores the file that stores serialize to
     * @param usernames the file that usernames serialize to
     * @see #initFromFiles(File, File, File, File)
     * @see java.lang.reflect.Method#setAccessible(boolean)
     * @see java.lang.reflect.Method#invoke(Object, Object...)
     */
    @SuppressWarnings({"unused"})
    private static void serializeToFiles(File buyers, File sellers, File stores, File usernames) {
        try (ObjectOutputStream oinBuyers = new ObjectOutputStream
                (new FileOutputStream(buyers));
             ObjectOutputStream oinSellers = new ObjectOutputStream
                     (new FileOutputStream(sellers));
             ObjectOutputStream oinStores = new ObjectOutputStream
                     (new FileOutputStream(stores));
             ObjectOutputStream oinNames = new ObjectOutputStream
                     (new FileOutputStream(usernames))) {
            oinBuyers.writeObject(listOfBuyers);
            oinSellers.writeObject(listOfSellers);
            oinNames.writeObject(listOfUsersNames);
            oinStores.writeObject(listOfStores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * serialize the lists and write them to the corresponding files
     * do this after everything is done
     */
    public static void serialize() {
        try (ObjectOutputStream oinBuyers = new ObjectOutputStream
                (new FileOutputStream("src/UserCore/serialized_buyers"));
             ObjectOutputStream oinSellers = new ObjectOutputStream
                     (new FileOutputStream("src/UserCore/serialized_sellers"));
             ObjectOutputStream oinStores = new ObjectOutputStream
                     (new FileOutputStream("src/UserCore/serialized_stores"));
             ObjectOutputStream oinNames = new ObjectOutputStream
                     (new FileOutputStream("src/UserCore/serialized_usernames"))) {
            oinBuyers.writeObject(listOfBuyers);
            oinSellers.writeObject(listOfSellers);
            oinNames.writeObject(listOfUsersNames);
            oinStores.writeObject(listOfStores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PublicInformation() {}

    /**
     * LOGIN METHOD
     * @param user String of the username they want to log into
     * @param password password
     *
     * @return The FullBuyer if they logged in to a FullBuyer, or a FullSeller if they logged into a FullSeller
     * @throws InvalidPasswordException when the username matches but the password is incorrect
     * @throws IllegalUserNameException when there is no username that matches
     */
    public static FullUser login(String user, String password) throws InvalidPasswordException, IllegalUserNameException {
        for (FullBuyer fb : listOfBuyers) {
            if (fb.getUser().getUserName().equals(user)) {
                if (fb.passwordCheck(password)) {
                    return fb;
                } else {
                    throw new InvalidPasswordException();
                }
            }
        }
        for (FullSeller fs : listOfSellers) {
            if (fs.getUser().getUserName().equals(user)) {
                if (fs.passwordCheck(password)) {
                    return fs;
                } else {
                    throw new InvalidPasswordException();
                }
            }
        }
        throw new IllegalUserNameException();
    }
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

    /**
     *
     * @param storeList list of stores to iterate through and organize based on each store's message received counter
     *
     * @return organized list of stores in order from most popular to least popular
     */
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
     * @param name name of the store being created
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

    /**
     * takes two arrays one of a user list and count and combines them to an organized string (Used by FullSeller)
     * @param users array of users
     * @param usersCount array of the corresponding number of messages recieved or sent
     * @param increasing decides whether or not the arrays are sorted in increasing order based off the counts
     * @return organized String of the user and its corresponding count all in order from highest to lowest
     */
    public static String correspondingArraysToString(FullUser[] users, Integer[] usersCount, boolean increasing) {
        if (increasing) {
            for (int i = 0; i < users.length; i++) {
                for (int j = i + 1; j < users.length; j++) {
                    FullUser tempUsr;
                    Integer tempInt;
                    if (usersCount[i] < usersCount[j]) {
                        tempUsr = users[i];
                        tempInt = usersCount[i];
                        users[i] = users[j];
                        usersCount[i] = usersCount[j];
                        users[j] = tempUsr;
                        usersCount[j] = tempInt;
                    }
                }
            }
        }
        String s = "| ";
        for (int i = 0; i < users.length; i++) {
            if (i == users.length - 1) {
                s += users[i].getUser().getUserName() + ": " + usersCount[i] + "|";
            } else {
                s += users[i].getUser().getUserName() + ": " + usersCount[i] + "\n";
            }
        }
        return s;
    }

    /**
     *  Takes two arrays one of a user list and count and combines them to an organized string (Used by FullBuyer)
     * @param stores list of stores
     * @param counts corresponding count to stores
     * @param increasing boolean to see whether to organize it by highest count stores to least
     * @return String of stores and their count value
     */
    public static String correspondingArrayToString(Store[] stores, Integer[] counts, boolean increasing) {
        if (increasing) {
            for (int i = 0; i < stores.length; i++) {
                for (int j = i + 1; j < stores.length; j++) {
                    Store tempStore;
                    Integer tempInt;
                    if (counts[i] < counts[j]) {
                        tempStore = stores[i];
                        tempInt = counts[i];
                        stores[i] = stores[j];
                        counts[i] = counts[j];
                        stores[j] = tempStore;
                        counts[j] = tempInt;
                    }
                }
            }
        }
        String s = "";
        for (int i = 0; i < stores.length; i++) {
            s += "(" + stores[i].getStoreName() + ") : " + counts[i] + " messages sent\n";
        }
        return s;
    }


    public static void main(String[] args) {
        Store store = new Store("A", new Seller("fads", "samsonates@gmail.com", "asdf"));
        System.out.println(listOfStores.get(0).getStoreName());
    }
}
