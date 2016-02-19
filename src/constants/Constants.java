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
    
    //<editor-fold defaultstate="collapsed" desc="Valors necessaris per inicialitzar l'aplicació.">
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
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Constants per a l'ús de fitxers XML.">
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
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Misc.">
    /**
     * Volum de les notes.
     */
    public static final int VOLUME = 300;
    
    /**
     * Text per mostrar al TextField a on introduir el nom del fitxer XML per
     * guardar cadascun dels objectes Note.
     */
    public static final String TF_FILE_PROMPT = "Fitxer...";
    //</editor-fold>
    
}
