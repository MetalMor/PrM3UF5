package note;

import constants.Constants;


/**
 * Classe d'implementació per crear objectes Note.
 * 
 * 110216
 * @author Edgar Ben
 */
public class NoteImpl extends Note {
 
    public NoteImpl (int note) {
       super(note);
    }
    
    public NoteImpl (int note, long timestamp) {
        super(note,timestamp);
    }

    public int getPlayedKey() {
        return (getValue()-90)/2;
    }

    public long getPlayedTime() {
        return getTimestamp() - Constants.CURRENT_TIME_MILLIS;
    }
    
}
