package UserCore;

/** IllegalStoreNameException
 *
 * throw this when a store is trying to be created but that name already exists
 * author: Samson Tesfagiorgis
 */

public class IllegalStoreNameException extends Exception {

    public IllegalStoreNameException(String s) {
        super(s);
    }

}
