package note;

//<editor-fold defaultstate="collapsed" desc="Imports.">
import java.util.ArrayList;
import java.util.Collection;
//</editor-fold>

/**
 * Classe que hereta d'ArrayList per crear estructures de dades orientades a
 * guardar objectes de la classe Note.
 * 
 * @author Edgar Ben
 */
public class NoteList<T> extends ArrayList {

    // PROPIETATS
    
    //<editor-fold defaultstate="collapsed" desc="Propietats.">
    /**
     * Array d'objectes genèrics.
     */
    private T[] llista;
    //</editor-fold>

    // MÈTODES
    
    //<editor-fold defaultstate="collapsed" desc="Constructors.">
    /**
     * Constructor de tamany estàtic.
     * 
     * @param initialCapacity Valor integer del límit en el nombre d'elements.
     */
    public NoteList(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Constructor sense arguments.
     */
    public NoteList() {
    }

    /**
     * Constructor a partir d'una colecció predefinida.
     * 
     * @param c Objecte que implementa la interfície Collection.
     */
    public NoteList(Collection c) {
        super(c);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters i setters.">
    /**
     * Retorna la llista d'elements.
     *
     * @return Llista d'elements genèrics.
     */
    public T[] getListaNotas() {
        return llista;
    }
    
    /**
     * Defineix la llista d'elements
     *
     * @param llista Llista d'elements genèrics.
     */
    public void setListaNotas(T[] llista) {
        this.llista = llista;
    }
    //</editor-fold>

}
