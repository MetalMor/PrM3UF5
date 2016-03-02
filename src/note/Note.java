package note;

import constants.ApplicationConstants;
import constants.NoteConstants;
import java.util.HashMap;
import java.util.Map;

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
    
    /**
     * Estructura que conté les equivalències de notes i tecles.
     */
    private static Map<String, String> noteEqv;
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
     * @param o Objecte amb el qual comparar-se.
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
     * Retorna l'objecte en forma de <code>String</code> (per debugar). 
     *
     * @return Cadena amb les propietats de l'objecte llistades.
     */
    @Override
    public String toString() {
        String noteToString = noteEqv.get(this.getPlayedKey());
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
     * Funció que inicialitza l'estructura de tipus <code>Map</code> que conté 
     * les equivalències entre el valor de cada tecla al teclat virtual i la 
     * seva nota corresponent.
     */
    public static void initNoteMap() {
        
        Map<String, String> noteEqvTemp = new HashMap<>();
        
        noteEqvTemp.put(NoteConstants.DO_NUMB, NoteConstants.DO_STR);
        noteEqvTemp.put(NoteConstants.RE_NUMB, NoteConstants.RE_STR);
        noteEqvTemp.put(NoteConstants.MI_NUMB, NoteConstants.MI_STR);
        noteEqvTemp.put(NoteConstants.FA_NUMB, NoteConstants.FA_STR);
        noteEqvTemp.put(NoteConstants.SOL_NUMB, NoteConstants.SOL_STR);
        noteEqvTemp.put(NoteConstants.LA_NUMB, NoteConstants.LA_STR);
        noteEqvTemp.put(NoteConstants.SI_NUMB, NoteConstants.SI_STR);
        noteEqvTemp.put(NoteConstants.DO2_NUMB, NoteConstants.DO2_STR);
        noteEqvTemp.put(NoteConstants.RE2_NUMB, NoteConstants.RE2_STR);
        noteEqvTemp.put(NoteConstants.MI2_NUMB, NoteConstants.MI2_STR);
        noteEqvTemp.put(NoteConstants.FA2_NUMB, NoteConstants.FA2_STR);
        noteEqvTemp.put(NoteConstants.SOL2_NUMB, NoteConstants.SOL2_STR);
        
        setNoteEqv(noteEqvTemp);
        
    }
    
    /**
     * Funció per obtenir el moment en milisegons de la reproducció d'una nota.
     *
     * @return Valor long del temps en milisegons entre el moment de
     * l'execució del programa i la instància de l'objecte <code>Note</code> 
     * reproduït.
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
    
    /**
     * Funció per obtenir la clau <code>String</code> corresponent al teclat
     * a partir del valor numèric d'una nota.
     * 
     * @param value Valor enter de la nota.
     * @return Clau <code>String</code> de l'índex de la tecla corresponent.
     */
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
     * Retorna l'estructura d'equivalències de notes i tecles.
     * 
     * @return Estructura de tipus <code>Map</code>.
     */
    public Map<String, String> getNoteEqv() {
        return noteEqv;
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
    
    /**
     * Defineix l'estructura d'equivalències de notes i tecles.
     * 
     * @param noteEqv Estructura de tipus <code>Map</code>.
     */
    public static void setNoteEqv(Map<String, String> noteEqv) {
        Note.noteEqv = noteEqv;
    }
    //</editor-fold>
    
}
