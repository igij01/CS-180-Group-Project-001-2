import MessageCore.IllegalTargetException;
import MessageCore.IllegalUserAccessException;
import UserCore.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static UserCore.PublicInformation.*;

/**
 * Main
 * <p>
 * main class that run the market messaging system
 *
 * @author Samson Tesfagiorgis, Yulin Lin, Teresa Wan, Arthur Ruano, Alexander Roth, 001
 * @version 11/14/2022
 */
public class Main {
    public static void main(String[] args) {
//        if (!(args != null && args.length > 0 && args[0].equalsIgnoreCase("debug")))
//            PublicInformation.init();
        PublicInformation.serialize();
        PublicInformation.serialize();
        PublicInformation.serialize();
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to the market messaging system");
        FullUser user = mainLogin(scan);
        if (user.newMessage())
            System.out.println("You have an unread message!");
        int decision = 0;
        do {
            decision = mainDash(scan);
        } while (mainDecision(scan, decision, user));
        System.out.println("Thank you for using the market messaging system");
        user.logout();
        if (!(args != null && args.length > 0 && args[0].equalsIgnoreCase("debug")))
            PublicInformation.serialize();
    }

    public static boolean mainDecision(Scanner scan, int decision, FullUser user) {
        switch (decision) {
            case 1:
                case1(scan, user);
                return true;
            case 2:
                return case2(scan, user);
            case 3:
                return case3(scan, user);
            case 4:
                if (user instanceof FullBuyer) {
                    return case4(scan, (FullBuyer) user);
                } else {
                    return case4(scan, (FullSeller) user);
                }
            case 5:
                String input;
                if (user.waitingToBeDeletedStatus()) {
                    System.out.println("You will lost your account if you log out!");
                    System.out.println("Are you sure you want to continue?");
                    input = scan.nextLine();
                    return (!((input.equalsIgnoreCase("y")) || input.equalsIgnoreCase("yes")));
                }
                return false;
        }
        return true;
    }

    public static void case1(Scanner scan, FullUser user) {
        boolean repeat;
        do {
            System.out.println(user.toString());
            repeat = true;
            System.out.println("1. Change username");
            System.out.println("2. Change email");
            System.out.println("3. Block users");
            System.out.println("4. Unblock users");
            System.out.println("5. Make yourself invisible to users");
            System.out.println("6. Unmake yourself invisible to users");
            System.out.println("7. Add a word to be filtered");
            System.out.println("8. Unblock a word");
            System.out.println("9. Change the current censor pattern");
            System.out.println("10. Change filtering mode");
            System.out.println("11. Delete account");
            System.out.println("12. Recover Account");
            System.out.println("13. Go back");
            boolean loop = false;
            int choice = 0;
            do {
                loop = false;
                try {
                    choice = Integer.parseInt(scan.nextLine());
                    if (choice < 1 || choice > 13) {
                        System.out.println("Please choose a number between 1 and 13");
                        loop = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a number");
                    loop = true;
                }
            } while (loop);
            switch (choice) {
                case 1:
                    do {
                        loop = false;
                        System.out.println("Please enter the new username");
                        String newUsername = scan.nextLine();
                        System.out.println("Please enter the password to confirm your action");
                        String password = scan.nextLine();
                        try {
                            user.changeUsername(newUsername, password);
                        } catch (InvalidPasswordException | IllegalUserNameException e) {
                            System.out.println(e.getMessage());
                            loop = tryAgain(scan);
                        }
                    } while (loop);
                    break;
                case 2:
                    do {
                        loop = false;
                        System.out.println("Please enter the new email");
                        String newEmail = scan.nextLine();
                        System.out.println("Please enter the password to confirm your action");
                        String password = scan.nextLine();
                        try {
                            user.changeEmail(newEmail, password);
                        } catch (InvalidPasswordException | EmailFormatException e) {
                            System.out.println(e.getMessage());
                            loop = tryAgain(scan);
                        }
                    } while (loop);
                    break;
                case 3:
                    FullUser blockUser = null;
                    do {
                        loop = false;
                        System.out.println("Please enter the name of the buyer/seller to block");
                        String blockUsername = scan.nextLine();
                        if (user instanceof FullSeller) {
                            if ((blockUser = PublicInformation.findBuyer(blockUsername, (FullSeller) user)) == null) {
                                System.out.println("The buyer name cannot be found");
                                loop = tryAgain(scan);
                            }
                        } else if (user instanceof FullBuyer) {
                            if ((blockUser = PublicInformation.findSeller(blockUsername, (FullBuyer) user)) == null) {
                                System.out.println("The seller name cannot be found");
                                loop = tryAgain(scan);
                            }
                        }
                    } while (loop);
                    if (blockUser != null)
                        user.block(blockUser);
                    break;
                case 4:
                    FullUser unBlockUser = null;
                    do {
                        loop = false;
                        System.out.println("Please enter the name of the buyer/seller to unblock");
                        String unBlockUsername = scan.nextLine();
                        if (user instanceof FullSeller) {
                            if ((unBlockUser = PublicInformation.findBuyer(unBlockUsername, (FullSeller) user)) == null) {
                                System.out.println("The buyer name cannot be found");
                                loop = tryAgain(scan);
                            }
                        } else if (user instanceof FullBuyer) {
                            if ((unBlockUser = PublicInformation.findSeller(unBlockUsername, (FullBuyer) user)) == null) {
                                System.out.println("The seller name cannot be found");
                                loop = tryAgain(scan);
                            }
                        }
                    } while (loop);
                    if (unBlockUser != null)
                        if (!user.unblock(unBlockUser))
                            System.out.println("The user is not blocked by you!");
                    break;
                case 5:
                    FullUser invisUser = null;
                    do {
                        loop = false;
                        System.out.println("Please enter the name of the buyer/seller to become invisible");
                        String invisUsername = scan.nextLine();
                        if (user instanceof FullSeller) {
                            if ((invisUser = PublicInformation.findBuyer(invisUsername, (FullSeller) user)) == null) {
                                System.out.println("The buyer name cannot be found");
                                loop = tryAgain(scan);
                            }
                        } else if (user instanceof FullBuyer) {
                            if ((invisUser = PublicInformation.findSeller(invisUsername, (FullBuyer) user)) == null) {
                                System.out.println("The seller name cannot be found");
                                loop = tryAgain(scan);
                            }
                        }
                    } while (loop);
                    if (invisUser != null)
                        user.makeInvisible(invisUser);
                    break;
                case 6:
                    FullUser unInvisUser = null;
                    do {
                        loop = false;
                        System.out.println("Please enter the name of the buyer/seller to become un-invisible");
                        String unInvisUsername = scan.nextLine();
                        if (user instanceof FullSeller) {
                            if ((unInvisUser = PublicInformation.findBuyer(unInvisUsername, (FullSeller) user)) == null) {
                                System.out.println("The buyer name cannot be found");
                                loop = tryAgain(scan);
                            }
                        } else if (user instanceof FullBuyer) {
                            if ((unInvisUser = PublicInformation.findSeller(unInvisUsername, (FullBuyer) user)) == null) {
                                System.out.println("The seller name cannot be found");
                                loop = tryAgain(scan);
                            }
                        }
                    } while (loop);
                    if (unInvisUser != null)
                        if (!user.unInvisible(unInvisUser))
                            System.out.println("You did not become invisible to this user");
                    break;
                case 7:
                    String filterWord;
                    do {
                        loop = false;
                        System.out.println("Please enter a word to be filtered");
                        filterWord = scan.nextLine();
                        if (filterWord.matches("[^\\w']+")) {
                            System.out.println("contains characters that are not used in a word");
                            filterWord = null;
                            loop = tryAgain(scan);
                        }
                    } while (loop);
                    if (filterWord != null)
                        user.addFilterWord(filterWord);
                    break;
                case 8:
                    String unFilterWord;
                    do {
                        loop = false;
                        System.out.println("Please enter a word to be un-filtered");
                        unFilterWord = scan.nextLine();
                        if (unFilterWord.matches("[^\\w']+")) {
                            System.out.println("contains characters that are not used in a word");
                            unFilterWord = null;
                            loop = tryAgain(scan);
                        }
                    } while (loop);
                    if (unFilterWord != null)
                        if (!user.removeFilteredWord(unFilterWord))
                            System.out.println("The word was not filtered");
                    break;
                case 9:
                    char censoredChar = (char) -1;
                    do {
                        loop = false;
                        System.out.println("Please enter a character to be used as censored pattern");
                        String str = scan.nextLine();
                        if (str.length() == 1)
                            censoredChar = str.charAt(0);
                        else {
                            System.out.println("Not a valid character");
                            loop = tryAgain(scan);
                        }
                    } while (loop);
                    if (censoredChar != (char) -1)
                        user.changeReplacedChar(censoredChar);
                    break;
                case 10:
                    do {
                        System.out.println("Please choose the new filtering mode");
                        System.out.println("1. Turn off filter");
                        System.out.println("2. Turn on filter");
                        try {
                            loop = false;
                            int select = Integer.parseInt(scan.nextLine());
                            if (select == 1)
                                user.changeFilteringMode(true);
                            else if (select == 2)
                                user.changeFilteringMode(false);
                            else {
                                System.out.println("Please enter 1 or 2");
                                loop = true;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a number");
                            loop = true;
                        }
                    } while (loop);
                    break;
                case 11:
                    System.out.println("You are about to delete your account!");
                    System.out.println("Recovery is only possible before you logout");
                    System.out.println("Please enter your password to confirm this action");
                    String pass = scan.nextLine();
                    try {
                        PublicInformation.deleteAccount(user, pass);
                    } catch (InvalidPasswordException e) {
                        System.out.println("Wrong Password!");
                    }
                    break;
                case 12:
                    if (PublicInformation.recoverAccount(user)) {
                        System.out.println("Glad you are back!");
                    } else {
                        System.out.println("You never try to delete your account!");
                    }
                case 13:
                    repeat = false;
                    break;
            }
        }
        while (repeat);
    }

    public static boolean tryAgain(Scanner scan) {
        do {
            System.out.println("Do you want to try again?(y/n)");
            String choice = scan.nextLine();
            if (choice.equalsIgnoreCase("yes") || choice.equalsIgnoreCase("y"))
                return true;
            else if (choice.equalsIgnoreCase("no") || choice.equalsIgnoreCase("n"))
                return false;
        } while (true);
    }

    public static boolean case4(Scanner scan, FullBuyer buyer) {
        int list;
        do {
            System.out.println("1.search sellers");
            System.out.println("2.Print list of Stores");
            System.out.println("3.Print Buyer Dashboard");
            System.out.println("4.Back");
            try {
                list = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Enter a number");
                return case4(scan, buyer);
            }
        } while (list > 4 || list < 1);
        if (list == 1) {
            System.out.println("Please enter search text");
            String searchText = scan.nextLine();
            System.out.println(PublicInformation.findSellerBasedOnLetters(searchText, buyer));
            case4(scan, buyer);
        } else if (list == 2) {
            if (storeList(buyer) == null) {
                System.out.println("There are no stores!");
                return case4(scan, buyer);
            }
            System.out.println(storeList(buyer));
            System.out.println();
            System.out.println("Please put in the name of the store you want to message");
            String storeName = scan.nextLine();
            Store store;
            if ((store = PublicInformation.getStore(storeName)) != null) {
                System.out.println("The store owner for the store " + store.getStoreName() + " is " +
                        User.userName(store.getOwner()));
                System.out.println("Do you want to send a string message or a file");
                System.out.println("1 for string    2 for txt file");
                String selection = scan.nextLine();
                if (selection.equals("1")) {
                    System.out.println("What would you like to send them?");
                    String newMessage = scan.nextLine();
                    try {
                        buyer.messageStore(store, newMessage);
                    } catch (IllegalTargetException e) {
                        System.out.println("You must message a user of a different role");
                        return case4(scan, buyer);
                    }
                } else if (selection.equals("2")) {
                    System.out.println("Please put in the file path");
                    File txtFile = new File(scan.nextLine());
                    try {
                        buyer.messageStore(store, txtFile);
                    } catch (IOException e) {
                        System.out.println("Something went wrong when trying to read the file");
                        return case4(scan, buyer);
                    } catch (IllegalTargetException e) {
                        System.out.println("You must message a user of a different role");
                        return case4(scan, buyer);
                    }
                }
            } else {
                System.out.println("Please put in a valid store name");
            }
            case4(scan, buyer);
        } else if (list == 3) {
            boolean repeat;
            do {
                repeat = false;
                System.out.println("Do you want to view dashboard in increasing order?");
                String choice = scan.nextLine();
                if (choice.equalsIgnoreCase("yes") || choice.equalsIgnoreCase("y"))
                    System.out.println(buyer.viewDashboard(true));
                else if (choice.equalsIgnoreCase("no") || choice.equalsIgnoreCase("n"))
                    System.out.println(buyer.viewDashboard(false));
                else {
                    System.out.println("Please enter yes or no");
                    repeat = true;
                }
            } while (repeat);
        }
        return true;
    }

    public static boolean case4(Scanner scan, FullSeller seller) {
        int list;
        do {
            System.out.println("1.Print list of buyers");
            System.out.println("2.Search buyer");
            System.out.println("3.Print Dashboard");
            System.out.println("4.Create store");
            System.out.println("5.Back");
            try {
                list = scan.nextInt();
                scan.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Enter a number");
                return case4(scan, seller);
            }
        } while (list > 5 || list < 1);
        if (list == 1) {
            System.out.println(buyerList(seller));
            System.out.println();
        } else if (list == 2) {
            System.out.println("Please enter search text");
            String searchText = scan.nextLine();
            System.out.println(PublicInformation.findBuyerBasedOnLetters(searchText, seller));
            System.out.println();
            case4(scan, seller);
        } else if (list == 3) {
            boolean repeat;
            do {
                repeat = false;
                System.out.println("Do you want to view dashboard in increasing order?");
                String choice = scan.nextLine();
                if (choice.equalsIgnoreCase("yes") || choice.equalsIgnoreCase("y"))
                    System.out.println(seller.viewDashboard(true));
                else if (choice.equalsIgnoreCase("no") || choice.equalsIgnoreCase("n"))
                    System.out.println(seller.viewDashboard(false));
                else {
                    System.out.println("Please enter yes or no");
                    repeat = true;
                }
            } while (repeat);
        } else if (list == 4) {
            boolean repeat;
            do {
                repeat = false;
                System.out.println("Please enter the name for your store");
                String storeName = scan.nextLine();
                try {
                    seller.createStore(storeName);
                } catch (IllegalStoreNameException e) {
                    System.out.println(e.getMessage());
                    repeat = tryAgain(scan);
                }
            } while (repeat);
        }
        return true;
    }

    public static boolean case3(Scanner scan, FullUser user) {
        int message;
        FullUser receiver = null;
        do {
            System.out.println("1.Create new message");
            System.out.println("2.Remove message");
            System.out.println("3.Edit message");
            System.out.println("4.Back");
            try {
                message = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Enter a number");
                return case3(scan, user);
            }
        } while (message > 4 || message < 1);
        if (message == 1) {
            System.out.println("Who would you like to message?");
            String username = scan.nextLine();
            receiver = findUser(username, user);
            if (receiver == null) {
                System.out.println("No such user exists");
                System.out.println("Please navigate to list to search for username to message");
                return case3(scan, user);
            }
            System.out.println("Do you want to send a string message or a file");
            System.out.println("1 for string    2 for txt file");
            String selection = scan.nextLine();
            if (selection.equals("1")) {
                System.out.println("What would you like to send them?");
                String newMessage = scan.nextLine();
                try {
                    user.createMessage(receiver, newMessage);
                } catch (IllegalTargetException e) {
                    System.out.println("You must message a user of a different role");
                    return case3(scan, user);
                }
            } else if (selection.equals("2")) {
                System.out.println("Please put in the file path");
                File txtFile = new File(scan.nextLine());
                try {
                    user.createMessage(receiver, txtFile);
                } catch (IOException e) {
                    System.out.println("Something went wrong when trying to read the file");
                    return case3(scan, user);
                } catch (IllegalTargetException e) {
                    System.out.println("You must message a user of a different role");
                    return case3(scan, user);
                }
            }
            return true;
        } else if (message == 2) {
            System.out.println("What is the conversation number?");
            int conIndex = scan.nextInt();
            scan.nextLine();
            System.out.println("What is the message number?");
            int mesIndex = scan.nextInt();
            scan.nextLine();
            try {
                user.deleteMessage(conIndex, mesIndex);
            } catch (IllegalUserAccessException e) {
                System.out.println("You are not authorized to delete this message");
                return case3(scan, user);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("The entered index does not exist");
                System.out.println("Please navigate to Mail to see list of conversations and messages");
                return case3(scan, user);
            }
            return true;
        } else if (message == 3) {
            System.out.println("What is the conversation index?");
            int conIndex = scan.nextInt();
            scan.nextLine();
            System.out.println("What is the message index?");
            int mesIndex = scan.nextInt();
            scan.nextLine();
            System.out.println("What would you like to replace the message with?");
            String replace = scan.nextLine();
            try {
                user.editMessage(conIndex, mesIndex, replace);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("The conversation or message index does not exist");
                System.out.println("Please navigate to Mail to see list of conversations and messages");
                return case3(scan, user);
            } catch (IllegalUserAccessException e) {
                System.out.println("You don't have permission to edit this message");
                return case3(scan, user);
            }
            return true;
        } else {
            return true;
        }
    }


    public static boolean case2(Scanner scan, FullUser user) {
        if (user.printConversationTitles() == null || user.printConversationTitles().isBlank()) {
            System.out.println("You got no conversation!");
            System.out.println("Go message some one");
            return true;
        }
        System.out.println(user.printConversationTitles());
        System.out.println("Enter conversation index");
        System.out.println("Enter -1 to go back");
        System.out.println("Enter -2 to go to export mode");
        int conversation;
        try {
            conversation = Integer.parseInt(scan.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number");
            return mainDecision(scan, 2, user);
        }
        if (conversation == -2) {
            ArrayList<Integer> indexes = new ArrayList<>();
            System.out.println("Please specify the file path");
            File file = new File(scan.nextLine());
            int i;
            do {
                System.out.println("Enter conversation index for export");
                System.out.println("Enter -1 to stop");
                try {
                    i = Integer.parseInt(scan.nextLine());
                    if (i >= 0)
                        indexes.add(i);
                } catch (NumberFormatException e) {
                    System.out.println("Please put in a number");
                    i = -2;
                }
            } while (i != -1);
            try {
                if (indexes.isEmpty())
                    System.out.println("You did not put in any indexes!");
                else {
                    user.printConversationInCSV(indexes, file);
                    System.out.printf("Successfully written to %s\n", file);
                }
            } catch (IOException e) {
                System.out.println("something went wrong when trying to write to the file");
                return mainDecision(scan, 2, user);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("list contain Invalid index. please try again");
                return mainDecision(scan, 2, user);
            }
        } else if (conversation != -1) {
            try {
                System.out.println(user.printConversation(conversation));
                System.out.println("Do you want to save this conversation in csv?");
                String choice = scan.nextLine();
                if (choice.equalsIgnoreCase("yes") || choice.equalsIgnoreCase("y")) {
                    System.out.println("Please specify the file path");
                    File file = new File(scan.nextLine());
                    try {
                        user.printConversationInCSV(conversation, file);
                    } catch (IOException e) {
                        System.out.println("Something went wrong when trying to write to this file");
                        return case2(scan, user);
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid index please try again");
                return mainDecision(scan, 2, user);
            }
        }
        return true;
    }


    public static int mainDash(Scanner scan) {
        int dashAction = 0;
        do {
            System.out.println("1.Profile");
            System.out.println("2.Mail");
            System.out.println("3.Message");
            System.out.println("4.List");
            System.out.println("5.Logout");
            try {
                dashAction = scan.nextInt();
                scan.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("please enter either 1, 2, 3, 4, or 5");
                dashAction = 0;
            }
        } while (dashAction < 1 || dashAction > 5);
        return dashAction;
    }

    public static FullUser mainLogin(Scanner scan) {
        boolean loginLoop;
        int register = 0;
        FullUser fullUser = null;
        do {
            loginLoop = true;
            System.out.println("Please enter username.");
            String username = scan.nextLine();
            System.out.println("Please enter password.");
            String password = scan.nextLine();
            try {
                fullUser = login(username, password);
                loginLoop = false;
                System.out.println("Login successful!");
            } catch (IllegalUserNameException e) {
                System.out.println("Invalid Username.");
            } catch (InvalidPasswordException e) {
                System.out.println("Invalid password.");
            }
            if (loginLoop) {
                do {
                    System.out.println("Would you like to register? Or try again?");
                    System.out.println("1.Register");
                    System.out.println("2.Again");
                    try {
                        register = Integer.parseInt(scan.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Please input a number");
                    }
                } while (register != 1 && register != 2);
                if (register == 1) {
                    loginLoop = false;
                    return mainRegister(scan);
                }
            }
        } while (loginLoop);
        return fullUser;
    }

    public static FullUser mainRegister(Scanner scan) {
        String username = "start";
        do {
            if (!username.equals("start")) {
                System.out.println("This username is unavailable");
            }
            System.out.println("Enter desired Username");
            username = scan.nextLine();
        } while (listOfUsersNames.contains(username));
        do {
            System.out.println("Enter desired Email");
            String email = scan.nextLine();
            System.out.println("Enter desired password");
            String password = scan.nextLine();
            boolean repeat;
            try {
                do {
                    repeat = false;
                    System.out.println("enter \"buyer\" or \"seller\"");
                    String role = scan.nextLine();
                    if (role.equalsIgnoreCase("buyer")) {
                        System.out.printf("Username: %s\temail: %s\tpassword: %s\trole: Buyer\n",
                                username, email, password);
                        System.out.println("Confirm?(y/n)");
                        String decision = scan.nextLine();
                        if (decision.equalsIgnoreCase("y") || decision.equalsIgnoreCase("yes"))
                            return new FullBuyer(username, email, password);
                    } else if (role.equalsIgnoreCase("seller")) {
                        System.out.printf("Username: %s\temail: %s\tpassword: %s\trole: Seller\n",
                                username, email, password);
                        System.out.println("Confirm?(y/n)");
                        String decision = scan.nextLine();
                        if (decision.equalsIgnoreCase("y") || decision.equalsIgnoreCase("yes"))
                            return new FullSeller(username, email, password);
                    } else {
                        repeat = true;
                        System.out.println("Please enter buyer or seller!");
                    }
                } while (repeat);
            } catch (EmailFormatException e) {
                System.out.println("Please enter a valid email!");
            }
        } while (true);
    }


}
