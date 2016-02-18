package exc;

/**
 * Classe que hereta d'Exception per controlar errors amb la transferència
 * d'objectes Note del programa a un fitxer XML i viceversa.
 *
 * 020216
 * @author mor
 */
public class WrongNoteException extends Exception {

    private static String error = "Format inadequat del missatge MIDI XML.";
    
    /**
     * Nova instància de <code>WrongMessageException</code>.
     */
    public WrongNoteException() {
        super(error);
    }
    
    public String getError() {
        return error;
    }
    
}
