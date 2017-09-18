package Exception;

/**
 * Created by mgoo on 11/02/17.
 */
public class MethodNotWhitelistedException extends Exception {
    public MethodNotWhitelistedException(String message){
        super("The Method has not being whitelisted: "+message);
    }
}
