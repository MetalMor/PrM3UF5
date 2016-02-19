package exc;

/**
 * Excepció per gestionar errors amb el teclat virtual.
 *
 * 010216
 * @author mor
 */
public class MissingKeyboardException extends Exception {

    private static String error = "No s'ha pogut carregar el teclat del xilòfon.";
    
    /**
     * Nova instància de <code>MissingKeyboardException</code>.
     */
    public MissingKeyboardException() {
        super(error);
    }
    
    public String getError() {
        return error;
    }
    
}
