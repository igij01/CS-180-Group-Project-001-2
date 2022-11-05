package UserCore;

import java.util.ArrayList;

public class PublicInformation {

    public static ArrayList<Store> listOfStores = new ArrayList<>();
    public static ArrayList<String> listOfUsers = new ArrayList<>();

    private PublicInformation() {}

    public static void addListOfStores(Store store) {
        listOfStores.add(store);
    }

    public static void addListOfUsers(String user) {
        listOfUsers.add(user);
    }


    public static void main(String[] args) {
        Store store = new Store("A", new Seller("fads", "samsonates@gmail.com", "asdf"));
        System.out.println(listOfStores.get(0).getStoreName());
    }
}
