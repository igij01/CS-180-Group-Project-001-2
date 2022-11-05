package UserCore;

public class FullSeller extends FullUser{

    public FullSeller (String username, String email, String pwd) {
        super(username, email, pwd, Role.SELLER);
    }


}
