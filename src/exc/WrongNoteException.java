package exc;

import constants.ExceptionErrorConstants;

/**
 * Excepció per controlar errors amb la transferència d'objectes Note del 
 * programa a un fitxer XML i al revés.
 *
 * @version 020216
 * @author mor
 */
public class WrongNoteException extends Exception implements ExceptionInterface {

    private static String error = ExceptionErrorConstants.WN_ERROR;
    
    /**
     * Nova instància de <code>WrongMessageException</code>.
     */
    public WrongNoteException() {
        super(error);
    }
    
    @Override
    public String getError() {
        return error;
    }
    
}
