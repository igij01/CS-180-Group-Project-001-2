package Server;

import UserCore.*;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class TempUpdate {
    // update this field everytime you update the field of the class
    // put in a random number or just increment the number
    @Serial
    private static final long serialVersionUID = 2L;
    private static final File filePath = new File("src/Server/ServerTempStorage");

    private static byte[] bytesRemaining;

    static {
        try {
            bytesRemaining = Files.readAllBytes(filePath.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<TempUpdate> history = new ArrayList<>();

    private LocalDateTime time;

    private ArrayList<Store> listOfNewStores;
    private ArrayList<String> listOfNewUsersNames;
    private ArrayList<FullSeller> listOfNewSellers;
    private ArrayList<FullBuyer> listOfNewBuyers;

    public TempUpdate() {
        listOfNewStores = new ArrayList<>();
        listOfNewUsersNames = new ArrayList<>(PublicInformation.listOfUsersNames);
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
    }

    public void addNewUsername(String name) {
        listOfNewUsersNames.add(name);
    }

    public void replaceUsername(String oldUsername, String newUsername) {
        listOfNewUsersNames.remove(oldUsername);
        listOfNewUsersNames.add(newUsername);
    }

    public void updateStoreList(Store store) {
        listOfNewStores.add(store);
    }

    public void saveToFile() {
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

    public static void readFile() {
        try (ByteArrayInputStream bin = new ByteArrayInputStream(bytesRemaining);
                ObjectInputStream ois = new ObjectInputStream(bin)) {
            Object obj = ois.readObject();
            history.add((TempUpdate) obj);
            bytesRemaining = bin.readAllBytes();
            if (bytesRemaining.length == 0)
                return;
            readFile();
        } catch (EOFException ignored) {
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
