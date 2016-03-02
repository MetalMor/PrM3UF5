package fxyl;

//<editor-fold defaultstate="collapsed" desc="Imports.">
import constants.ApplicationConstants;
import exc.InvalidFileNameException;
import exc.KeyErrorException;
import exc.MissingKeyboardException;
import exc.WrongNoteException;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
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
     * Array d'instruments MIDI extrets del <code>Soundbank</code> per defecte.
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
     * recollit a l'estructura de dades keyboard de tipus <code>ArrayList</code>.
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
    /**
     * Imatge animada.
     */
    @FXML private ImageView image;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Propietats de control de gravació/reproducció.">
    /**
     * Objecte encarregat de gestionar la inserció i extracció d'objectes <code>Note</code>
     * a un fitxer XML.
     */
    private FXylophoneXML xmlNoteRecorder;
    /**
     * Estructura de dades a on guardar els objectes <code>Note</code>, preparats per ser
     * inserits a un fitxer XML.
     */
    private static NoteList<Note> noteList;
    /**
     * Propietat que guarda temporalment el valor de la nota per reproduir-la.
     */
    private int key;
    /**
     * Temps en milisegons que el programa esperarà entre una nota i una altra
     * quan estigui reproduint les dades d'un fitxer XML.
     */
    private long wait = 0;
    /**
     * Flag per indicar si el programa està gravant.
     */
    private boolean recording = false;
    /**
     * Moment de l'inici de la gravació.
     */
    private static long recordTime;
    /**
     * Flag per indicar si el programa està reproduint.
     */
    private boolean playing = false;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Objectes de la interfície de l'usuari.">
    /**
     * Botó per reproduir els objectes <code>Note</code> desats a un fitxer XML.
     */
    @FXML private Button play;
    /**
     * Botó per iniciar la inserció d'objectes <code>Note</code> a l'<code>ArrayList</code> que servirà
     * per desar les dades a un fitxer XML.
     */
    @FXML private Button record;
    /**
     * Botó per parar la gravació i guardar totes les dades de l'objecte
     * <code>ArrayList</code> a un fitxer XML.
     */
    @FXML private Button stopRecord;
    
    /**
     * Indicador de gravació, canvia de color quan l'aplicació està en el procés
     * de gravació de notes.
     */
    @FXML private Circle recordControl;
    
    /**
     * Camp de text a on s'introdueix el nom del fitxer XML per guardar i
     * reproduir les notes.
     */
    @FXML private TextField fileNameTF;
    
    /**
     * Camp de text a on es mostra el fitxer utilitzat per l'intercanvi de
     * dades d'objectes <code>Note</code>.
     */
    @FXML private TextField showStateTF;
    
    /**
     * Camp de text a on es mostra la nota reproduïda en temps real.
     */
    @FXML private TextField showPlayedNoteTF;
    //</editor-fold>
    
//     MÈTODES
    
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
            getSyn().loadAllInstruments(getSyn().getDefaultSoundbank());
            setMc(getSyn().getChannels());
            getSyn().open();
            loadKeyBoard();
            loadButtons();
            
        } catch (MissingKeyboardException mkEx) {
            showInfo(mkEx.getError(), getShowStateTf());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            showInfo(ApplicationConstants.READY_MESSAGE, getShowStateTf());
        }
        
    }
    
    /**
     * Funció per afegir les tecles al teclat.
     * Prova d'afegir els objectes del teclat a la seva corresponent estructura
     * de dades <code>ArrayList</code> i retorna true. En cas que algun dels
     * objectes no s'hi pugui afegir, retorna false.
     *
     * @return boolean (true si la inicialització ha sigut correcta).
     */
    public boolean addKeysToKeyboard() {
        
        if(getKeyboard().add(rect01) &&
                getKeyboard().add(rect02) &&
                getKeyboard().add(rect03) &&
                getKeyboard().add(rect04) &&
                getKeyboard().add(rect05) &&
                getKeyboard().add(rect06) &&
                getKeyboard().add(rect07) &&
                getKeyboard().add(rect08) &&
                getKeyboard().add(rect09) &&
                getKeyboard().add(rect10) &&
                getKeyboard().add(rect11) &&
                getKeyboard().add(rect12))
            return true;
        
        return false;
        
    }
    
    /**
     * Funció de càrrega del teclat.
     * Carrega els objectes que formen part del teclat a la propietat keyboard
     * en forma d'una estructura de dades de tipus <code>ArrayList</code>.
     *
     * @throws exc.MissingKeyboardException en cas que hi hagi un error a l'hora
     * de vincular els diferents objectes del teclat.
     */
    public void loadKeyBoard() throws MissingKeyboardException {
        
        if(!addKeysToKeyboard())
            throw new MissingKeyboardException();
        
        setNoteList(new NoteList<>());
        
        for (Rectangle rect : getKeyboard()) {
            rect.setOnMouseClicked(new EventHandler<MouseEvent>() {
                
                @Override
                public void handle(MouseEvent me) {
                    try {
                        idToKey(rect.getId());
                    } catch (KeyErrorException keEx) {
                        getShowStateTf().setText(keEx.getError());
                        setKey(ApplicationConstants.DEF_NOTE_VALUE);
                    } finally {
                        playNote();
                    }
                }
                
            });
            
        }
        
    }
    
    /**
     * Funció de càrrega dels botons.
     * Carrega els objectes de control de gravació, amb les seves corresponents
     * funcions que defineixen el comportament de cadascun.
     *
     */
    public void loadButtons() {
        
        setFileNameTf(getFileNameTf());
        setShowPlayedNoteTf(getShowPlayedNoteTF());
        setShowStateTf(getShowStateTf());
        
        getFileNameTf().setPromptText(ApplicationConstants.TF_FILE_PROMPT);
        setXmlNoteRecorder(new FXylophoneXML());
        setRecording(false);
        
        getRecord().setOnMouseClicked(new EventHandler<MouseEvent>() {
            
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
        
        getStopRecord().setOnMouseClicked(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent me) {
                
                setRecording(false);
                noteSaving();
                
            }
            
        });
        
        getPlay().setOnMouseClicked(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent me) {
                
                if (!isPlaying()) {
                    showInfo(ApplicationConstants.PLAYING_MESSAGE, getShowStateTf());
                    setPlaying(true);
                    setWait(0);
                    try {
                        setFileNameFromTf();
                    } catch (InvalidFileNameException ifnEx) {
                        showInfo(ifnEx.getError(), getShowStateTf());
                        getXmlNoteRecorder().setFileName(ApplicationConstants.DEFAULT_FILENAME);
                    } finally {
                        playNotesFromXMLList();
                        setPlaying(false);
                        showInfo(ApplicationConstants.END_PLAYING_MESSAGE, getShowStateTf());
                    }
                }
                
            }
            
        });
        
    }
    
    /**
     * Controla l'indicador de gravació. Mentre l'aplicació estigui enregistrant
     * notes, l'indicador serà de color vermell. En cas contrari, serà de color
     * gris.
     * 
     * @param recording Booleà per indicar si l'aplicació es troba en procés de
     * gravació.
     */
    private void switchRecordControl(boolean recording) {
        
        this.recordControl = getRecordControl();
        
        if (recording)
            getRecordControl().setFill(Color.RED);
        else
            getRecordControl().setFill(Color.GREY);
        
    }
    
    /**
     * Mostra una informació determinada a un camp de text especificat.
     * 
     * @param data Informació a mostrar.
     * @param out Objecte <code>TextField</code> que fa de sortida.
     */
    private void showInfo(String data, TextField out) {
        out.setText(data);
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Mètodes controladors de gravació/reproducció de notes.">
    /**
     * Funció per especificar el nom del fitxer XML a on es guardaran els
     * objectes Note. Els formata adequadament
     */
    private void setFileNameFromTf() throws InvalidFileNameException {
        
        String fileName = getFileNameTf().getText();
        
        if (!fileName.equals(ApplicationConstants.VOID_STRING))
            getXmlNoteRecorder().setFileName(fileName);
        else {
            throw new InvalidFileNameException();
        }
        
        System.out.println("Nom del fitxer: " + getXmlNoteRecorder().getFileName());
        
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
        
        int newKey = Note.noteValueFromKey(key);
        int oldKey = this.getKey();
        setKey(newKey);
        
        if (oldKey == newKey)
            throw new KeyErrorException();
        
    }
    
    /**
     * Funció per reproduir una nota.
     * Reprodueix una nota pel canal 10 i desa les seves propietats
     * (moment i to) en un objecte de la classe Note.
     * En cas d'estar el programa en procés de gravació, afegeix la nota
     * reproduïda a una estructura de dades NoteQueue.
     */
    public void playNote() {
        
        Note n = new NoteImpl(getKey());
        
        if (isRecording()) {
            noteList.add(n);
        }
        
        sound(n);
        setKey(0);
        
    }
    
    /**
     * Funció que itera la llista de notes extreta del fitxer XML. Guarda la 
     * llista com a propietat i envia per paràmetre cadascun dels camps de la 
     * llista a la funció playRecordedNote.
     */
    private void playNotesFromXMLList() {
        try {
            showInfo(ApplicationConstants.PLAYING_MESSAGE, getShowStateTf());
            NoteList<Note> noteList = (NoteList<Note>) getXmlNoteRecorder().XMLtoNotes();
            setNoteList(noteList);
            setFileNameFromTf();
        } catch (WrongNoteException wnEx) {
            showInfo(wnEx.getError(), getShowStateTf());
        } catch (InvalidFileNameException ifnEx) {
            showInfo(ifnEx.getError(), getShowStateTf());
            getXmlNoteRecorder().setFileName(ApplicationConstants.DEFAULT_FILENAME);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            showInfo(ApplicationConstants.END_PLAYING_MESSAGE, getShowStateTf());
            for (Note note : getNoteList())
                playRecordedNote(note);
        }
    }
    
    /**
     * Funció que s'encarrega de cridar les rutines que guarden cada nota
     * reproduida a un fitxer XML especificat.
     */
    private void noteSaving() {
        setRecording(false);
        try {
            getXmlNoteRecorder().setNoteRecording(noteList);
            setFileNameFromTf();
        } catch (InvalidFileNameException ifnEx) {
            showInfo(ifnEx.getError(), getShowStateTf());
            getXmlNoteRecorder().setFileName(ApplicationConstants.DEFAULT_FILENAME);
            try {
                getXmlNoteRecorder().notesToXML();
            } catch (WrongNoteException wnEx) {
                showInfo(wnEx.getError(), getShowStateTf());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                getXmlNoteRecorder().notesToXML();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
           
        }
    }
    
    /**
     * Funció per reproduir una nota extreta d'un fitxer XML.
     * Implementa un sistema pel qual la funció interpreta el temps entre cada
     * nota i fa esperar l'aplicació per tal de mantenir la coherència de la
     * gravació.
     *
     * @param recdNote Nota a reproduir, extreta del fitxer XML.
     */
    public void playRecordedNote(Note recdNote) {
        try {
            // espera el temps necessari des de l'última nota per reproduïr la següent
            long playedTime = recdNote.getTimestamp() + 1;
            long timeSleep = playedTime - getWait();
            //int noteValue = recdNote.getValue();
            
            Thread.sleep(timeSleep);
            
            sound(recdNote);
            setWait(playedTime);
            
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Funció que s'encarrega  de cridar la funció reproductora de notes. També
     * printa el nom de la nota al seu TextField corresponent.
     */
    private void sound(Note note){
    
         getMc(ApplicationConstants.MIDICHANNEL)
                 .noteOn(note.getValue(), ApplicationConstants.DEF_NOTE_VOLUME);
         // TODO textfield 
         showInfo(note.getPlayedKey(), getShowPlayedNoteTF());
    
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getters i setters.">
    
    /**
     * Retorna la posició especificada de l'array de canals de MIDI.
     * 
     * @param pos Valor enter de la posició requerida de l'array.
     * @return Objecte <code>MidiChannel</code> trobat a la posició requerida.
     */
    public MidiChannel getMc(int pos) {
        return mc[pos];
    }
    
    /**
     * Retorna el sintetitzador de l'API MIDI.
     * 
     * @return Objecte de la classe <code>Synthesizer</code>.
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
     * Retorna el valor enter de la nota reproduida.
     * 
     * @return Valor enter de la nota reproduida amb l'API MIDI.
     */
    public int getKey() {
        return key;
    }
    
    /**
     * Retorna un valor booleà per saber si l'aplicació està en procés de
     * gravació.
     * 
     * @return Valor boolean (true si està gravant, false si no ho està).
     */
    public boolean isRecording() {
        return recording;
    }
    
    /**
     * Retorna el moment en què l'aplicació ha començat a gravar.
     * 
     * @return Valor long del temps en milisegons del moment en el que el
     * programa ha començat a gravar les notes.
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
     * Retorna l'estructura de dades que guarda objectes <code>Note</code> per 
     * gravar i reproduir.
     * 
     * @return Llista d'objectes de la classe Note.
     */
    public List<Note> getNoteList() {
        return noteList;
    }
    
    /**
     * Retorna l'objecte enregistrador/reproductor d'objectes <code>Note</code> 
     * en fitxers XML.
     * 
     * @return Objecte controlador dels fitxers XML on es guarden les dades
     * dels objectes de la classe <code>Note</code> gravats.
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
     * Retorna la imatge mostrada per la vista.
     * 
     * @return Objecte de la classe <code>Image</code> mostrat per la vista.
     */
    public Image getImage() {
        return image.getImage();
    }

    /**
     * Retorna la llista d'objectes que formen el teclat virtual.
     * 
     * @return Llista d'objectes de la classe <code>Rectangle</code>.
     */
    public List<Rectangle> getKeyboard() {
        return keyboard;
    }
    
    /**
     * Retorna el botó per començar a gravar.
     * 
     * @return Objecte de la classe <code>Button</code>.
     */
    private Button getRecord() {
        return record;
    }

    /**
     * Retorna el botó per parar de gravar.
     * 
     * @return Objecte de la classe <code>Button</code>.
     */
    private Button getStopRecord() {
        return stopRecord;
    }

    /**
     * Retorna el botó per reproduir un fitxer XML.
     * 
     * @return Objecte de la classe <code>Button</code>.
     */
    private Button getPlay() {
        return play;
    }
    
    /**
     * Retorna l'objecte que fa d'indicador per saber si l'aplicació es troba
     * en procés de gravació de notes. En cas que no existeixi, el crea.
     * 
     * @return Objecte indicador de la classe <code>Circle</code>.
     */
    public Circle getRecordControl() {
        if (recordControl == null)
            recordControl = new Circle();
        return recordControl;
    }
    
    /**
     * Retorna l'objecte camp de text per escriure el nom del fitxer XML amb
     * el què l'aplicació treballarà.
     * 
     * @return Objecte de la classe <code>TextField</code>.
     */
    public TextField getFileNameTf() {
        if (fileNameTF == null)
            fileNameTF = new TextField();
        return fileNameTF;
    }
    
    /**
     * Retorna l'objecte camp de text a on mostrar l'estat de l'aplicació.
     * En cas que no existeixi, el crea.
     * 
     * @return Objecte de la classe <code>TextField</code>.
     */
    public TextField getShowStateTf() {
        if (showStateTF == null)
            showStateTF = new TextField();
        return showStateTF;
    }

    /**
     * Retorna l'objecte camp de text a on mostrar la nota reproduïda pel 
     * teclat virtual. En cas que no existeixi, el crea.
     * 
     * @return Objecte de la classe <code>TextField</code>.
     */
    public TextField getShowPlayedNoteTF() {
        if (showPlayedNoteTF == null)
            showPlayedNoteTF = new TextField();
        return showPlayedNoteTF;
    }
    
    /**
     * Defineix l'objecte sintetitzador de l'API MIDI.
     * 
     * @param syn Objecte de la classe <code>Synthesizer</code>.
     */
    public void setSyn(Synthesizer syn) {
        this.syn = syn;
    }
    
    /**
     * Defineix el vector d'instruments de l'API MIDI.
     * 
     * @param instr Array d'objectes de la classe <code>Instrument</code>.
     */
    public void setInstr(Instrument[] instr) {
        this.instr = instr;
    }
    
    /**
     * Defineix el vector de canals de l'API MIDI.
     * 
     * @param mc Array d'objectes de la classe <code>MidiChannel</code>.
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
     * Defineix la llista de notes per gravar/reproduir.
     * 
     * @param noteRecording Estructura de dades <code>List</code> d'objectes de 
     * la classe Note.
     */
    public void setNoteList(NoteList<Note> noteRecording) {
        FXylophoneController.noteList = noteRecording;
    }
    
    /**
     * Defineix si l'aplicació està gravant o no.
     * 
     * @param recording Valor boolean (true si està gravant, false si no ho 
     * està).
     */
    public void setRecording(boolean recording) {
        this.recording = recording;
        if (recording)
            showInfo(ApplicationConstants.RECORDING_MESSAGE, getShowStateTf());
        else
            showInfo(ApplicationConstants.STOP_RECORDING_MESSAGE, getShowStateTf());
        switchRecordControl(recording);
    }
    
    /**
     * Defineix el moment en què l'aplicació ha començat a enregistrar so.
     * 
     * @param recordTime Valor long del moment en milisegons en què l'aplicació
     * ha començat a gravar.
     */
    public void setRecordTime(long recordTime) {
        FXylophoneController.recordTime = recordTime;
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
     * Defineix l'objecte enregistrador/reproductor d'objectes <code>Note</code>
     * en fitxers XML.
     * 
     * @param xmlToNote Objecte controlador dels fitxers XML on es guarden les dades
     * dels objectes de la classe <code>Note</code> gravats.
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
    
    /**
     * Defineix la imatge mostrada per la vista.
     * 
     * @param image Objecte imatge que la vista mostrarà.
     */
    public void setImage(Image image) {
        this.image.setImage(image);
    }
    
    /**
     * Defineix l'objecte que farà d'indicador per saber si l'aplicació
     * està en procés de gravació de notes.
     * 
     * @param recordControl Objecte indicador de la classe <code>Circle</code>.
     */
    public void setRecordControl(Circle recordControl) {
        this.recordControl = recordControl;
    }
    
    /**
     * Defineix l'objecte camp de text a on mostrar el fitxer XML que
     * l'aplicació utilitzarà per guardar/reproduir notes.
     * 
     * @param fileNameTF Objecte camp de text.
     */
    private void setFileNameTf(TextField fileNameTF) {
        this.fileNameTF = fileNameTF;
    }
    
    /**
     * Defineix l'objecte camp de text a on mostrar l'estat de l'aplicació.
     * 
     * @param showStateTF Objecte camp de text.
     */
    public void setShowStateTf(TextField showStateTF) {
        this.showStateTF = showStateTF;
    }

    /**
     * Defineix l'objecte camp de text a on mostrar la nota reproduïda pel
     * teclat virtual.
     * 
     * @param showPlayedNoteTF Objecte camp de text.
     */
    public void setShowPlayedNoteTf(TextField showPlayedNoteTF) {
        this.showPlayedNoteTF = showPlayedNoteTF;
    }
    //</editor-fold>
    
}
