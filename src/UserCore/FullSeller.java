package UserCore;

import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;

public class FullSeller extends FullUser {

    public FullSeller (String username, String email, String pwd) {
        super(new Seller(username, email, pwd));
        PublicInformation.addListOfSellers(this);
    }
    public void viewDashboard() {

    }

}
