package note;

//<editor-fold defaultstate="collapsed" desc="Imports.">
import constants.ApplicationConstants;
import constants.NoteConstants;
import fxyl.FXylophoneController;
import java.util.HashMap;
import java.util.Map;
//</editor-fold>


/**
 * Classe d'implementació per crear objectes Note.
 * 
 * @version 110216
 * @author Edgar Ben
 */
public class NoteImpl extends Note {
 
    //<editor-fold defaultstate="collapsed" desc="Constructors.">
    public NoteImpl (int note) {
        super(note);
    }
    
    public NoteImpl (int note, long timestamp) {
        super(note,timestamp);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Overrides (Note).">
    @Override
    public String getPlayedKey() {
        return Note.noteKeyFromValue(this.getValue());
    }
    
    @Override
    public long getPlayedTime() {
        
        long currentTime = System.currentTimeMillis();
        long recordTime = FXylophoneController.getRecordTime();
        long noteTime = currentTime - recordTime;
        
        /* DEBUG
        System.out.println("Actual: " + currentTime/1000);
        System.out.println("Programa: " + recordTime/1000);
        System.out.println("Nota: " + noteTime/1000);
        */        
        
        return noteTime;
        
    }
    //</editor-fold>
    
}
