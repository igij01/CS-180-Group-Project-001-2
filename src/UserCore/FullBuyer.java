package UserCore;

import java.util.ArrayList;
import java.util.Objects;

public class FullBuyer extends FullUser {

    private static ArrayList<Store> storesMessaged = new ArrayList<>();
    private static ArrayList<Integer> timesStoresMessaged = new ArrayList<>();
    /**
     * Creates a new Full Buyer instance
     *
     * @param userName the name of the user
     * @param email    the email address assoc. with the user
     * @param pwd      the password of the user
     * @throws EmailFormatException     when email address are not put in the right format
     * @throws IllegalUserNameException when the passed in username is already taken
     */
    public FullBuyer(String userName, String email, String pwd) {
        super(new Buyer(userName, email, pwd));
        PublicInformation.addListOfBuyers(this);
    }

    public void messageStore(Store store, String content) {
        super.createMessage(Objects.requireNonNull(PublicInformation.findFullSellerFromStore(store)), content);
        store.incrementCounter();

        if (!storesMessaged.contains(store)) {
            storesMessaged.add(store);
            for (int i = 0; i < storesMessaged.size(); i++) {
                if (storesMessaged.get(i).equals(store)) {
                    timesStoresMessaged.set(i, 1);
                }
            }
        }

        for (int i = 0; i < storesMessaged.size(); i++) {
            if (storesMessaged.get(i).equals(store)) {
                timesStoresMessaged.set(i, timesStoresMessaged.get(i) + 1);
            }
        }

    }

    public void messageSeller(FullSeller seller, String content) {
        super.createMessage(seller, content);
    }


    public void viewDashboard() {

       Store[] mostPopStores = new Store[PublicInformation.listOfStores.size()];
       int count = 0;
       for (Store s : PublicInformation.listOfStores) {
           mostPopStores[count] = s;
           count++;
       }
        //Sorts Stores in terms of most Messaged received
       for (int i = 0; i < mostPopStores.length; i++) {
           for (int j = i + 1; j < mostPopStores.length; j++) {
                Store temp;
                if (mostPopStores[i].getCounter() < mostPopStores[j].getCounter()) {
                    temp = mostPopStores[i];
                    mostPopStores[i] = mostPopStores[j];
                    mostPopStores[j] = temp;
                }
           }
       }

       Store[] mostMessagedStores = new Store[storesMessaged.size()];
       count = 0;
       for (Store s : storesMessaged) {
           mostMessagedStores[count] = s;
           count++;
       }
       //Sorts Stores in terms of most messaged by this buyer
       for (int i = 0; i < mostMessagedStores.length; i++) {
           for (int j = i + 1; j < mostMessagedStores.length; j++) {
               Store temp;
               if (timesStoresMessaged.get(i) < timesStoresMessaged.get(j)) {
                   temp = mostMessagedStores[i];
                   mostMessagedStores[i] = mostMessagedStores[j];
                   mostMessagedStores[j] = temp;
               }
           }
       }
    }


}
