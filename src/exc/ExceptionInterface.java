package exc;

/**
 * Interfície que defineix els requeriments d'una excepció per aquesta 
 * aplicació.
 * 
 * @version 230216
 * @author mor
 */
public interface ExceptionInterface {
    
    /**
     * Retorna la cadena d'error de l'excepció.
     * 
     * @return Cadena (String) de l'error.
     */
    public String getError();
    
}
