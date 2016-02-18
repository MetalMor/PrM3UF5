package exc;

/**
 *
 * 080216
 * @author mor
 */
public class InvalidFileNameException extends Exception {

    private static String error = "Nom del fitxer inadequat.";
    
    /**
     * Nova instància de <code>KeyErrorException</code>.
     */
    public InvalidFileNameException() {
        super(error);
    }
    
    public String getError() {
        return error;
    }
    
}