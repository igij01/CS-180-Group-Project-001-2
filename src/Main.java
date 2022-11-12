import MessageCore.IllegalUserAccessException;
import UserCore.*;

import java.sql.SQLOutput;
import java.util.Scanner;

import static UserCore.PublicInformation.listOfUsersNames;
import static UserCore.PublicInformation.login;

public class Main {
    public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    mainLogin(scan);



    }
    // boolean return value will come in handy with GUIs
    public static boolean mainLogin(Scanner scan) {
        boolean loginLoop = true;
        int register = 0;
        System.out.println("Please enter username.");
        String username = scan.nextLine();
        System.out.println("Please enter password.");
        String password = scan.nextLine();
        do {
            try {
                FullUser fullUser = login(username, password);
                System.out.println("Login successful!");
                loginLoop = false;
            } catch (IllegalUserAccessException e) {
                System.out.println("Invalid Username.");
            } catch (InvalidPasswordException e) {
                System.out.println("Invalid password.");
            }
            if (loginLoop) {
                do {
                    System.out.println("Would you like to register? Or try again?");
                    System.out.println("(1)Register");
                    System.out.println("(2)Again");
                    register = scan.nextInt();
                    scan.nextLine();
                } while (register != 1 && register != 2);
                if (register == 1) {
                    return mainRegister(scan);
                }
            }
        } while (loginLoop);
        return true;
    }
    // boolean return value will come in handy with GUIs
    public static boolean mainRegister(Scanner scan) {
        String username = "start";
        do {
            if (!username.equals("start")) {
                System.out.println("This username is unavailable");
            }
            System.out.println("Enter desired Username");
            username = scan.nextLine();
        } while (listOfUsersNames.contains(username));
        System.out.println("Enter desired Email");
        String email = scan.nextLine();
        System.out.println("Enter desired password");
        String password = scan.nextLine();
        System.out.println("enter \"buyer\" or \"seller\"");
        String role = scan.nextLine();
        if (role.equalsIgnoreCase("buyer")) {
            FullBuyer buyer = new FullBuyer(username, email, password);
        } else if (role.equalsIgnoreCase("seller")) {
            FullSeller seller = new FullSeller(username, email, password);
        }
        return true;
    }






}
