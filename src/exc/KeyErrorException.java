package exc;

import constants.ExceptionErrorConstants;

/**
 * Excepció per tractar errors de recuperació dels valors de cada tecla.
 *
 * @version 010216
 * @author mor
 */
public class KeyErrorException extends Exception implements ExceptionInterface {

    private static String error = ExceptionErrorConstants.KE_ERROR;
    
    /**
     * Nova instància de <code>KeyErrorException</code>.
     */
    public KeyErrorException() {
        super(error);
    }
    
    @Override
    public String getError() {
        return error;
    }
    
}
