package UserCore;

public class FullSeller extends FullUser{

    public FullSeller (String username, String email, String pwd) {
        super(new Seller(username, email, pwd));
    }


}
