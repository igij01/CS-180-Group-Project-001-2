package UserCore;

/**
 * IllegalStoreNameException
 * <p>
 * throw this when a store is trying to be created but that name already exists
 * author: Samson Tesfagiorgis
 */

public class IllegalStoreNameException extends IllegalArgumentException {

    public IllegalStoreNameException(String s) {
        super("store name " + s + " is already taken");
    }

}
