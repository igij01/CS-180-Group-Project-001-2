import MessageCore.IllegalTargetException;
import MessageCore.IllegalUserAccessException;
import UserCore.*;

import java.util.InputMismatchException;
import java.util.Scanner;

import static UserCore.PublicInformation.*;

public class Main {
    public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    int rl = 0;
    boolean repeat = false;
    do {
        repeat = false;
        System.out.println("1.Login");
        System.out.println("2.Register");
        try {
            rl = Integer.parseInt(scan.nextLine());
            if (rl != 1 && rl != 2) {
                System.out.println("Please choose between 1 or 2");
                repeat = true;
            }
        } catch (NumberFormatException e) {
            repeat = true;
            System.out.println("Please enter the number 1 or 2");
        }
    } while (repeat);
    FullUser user = mainLogin(scan);
    int decision = 0;
    do {
        decision = mainDash(scan);
    } while(mainDecision(scan, decision, user));

    }
    public static boolean mainDecision(Scanner scan, int decision, FullUser user) {
        switch (decision) {
            case 1:
                System.out.print(user.toString());
                return true;
            case 2:
                return case2(scan, user);
            case 3:
                return case3(scan,user);
            case 4:
                if (user instanceof FullBuyer) {
                    return case4(scan, (FullBuyer) user);
                } else {
                    return case4(scan, (FullSeller) user);
                }
            case 5:
                return false;
        }
        return true;
    }
    public static boolean case4(Scanner scan, FullBuyer buyer) {
        //only buyers can see sellers and store; and only sellers and see list of buyers
        // maybe write 2 methods, one take in a FullBuyer, one take in a full seller, for this part
        int list;
        do {
            System.out.println("1.Print list of Sellers");
            System.out.println("2.Print list of Stores");
            System.out.println("3.Back");
            try {
                list = scan.nextInt();
                scan.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Enter a number");
                return case4(scan, buyer);
            }
        } while (list > 3 || list < 1);
        if (list == 1) {
            System.out.println(sellerList(buyer));
        } else if (list == 2) {
            System.out.println(storeList(buyer));
        }
        return true;
    }

    public static boolean case4(Scanner scan, FullSeller seller) {
        int list;
        do {
            System.out.println("1.Print list of Sellers");
            System.out.println("2.Back");
            try {
                list = scan.nextInt();
                scan.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Enter a number");
                return case4(scan, seller);
            }
        } while (list > 2 || list < 1);
        if (list == 1) {
            System.out.println(buyerList(seller));
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
                message = scan.nextInt();
                scan.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Enter a number");
                return case3(scan, user);
            }
        } while (message > 4 || message < 1);
        if (message == 1) {
            System.out.println("Who would you like to message?");
            String username = scan.nextLine();
            try {
                receiver = findUser(username, user);
            } catch (IllegalUserNameException e) {
                System.out.println("No such user exists");
                return case3(scan, user);
            }
            System.out.println("What would you like to send them?");
            String newMessage = scan.nextLine();
            try {
                user.createMessage(receiver, newMessage);
            } catch (IllegalTargetException e) {
                System.out.println("You must message a user of a different role");
                return case3(scan, user);
            }
            return true;
        } else if (message == 2) {
            System.out.println("What is the other user's Name?");
            String username = scan.nextLine();
            try {
                receiver = findUser(username, user);
            } catch (IllegalUserNameException e) {
                System.out.println("No such user exists");
                return case3(scan, user);
            }
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
        System.out.println(user.printConversationTitles());
        System.out.println("Enter conversation index");
        System.out.println("Enter -1 to go back");
        int conversation;
        try {
            conversation = scan.nextInt();
            scan.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Please enter a number");
            return mainDecision(scan, 2, user);
        }
        if (conversation != -1) {
            try {
                user.printConversation(conversation);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid index please try again");
                return mainDecision(scan,2,user);
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
                    register = scan.nextInt();
                    scan.nextLine();
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
