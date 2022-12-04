package Server;

import UserCore.FullBuyer;
import UserCore.FullSeller;
import UserCore.FullUser;
import UserCore.Store;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class TempUpdate {
    // update this field everytime you update the field of the class
    // put in a random number or just increment the number
    @Serial
    private static final long serialVersionUID = 2L;
    private LocalDateTime time;

    private ArrayList<Store> listOfNewStores;
    private ArrayList<String> listOfNewUsersNames;
    private ArrayList<FullSeller> listOfNewSellers;
    private ArrayList<FullBuyer> listOfNewBuyers;

    public TempUpdate() {
        listOfNewStores = new ArrayList<>();
        listOfNewUsersNames = new ArrayList<>();
        listOfNewSellers = new ArrayList<>();
        listOfNewBuyers = new ArrayList<>();
        time = LocalDateTime.now(ZoneId.of("US/Eastern"));
    }

    public void updateUserList(FullUser user) {
        if (user instanceof FullBuyer) {
            listOfNewBuyers.add((FullBuyer) user);
        } else if (user instanceof FullSeller) {
            listOfNewSellers.add((FullSeller) user);
        } else
            return;
        listOfNewUsersNames.add(user.getUsername());
    }

    public void updateStoreList(Store store) {
        listOfNewStores.add(store);
    }

    public void saveToFile(String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath, true))) {
            time = LocalDateTime.now(ZoneId.of("US/Eastern"));
            oos.writeObject(listOfNewStores);
            oos.writeObject(listOfNewUsersNames);
            oos.writeObject(listOfNewSellers);
            oos.writeObject(listOfNewBuyers);
            oos.writeObject(time);
            listOfNewStores.clear();
            listOfNewUsersNames.clear();
            listOfNewSellers.clear();
            listOfNewBuyers.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<TempUpdate> readFile(String filePath) {
        ArrayList<TempUpdate> history = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            Object obj;
            while ((obj = ois.readObject()) != null) {
                history.add((TempUpdate) obj);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return history;
    }
}
