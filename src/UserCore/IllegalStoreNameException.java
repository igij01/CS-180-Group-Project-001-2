package UserCore;

/**
 * IllegalStoreNameException
 * <p>
 * throw this when a store is trying to be created but that name already exists
 *
 * @author Samson Tesfagiorgis, 001
 * @version 11/11/2022
 */

public class IllegalStoreNameException extends IllegalArgumentException {

    public IllegalStoreNameException(String s) {
        super("store name " + s + " is already taken");
    }

}
