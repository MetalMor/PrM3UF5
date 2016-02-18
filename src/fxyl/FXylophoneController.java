package fxyl;

import constants.Constants;
import exc.InvalidFileNameException;
import exc.KeyErrorException;
import exc.MissingKeyboardException;
import exc.WrongNoteException;
import java.io.InputStream;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.xml.parsers.ParserConfigurationException;
import note.Note;
import note.NoteImpl;

/**
 * Classe controladora del xilòfon.
 * Realitza les tasques relacionades amb la reproducció de so.
 *
 * 260116
 * @author mor
 */
public class FXylophoneController implements Initializable {
    
    //PROPIETATS OBJECTE DE L'API MIDI.
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
    
    // PROPIETATS DEL TECLAT
    /**
     * Propietat que representa el teclat del xilòfon.
     * Cadascuna de les tecles del xilòfon és un objecte diferent que queda 
     * recollit a l'estructura de dades keyboard de tipus ArrayList.
     */
    private List<Rectangle> keyboard = new ArrayList<>();
    /**
     * Propietat que allotja temporalment el valor de la nota per reproduir-la.
     */
    private int key;
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
    
    // PROPIETATS DE CONTROL DE GRABACIÓ/REPRODUCCIÓ.
    /**
     * Flag per indicar si el programa està grabant.
     */
    private boolean recording = false;
    /**
     * Estructura de dades a on guardar els objectes Note, preparats per ser
     * inserits a un fitxer XML.
     */
    private static ArrayList<Note> noteRecording;
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
     * Camp de text a on s'introdueix el nom del fitxer XML per guardar i
     * reproduir les notes.
     */
    @FXML private TextField fileNameTF;
    
    /**
     * Objecte encarregat de gestionar la inserció i extracció d'objectes Note
     * a un fitxer XML.
     */
    private FXylophoneXML<Note> xmlNoteRecorder;
    /**
     * Temps en milisegons que el programa esperarà entre una nota i una altra
     * quan estigui reproduint les dades d'un fitxer XML.
     */
    private long wait = 0;
    
    /**
     * Inicialització.
     * Inicialitza el controlador amb els requeriments de l'API de MIDI.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            
            setSyn(MidiSystem.getSynthesizer());
            syn.loadAllInstruments(syn.getDefaultSoundbank());
            //setInstr(syn.getDefaultSoundbank().getInstruments());
            //syn.loadInstrument(instr[INSTRUMENT]);
            setMc(syn.getChannels());
            mc[10].programChange(0, Constants.INSTRUMENT);
            //sm.setMessage(ShortMessage.PROGRAM_CHANGE, 9, INSTRUMENT, 0);
            syn.open();
            loadKeyBoard();
            loadButtons();
            
        } catch (Exception ex) {
            ex.printStackTrace();
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
        setXmlNoteRecorder(new FXylophoneXML<>());
        
        record.setOnMouseClicked(new EventHandler<MouseEvent>() {
                
            @Override
            public void handle(MouseEvent me) {

                if(!isRecording()) {
                    setNoteRecording(new ArrayList<>());
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

                try {
                    for (Note note : xmlNoteRecorder.XMLtoNotes()) 
                        playRecordedNote(note);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
            
        });
        
    }
    
    /**
     * Funció que s'encarrega de cridar les rutines que guarden cada nota
     * reproduida a un fitxer XML especificat.
     */
    private void noteSaving() {
        setRecording(false);
        try {
            setFileNameFromTF();
            xmlNoteRecorder.setNoteRecording(noteRecording);
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
     * Funció de càrrega del teclat.
     * Carrega els objectes que formen part del teclat a la propietat keyboard
     * en forma d'una estructura de dades de tipus ArrayList.
     * 
     * @throws exc.MissingKeyboardException
     */
    public void loadKeyBoard() throws MissingKeyboardException {
        
        if(!addKeysToKeyboard())
            throw new MissingKeyboardException();
        
        setNoteRecording(new ArrayList<>());
        
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
     * Funció per reproduir una nota.
     * Reprodueix una nota pel canal 10 i desa les seves propietats 
     * (moment i to) en un objecte de la classe Note.
     * En cas d'estar el programa en procés de grabació, afegeix la nota 
     * reproduïda a una estructura de dades ArrayList.
     */
    public void playNote() {
        
        Note n = new NoteImpl(key);
        
        if (isRecording()) {
            noteRecording.add(n);
        }
      
        mc[4].noteOn(n.getValue(),300);
        setKey(0);
        
    }
    
    /**
     * Funció per reproduir una nota extreta d'un fitxer XML.
     * Implementa un sistema pel qual la funció interpreta el temps entre cada
     * nota i fa esperar l'aplicació per tal de mantenir la coherència de la
     * grabació.
     * 
     * @param recdNote Nota extreta del fitxer XML.
     */
    public void playRecordedNote(Note recdNote) {
        try {
            System.out.println(recdNote);
            // espera el temps necessari des de l'última nota per reproduïr la següent
            wait(recdNote.getPlayedTime() - getWait());
            mc[10].noteOn(recdNote.getValue(),300);
            setWait(recdNote.getPlayedTime());
        } catch (InterruptedException ex) {
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
     * Funció per canviar el valor de la nota a reproduïr.
     * Introduïnt com a paràmetre la id de l'objecte que representa una tecla
     * en aquesta funció, el valor de la nota canvia de forma
     * segura. En cas que no hagi canviat adequadament, llença una excepció.
     * 
     * @param key La nova nota per enregistrar.
     * @throws KeyErrorException 
     */
    public void idToKey(String key) throws KeyErrorException {
        
        int newKey = 2*Integer.parseInt(key) + 90;
        int oldKey = this.key;
        setKey(newKey);
        
        if (oldKey == newKey) 
            throw new KeyErrorException();
        
    }
    
    public Synthesizer getSyn() {
        return syn;
    }

    public Instrument[] getInstr() {
        return instr;
    }

    public MidiChannel[] getMc() {
        return mc;
    }

    public int getKey() {
        return key;
    }

    public boolean isRecording() {
        return recording;
    }

    public List<Note> getNoteRecording() {
        return noteRecording;
    }

    public FXylophoneXML<Note> getXmlNoteRecorder() {
        return xmlNoteRecorder;
    }

    public long getWait() {
        return wait;
    }
    
    public void setSyn(Synthesizer syn) {
        this.syn = syn;
    }

    public void setInstr(Instrument[] instr) {
        this.instr = instr;
    }

    public void setMc(MidiChannel[] mc) {
        this.mc = mc;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public static void setNoteRecording(ArrayList<Note> noteRecording) {
        FXylophoneController.noteRecording = noteRecording;
    }

    public void setRecording(boolean recording) {
        this.recording = recording;
    }

    private void setXmlNoteRecorder(FXylophoneXML<Note> xmlToNote) {
        this.xmlNoteRecorder = xmlToNote;
    }

    public void setWait(long wait) {
        this.wait = wait;
    }
    
}
