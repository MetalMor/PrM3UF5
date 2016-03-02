package exc;

import constants.ExceptionErrorConstants;

/**
 * Excepció per tractar problemes amb el nom d'un fitxer.
 * 
 * @version 080216
 * @author mor
 */
public class InvalidFileNameException extends Exception implements ExceptionInterface {

    private static String error = ExceptionErrorConstants.IFN_ERROR;
    
    /**
     * Nova instància de <code>KeyErrorException</code>.
     */
    public InvalidFileNameException() {
        super(error);
    }
    
    @Override
    public String getError() {
        return error;
    }
    
}
