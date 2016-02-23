package fxyl;

//<editor-fold defaultstate="collapsed" desc="Imports.">
import constants.Constants;
import exc.InvalidFileNameException;
import exc.KeyErrorException;
import exc.MissingKeyboardException;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import note.Note;
import note.NoteImpl;
import note.NoteList;
//</editor-fold>

/**
 * Classe controladora del xilòfon.
 * Realitza les tasques relacionades amb la comunicació amb l'usuari per al
 * tractament d'objectes de classe Note.
 *
 * @version 260116
 * @author mor
 */
public class FXylophoneController implements Initializable {
    
    // PROPIETATS
    
    //<editor-fold defaultstate="collapsed" desc="Objectes requerits per l'API MIDI.">
    /**
     * Objecte sintetitzador de MIDI.
     */
    private Synthesizer syn;
    /**
     * Array d'instruments MIDI extrets del Soundbank per defecte.
     */
    private Instrument[] instr;
    /**
     * Array de canals de reproducció de MIDI.
     */
    private MidiChannel[] mc;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Propietats del teclat virtual.">
    /**
     * Propietat que representa el teclat del xilòfon.
     * Cadascuna de les tecles del xilòfon és un objecte diferent que queda
     * recollit a l'estructura de dades keyboard de tipus ArrayList.
     */
    private List<Rectangle> keyboard = new ArrayList<>();
    /**
     * Primera tecla.
     */
    @FXML private Rectangle rect01;
    /**
     * Segona tecla.
     */
    @FXML private Rectangle rect02;
    /**
     * Tercera tecla.
     */
    @FXML private Rectangle rect03;
    /**
     * Quarta tecla.
     */
    @FXML private Rectangle rect04;
    /**
     * Cinquena tecla.
     */
    @FXML private Rectangle rect05;
    /**
     * Sisena tecla.
     */
    @FXML private Rectangle rect06;
    /**
     * Setena tecla.
     */
    @FXML private Rectangle rect07;
    /**
     * Vuitena tecla.
     */
    @FXML private Rectangle rect08;
    /**
     * Novena tecla.
     */
    @FXML private Rectangle rect09;
    /**
     * Desena tecla.
     */
    @FXML private Rectangle rect10;
    /**
     * Onzena tecla.
     */
    @FXML private Rectangle rect11;
    /**
     * Dotzena tecla.
     */
    @FXML private Rectangle rect12;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Propietats de control de grabació/reproducció.">
    /**
     * Objecte encarregat de gestionar la inserció i extracció d'objectes Note
     * a un fitxer XML.
     */
    private FXylophoneXML xmlNoteRecorder;
    /**
     * Estructura de dades a on guardar els objectes Note, preparats per ser
     * inserits a un fitxer XML.
     */
    private static NoteList<Note> noteList;
    /**
     * Propietat que allotja temporalment el valor de la nota per reproduir-la.
     */
    private int key;
    /**
     * Temps en milisegons que el programa esperarà entre una nota i una altra
     * quan estigui reproduint les dades d'un fitxer XML.
     */
    private long wait = 0;
    /**
     * Flag per indicar si el programa està grabant.
     */
    private boolean recording = false;
    /**
     * Moment de l'inici de la grabació.
     */
    private static long recordTime;
    /**
     * Flag per indicar si el programa està reproduint.
     */
    private boolean playing = false;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Objectes de la interfície de l'usuari.">
    /**
     * Botó per reproduir els objectes Note desats a un fitxer XML.
     */
    @FXML private Button play;
    /**
     * Botó per iniciar la inserció d'objectes Note a l'ArrayList que servirà
     * per desar les dades a un fitxer XML.
     */
    @FXML private Button record;
    /**
     * Botó per parar la grabació i guardar totes les dades de l'objecte
     * ArrayList a un fitxer XML.
     */
    @FXML private Button stopRecord;
    
    /**
     * Indicador de grabació, canvia de color quan l'aplicació està en el procés
     * de grabació de notes.
     */
    @FXML private Circle recordControl;
    
    /**
     * Camp de text a on s'introdueix el nom del fitxer XML per guardar i
     * reproduir les notes.
     */
    @FXML private TextField fileNameTF;
    //</editor-fold>
    
    // MÈTODES
    
    //<editor-fold defaultstate="collapsed" desc="Inicialització del controlador i la interfície de l'usuari.">
    /**
     * Inicialització de la classe controladora.
     * Inicialitza el controlador amb els requeriments de l'aplicació i l'API 
     * de MIDI.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            
            setSyn(MidiSystem.getSynthesizer());
            syn.loadAllInstruments(syn.getDefaultSoundbank());
            setMc(syn.getChannels());
            syn.open();
            loadKeyBoard();
            loadButtons();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    /**
     * Funció per afegir les tecles al teclat.
     * Prova d'afegir els objectes del teclat a la seva corresponent estructura
     * de dades ArrayList i retorna true. En cas que algun dels
     * objectes no s'hi pugui afegir, retorna false.
     *
     * @return boolean
     */
    public boolean addKeysToKeyboard() {
        
        if(keyboard.add(rect01) &&
                keyboard.add(rect02) &&
                keyboard.add(rect03) &&
                keyboard.add(rect04) &&
                keyboard.add(rect05) &&
                keyboard.add(rect06) &&
                keyboard.add(rect07) &&
                keyboard.add(rect08) &&
                keyboard.add(rect09) &&
                keyboard.add(rect10) &&
                keyboard.add(rect11) &&
                keyboard.add(rect12))
            return true;
        
        return false;
        
    }
    
    /**
     * Funció de càrrega del teclat.
     * Carrega els objectes que formen part del teclat a la propietat keyboard
     * en forma d'una estructura de dades de tipus ArrayList.
     *
     * @throws exc.MissingKeyboardException en cas que hi hagi un error a l'hora
     * de vincular els diferents objectes del teclat.
     */
    public void loadKeyBoard() throws MissingKeyboardException {
        
        if(!addKeysToKeyboard())
            throw new MissingKeyboardException();
        
        setNoteList(new NoteList<>());
        
        for (Rectangle rect : keyboard) {
            rect.setOnMouseClicked(new EventHandler<MouseEvent>() {
                
                @Override
                public void handle(MouseEvent me) {
                    try {
                        idToKey(rect.getId());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    playNote();
                }
                
            });
            
        }
        
    }
    
    /**
     * Funció de càrrega dels botons.
     * Carrega els objectes de control de grabació, amb les seves corresponents
     * funcions que defineixen el comportament de cadascun.
     *
     */
    public void loadButtons() {
        
        fileNameTF.setPromptText(Constants.TF_FILE_PROMPT);
        setXmlNoteRecorder(new FXylophoneXML());
        
        record.setOnMouseClicked(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent me) {
                
                if(!isRecording()) {
                    long now = System.currentTimeMillis();
                    setRecordTime(now);
                    setNoteList(new NoteList<>());
                    setRecording(true);
                }
                
            }
            
        });
        
        stopRecord.setOnMouseClicked(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent me) {
                
                setRecording(false);
                noteSaving();
                
            }
            
        });
        
        play.setOnMouseClicked(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent me) {
                
                if (!isPlaying()) {
                    setPlaying(true);
                    setWait(0);
                    try {
                        setFileNameFromTF();
                    } catch (InvalidFileNameException ifnEx) {
                        xmlNoteRecorder.setFileName(Constants.DEFAULT_FILENAME);
                    }
                    playNotesFromXMLList();
                    setPlaying(false);
                }
                
            }
            
        });
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Mètodes controladors de grabació/reproducció de notes.">
    /**
     * Funció per especificar el nom del fitxer XML a on es guardaran els
     * objectes Note. Els formata adequadament
     */
    private void setFileNameFromTF() throws InvalidFileNameException {
        
        String fileName = fileNameTF.getText();
        
        if (!fileName.equals(Constants.VOID_STRING))
            xmlNoteRecorder.setFileName(fileName);
        else {
            throw new InvalidFileNameException();
        }
        
        System.out.println("Nom del fitxer: " + xmlNoteRecorder.getFileName());
        
    }
    
    /**
     * Funció per canviar el valor de la nota a reproduïr.
     * Introduïnt com a paràmetre la id de l'objecte que representa una tecla
     * en aquesta funció, el valor de la nota canvia de forma
     * segura. En cas que no hagi canviat adequadament, llença una excepció.
     *
     * @param key La nova nota a enregistrar.
     * @throws KeyErrorException si hi ha problemes enregistrant el valor de les
     * notes.
     */
    public void idToKey(String key) throws KeyErrorException {
        
        int newKey = 2*Integer.parseInt(key) + 90;
        int oldKey = this.key;
        setKey(newKey);
        
        if (oldKey == newKey)
            throw new KeyErrorException();
        
    }
    
    /**
     * Funció per reproduir una nota.
     * Reprodueix una nota pel canal 10 i desa les seves propietats
     * (moment i to) en un objecte de la classe Note.
     * En cas d'estar el programa en procés de grabació, afegeix la nota
     * reproduïda a una estructura de dades NoteQueue.
     */
    public void playNote() {
        
        Note n = new NoteImpl(key);
        
        if (isRecording()) {
            noteList.add(n);
        }
        
        mc[4].noteOn(n.getValue(), Constants.VOLUME);
        setKey(0);
        
    }
    
    /**
     * Funció que itera la llista de notes extreta del fitxer XML. Guarda la 
     * llista com a propietat i envia per paràmetre cadascun dels camps de la 
     * llista a la funció playRecordedNote.
     */
    private void playNotesFromXMLList() {
        try {
            NoteList<Note> noteList = (NoteList<Note>) xmlNoteRecorder.XMLtoNotes();
            setNoteList(noteList);
            for (Note note : getNoteList())
                playRecordedNote(note);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Funció que s'encarrega de cridar les rutines que guarden cada nota
     * reproduida a un fitxer XML especificat.
     */
    private void noteSaving() {
        setRecording(false);
        try {
            setFileNameFromTF();
            xmlNoteRecorder.setNoteRecording(noteList);
            xmlNoteRecorder.notesToXML();
        } catch (InvalidFileNameException ifnEx) {
            System.out.println(ifnEx.getMessage());
            xmlNoteRecorder.setFileName(Constants.DEFAULT_FILENAME);
            try {
                xmlNoteRecorder.notesToXML();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Funció per reproduir una nota extreta d'un fitxer XML.
     * Implementa un sistema pel qual la funció interpreta el temps entre cada
     * nota i fa esperar l'aplicació per tal de mantenir la coherència de la
     * grabació.
     *
     * @param recdNote Nota a reproduir, extreta del fitxer XML.
     */
    public void playRecordedNote(Note recdNote) {
        try {
            System.out.println(recdNote);
            
            // espera el temps necessari des de l'última nota per reproduïr la següent
            long playedTime = recdNote.getTimestamp() + 1;
            long timeSleep = playedTime - getWait();
            int noteValue = recdNote.getValue();
            
            Thread.sleep(timeSleep);
            
            mc[10].noteOn(noteValue,Constants.VOLUME);
            setWait(playedTime);
            
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getters i setters.">
    /**
     * Retorna el sintetitzador de l'API MIDI.
     * 
     * @return Objecte de la classe Synthesizer.
     */
    public Synthesizer getSyn() {
        return syn;
    }
    
    /**
     * Retorna l'array d'instruments de l'API MIDI.
     * 
     * @return Array d'objectes de la classe Instrument. 
     */
    public Instrument[] getInstr() {
        return instr;
    }
    
    /**
     * Retorna l'array de canals de l'API MIDI.
     * 
     * @return Array d'objectes de la classe MidiChannel
     */
    public MidiChannel[] getMc() {
        return mc;
    }
    
    /**
     * Retorna el valor enter de la nota reproduida.
     * 
     * @return Valor enter de la nota reproduida amb l'API MIDI.
     */
    public int getKey() {
        return key;
    }
    
    /**
     * Retorna un valor booleà per saber si l'aplicació està en procés de
     * grabació.
     * 
     * @return Valor boolean (true si està grabant, false si no ho està).
     */
    public boolean isRecording() {
        return recording;
    }
    
    /**
     * Retorna el moment en què l'aplicació ha començat a grabar.
     * 
     * @return Valor long del temps en milisegons del moment en el que el
     * programa ha començat a grabar les notes.
     */
    public static long getRecordTime() {
        return FXylophoneController.recordTime;
    }
    
    /**
     * Retorna un valor booleà per saber si l'aplicació està en procés de
     * reproducció.
     * 
     * @return Valor boolean (true si està reproduint, false si no ho està).
     */
    public boolean isPlaying() {
        return playing;
    }
    
    /**
     * Retorna l'estructura de dades que guarda objectes Note per grabar i
     * reproduir.
     * 
     * @return Llista d'objectes de la classe Note.
     */
    public List<Note> getNoteList() {
        return noteList;
    }
    
    /**
     * Retorna l'objecte enregistrador/reproductor d'objectes Note en fitxers 
     * XML.
     * 
     * @return Objecte controlador dels fitxers XML on es guarden les dades
     * dels objectes de la classe Note grabats.
     */
    public FXylophoneXML getXmlNoteRecorder() {
        return xmlNoteRecorder;
    }
    
    /**
     * Retorna el temps que ha passat entre una nota i l'altre.
     * 
     * @return Valor long del temps entre la reproducció d'una nota i la 
     * següent.
     */
    public long getWait() {
        return wait;
    }
    
    /**
     * Defineix l'objecte sintetitzador de l'API MIDI.
     * 
     * @param syn Objecte de la classe Synthesizer.
     */
    public void setSyn(Synthesizer syn) {
        this.syn = syn;
    }
    
    /**
     * Defineix el vector d'instruments de l'API MIDI.
     * 
     * @param instr Array d'objectes de la classe Instrument.
     */
    public void setInstr(Instrument[] instr) {
        this.instr = instr;
    }
    
    /**
     * Defineix el vector de canals de l'API MIDI.
     * 
     * @param mc Array d'objectes de la classe MidiChannel.
     */
    public void setMc(MidiChannel[] mc) {
        this.mc = mc;
    }
    
    /**
     * Defineix el valor de la nota a reproduir.
     * 
     * @param key Valor enter de la nota a reproduir.
     */
    public void setKey(int key) {
        this.key = key;
    }
    
    /**
     * Defineix la llista de notes per grabar/reproduir.
     * 
     * @param noteRecording Estructura de dades List d'objectes de la classe
     * Note.
     */
    public static void setNoteList(NoteList<Note> noteRecording) {
        FXylophoneController.noteList = noteRecording;
    }
    
    /**
     * Defineix si l'aplicació està grabant o no.
     * 
     * @param recording Valor boolean (true si està grabant, false si no ho 
     * està).
     */
    public void setRecording(boolean recording) {
        this.recording = recording;
    }
    
    /**
     * Defineix el moment en què l'aplicació ha començat a enregistrar so.
     * 
     * @param recordTime Valor long del moment en milisegons en què l'aplicació
     * ha començat a grabar.
     */
    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }
    
    /**
     * Defineix si l'aplicació està reproduint o no.
     * 
     * @param playing Valor boolean (true si està reproduint, false si no ho 
     * està).
     */
    public void setPlaying(boolean playing) {
        this.playing = playing;
    }
    
    /**
     * Defineix l'objecte enregistrador/reproductor d'objectes Note en fitxers
     * XML.
     * 
     * @param xmlToNote Objecte controlador dels fitxers XML on es guarden les dades
     * dels objectes de la classe Note grabats.
     */
    private void setXmlNoteRecorder(FXylophoneXML xmlToNote) {
        this.xmlNoteRecorder = xmlToNote;
    }
    
    /**
     * Defineix el temps que ha passat entre una nota i l'altre.
     * 
     * @param wait Valor long del temps entre la reproducció d'una nota i la 
     * següent.
     */
    public void setWait(long wait) {
        this.wait = wait;
    }
    //</editor-fold>
    
}
