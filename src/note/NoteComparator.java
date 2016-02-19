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

    //<editor-fold defaultstate="collapsed" desc="Overrides.">
    /**
     * Compara objectes de la classe Note a partir del seu mètode compareTo.
     *
     * @param n1 Objecte de la classe Note.
     * @param n2 Objecte de la classe Note.
     * @return Negatiu si el primer va abans, positiu si va després o zero si
     * són iguals (cosa impossible, per cert).
     */
    @Override
    public int compare(Object n1, Object n2) {
        
        Note note1 = (Note) n1;
        Note note2 = (Note) n2;
        
        return note1.compareTo(note2);
        
    }
    //</editor-fold>
    
}
