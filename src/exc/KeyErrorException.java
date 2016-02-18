package exc;

/**
 * Classe que hereta d'Exception per tractar errors de recuperació dels valors
 * de cada tecla.
 *
 * 010216
 * @author mor
 */
public class KeyErrorException extends Exception {

    private static String error = "No s'ha pogut recuperar el valor de la tecla";
    
    /**
     * Nova instància de <code>KeyErrorException</code>.
     */
    public KeyErrorException() {
        super(error);
    }
    
    public String getError() {
        return error;
    }
    
}
