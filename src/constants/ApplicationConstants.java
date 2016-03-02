package constants;

/**
 * Classe per agrupar les constants útils per al funcionament del 
 * programa.
 * 
 *
 * @version 030216
 * @author mor
 */
public class ApplicationConstants {
    
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
    public static final String TITLE = "BANANA FXylophone!";
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Constants de la vista.">
    /**
     * Nom del fitxer que guarda la vista.
     */
    public static final String VIEW_FILE = "view/FXylophoneView.fxml";
    
    /**
     * Nom del fitxer que guarda l'animació del plàtan ballant.
     */
    public static final String PEANUT_BUTTER_JELLY_TIME_FILE = "file:view/peanut-butter-jelly-time.gif";
    
    /**
     * Nom del fitxer que guarda la icona de la barra de tasques/finestra.
     */
    public static final String ICON_FILE = "view/icon.gif";
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
     * Extensió per al fitxer que guarda la gravació.
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
    
    //<editor-fold defaultstate="collapsed" desc="Missatges als camps de text.">
    /**
     * Missatge mostrat quan l'aplicació està preparada per funcionar.
     */
    public static final String READY_MESSAGE = "Aplicació preparada.";
    
    /**
     * Missatge mostrat quan l'aplicació ha començat a gravar notes.
     */
    public static final String RECORDING_MESSAGE = "Inici de la gravació.";
    
    /**
     * Missatge mostrat quan l'aplicació ha acabat de gravar notes.
     */
    public static final String STOP_RECORDING_MESSAGE = "Fi de la gravació.";
    
    /**
     * Missatge mostrat quan l'aplicació ha començat a reproduir notes.
     */
    public static final String PLAYING_MESSAGE = "Inici de la reproducció.";
    
    /**
     * Missatge mostrat quan l'aplicació ha acabat de reproduir notes..
     */
    public static final String END_PLAYING_MESSAGE = "Fi de la reproducció.";
    
    /**
     * Text per mostrar al TextField a on introduir el nom del fitxer XML per
     * guardar cadascun dels objectes Note.
     */
    public static final String TF_FILE_PROMPT = "Fitxer...";
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Valors útils per al tractament d'objectes Note.">
    /**
     * Volum de les notes.
     */
    public static final int DEF_NOTE_VOLUME = 300;
    
    /**
     * To mínim per defecte de les notes.
     */
    public static final int DEF_NOTE_VALUE = 90;
    
    /**
     * Valor enter del canal de MIDI que reproduirà el so.
     */
    public static final int MIDICHANNEL = 10;
    
    //</editor-fold>
    
}