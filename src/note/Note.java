package note;

import constants.ApplicationConstants;

/**
 * Classe que representa una nota reproduïda pel programa.
 *
 * @version 020216
 * @author mor
 */
public abstract class Note implements Comparable {

    // PROPIETATS
    
    //<editor-fold defaultstate="collapsed" desc="Propietats.">
    /**
     * Valor integer de la nota reproduïda.
     */
    private int value;
    
    /**
     * Temps en milisegons des de la data UNIX fins a la reproducció de la nota.
     */
    private long timestamp;
    //</editor-fold>
    
    // MÈTODES
    
    //<editor-fold defaultstate="collapsed" desc="Constructors.">
    /**
     * Constructor predeterminat.
     * Inicialitza la propietat timestamp amb el temps en milisegons actual.
     *
     * @param value Valor enter de la nota.
     */
    public Note(int value) {
        this.value = value;
        this.timestamp = getPlayedTime(); // temps en milisegons des de l'execució del programa
    }
    
    /**
     * Constructor de notes reproduides.
     * Per instanciar objectes note guardades en un fitxer XML.
     *
     * @param value Valor enter de la nota.
     * @param timestamp Temps en milisegons de la reproducció de la nota.
     */
    public Note(int value, long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Overrides (Object).">
    /**
     * Retorna un valor enter negatiu, positiu o zero en funció de
     * l'ordre esperat de les notes.
     *
     * @param o Objecte amb el qual comparar-se
     * @return Valor negatiu si l'objecte en qüestió es troba abans que l'altre,
     * positiu en cas contrari i 0 si van a la mateixa posició (cosa impossible, 
     * per cert).
     */
    @Override
    public int compareTo(Object o) {
        
        Note other = (Note) o;
        
        if (this.timestamp < other.timestamp)
            return -1;
        else if (this.timestamp > other.timestamp)
            return 1;
        
        return 0;
        
    }
    
    /**
     * Retorna l'objecte en forma de String (per debugar). 
     *
     * @return Cadena amb les propietats de l'objecte llistades.
     */
    @Override
    public String toString() {
        String noteToString = new StringBuilder()
                .append("Note{\n\t" )
                .append("note=").append(getPlayedKey()).append("\n\t")
                .append("timestamp=").append(getTimestamp()/*getPlayedTime()*/)
                .append("\n}\n").toString();
        return noteToString;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Mètodes abstractes.">
    /**
     * Funció per obtenir el valor id corresponent al seu objecte del teclat
     * de la nota reproduïda.
     *
     * @return Valor int de la tecla premuda al teclat, associada amb la nota
     * en qüestió.
     */
    public abstract String getPlayedKey();
    
    /**
     * Funció per obtenir el moment en milisegons de la reproducció d'una nota.
     *
     * @return Valor long del temps en milisegons entre el moment de
     * l'execució del programa i la instància de l'objecte Note reproduït.
     */
    public abstract long getPlayedTime();
    
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Altres.">
    /**
     * Funció per obtenir el valor numèric d'una nota a partir de la clau
     * corresponent a la tecla del teclat virtual.
     *
     * @param key Índex de la tecla premuda.
     * @return Valor enter de la nota.
     */
    public static int noteValueFromKey(String key) {
        return 2*Integer.parseInt(key) + ApplicationConstants.DEF_NOTE_VALUE;
    }
    
    public static String noteKeyFromValue(int value) {
        return String.valueOf((value - ApplicationConstants.DEF_NOTE_VALUE)/2);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getters i setters">
    /**
     * Retorna el valor de la nota.
     *
     * @return Valor enter de la nota.
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Retorna el moment de la reproducció de la nota.
     *
     * @return Temps en milisegons de la reproducció de la nota.
     */
    public long getTimestamp() {
        return timestamp;
    }
    
    /**
     * Defineix el valor de la nota.
     *
     * @param value Valor enter de la nota.
     */
    public void setValue(int value) {
        this.value = value;
    }
    
    /**
     * Defineix el moment de reproducció de la nota.
     *
     * @param timestamp Temps en milisegons de la reproducció de la nota.
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    //</editor-fold>
    
}
