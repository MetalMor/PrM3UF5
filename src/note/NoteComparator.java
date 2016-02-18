package note;

import java.util.Comparator;

/**
 * Classe comparadora d'objectes Note.
 * Implementa Comparator per tal de mantenir les estructures de dades d'objectes
 * Note ordenats ascendentment.
 *
 * 020216
 * @author mor
 */
public class NoteComparator implements Comparator {

    @Override
    public int compare(Object n1, Object n2) {
        
        Note note1 = (Note) n1;
        Note note2 = (Note) n2;
        
        return note1.compareTo(note2);
        
//        if (note1.compareTo(note2) < 0)
//            return -1;
//        
//        else if (note1.compareTo(note2) > 0)
//            return 1;
//        
//        return 0;
        
    }
    
}
