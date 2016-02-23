package constants;

//<editor-fold defaultstate="collapsed" desc="Imports.">
import constants.ApplicationConstants;
//</editor-fold>

/**
 * Conjunt de constants utilitzades per printar els errors de les diferents
 * excepcions personalitzades que utilitza el programa.
 *
 * @version 230216
 * @author mor
 */
public class ExceptionErrorConstants {
    
    //<editor-fold defaultstate="collapsed" desc="Cadenes d'error.">
    /**
     * Cadena d'error per a l'excepció <code>InvalidFileNameException</code>.
     */
    public static final String IFN_ERROR = "Nom del fitxer inadequat. Utilitzant el valor per defecte: " + ApplicationConstants.DEFAULT_FILENAME + ApplicationConstants.EXT;
    
    /**
     * Cadena d'error per a l'excepció <code>KeyErrorException</code>.
     */
    public static final String KE_ERROR = "No s'ha pogut recuperar el valor de la tecla, reproduint nota per defecte: " + ApplicationConstants.DEF_NOTE_VALUE;
    
    /**
     * Cadena d'error per a l'excepció <code>MissingKeyboardException</code>.
     */
    public static final String MK_ERROR = "No s'ha pogut carregar correctament el teclat del xilòfon.";
    
    /**
     * Cadena d'error per a l'excepció <code>WrongNoteException</code>.
     */
    public static final String WN_ERROR = "Format inadequat de l'objecte Note. Utilitzant el valor per defecte: " + ApplicationConstants.DEF_NOTE_VALUE;
//</editor-fold>
    
}
