package constants;

/**
 * Classe per agrupar les constants necessàries per al funcionament del 
 * projecte.
 * 
 *
 * 030216
 * @author mor
 */
public class Constants {
    
    /**
     * Amplada de la finestra.
     */
    public static final int W_WIDTH = 1200;
    
    /**
     * Alçada de la finestra.
     */
    public static final int W_HEIGHT = 1000;
    
    /**
     * Títol de la finestra del programa.
     */
    public static final String TITLE = "FXylophone!";
    
    /**
     * Nom del fitxer que guarda la vista.
     */
    public static final String VIEW_FILE = "FXylophoneView.fxml";
    
    /**
     * Nom del fitxer d'estils. S'ha deixat d'utilitzar perquè el fitxer 
     * d'estils s'inclou a la vista
     */
    public static final String STYLESHEET = "style.css";
    
    /**
     * Text per mostrar al TextField a on introduir el nom del fitxer XML per
     * guardar cadascun dels objectes Note.
     */
    public static final String TF_FILE_PROMPT = "Fitxer...";
    
    /**
     * Nom per defecte del fitxer XML a on guardar els objectes Note.
     */
    public static final String DEFAULT_FILENAME = "newMidiTrack";
    
    /**
     * Constant que representa una cadena buida. És una pijeria utilitzar-la,
     * pero queda més bonic i elegant que fer hardcode de cometes sense res.
     */
    public static final String VOID_STRING = "";
    
    /**
     * Extensió per al fitxer que guarda la grabació.
     */
    public static final String EXT = ".xml";
   
    /**
     * Element arrel del fitxer xml.
     */
    public static final String SOUNDFILE_ELEMENT = "SoundFile";
    
    /**
     * Element corresponent a un objecte de classe Note.
     */
    public static final String NOTE_ELEMENT = "Note";
    
    /**
     * Element corresponent a l'atribut value d'un objecte de classe 
     * Note.
     */
    public static final String VALUE_ELEMENT = "Value";
    
    /**
     * Element corresponent a l'atribut timestamp d'un objecte de classe
     * Note.
     */
    public static final String TIMESTAMP_ELEMENT = "Timestamp";
    
    /**
     * Temps en milisegons del moment de l'execució del programa.
     */
    public static final long CURRENT_TIME_MILLIS = System.currentTimeMillis();
    
    /**
     * Codi integer per obtenir l'instrument del soundbank. No sembla que 
     * funcioni, sempre sona el p*** piano.
     */
    public static final int INSTRUMENT = 13; // xilòfon -> 13
    
    /**
     * To de les notes.
     */
    public static final int NOTE_VALUE = 85;
    
    /**
     * Volum de les notes.
     */
    public static final int NOTE_VOLUME = 300;
}
