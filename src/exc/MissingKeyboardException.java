package exc;

import constants.ExceptionErrorConstants;

/**
 * Excepció per gestionar errors amb el teclat virtual.
 *
 * 010216
 * @author mor
 */
public class MissingKeyboardException extends Exception implements ExceptionInterface {

    private static String error = ExceptionErrorConstants.MK_ERROR;
    
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
