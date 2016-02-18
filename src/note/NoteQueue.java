package note;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Edgar Ben
 */
public class NoteQueue<T> extends ArrayList {

private T[] ListaNotas;

    

//<editor-fold defaultstate="collapsed" desc="Constructores">
public NoteQueue(int initialCapacity) {
    super(initialCapacity);
}

public NoteQueue() {
}

public NoteQueue(Collection c) {
    super(c);
}
//</editor-fold>

    public T[] getListaNotas() {
        return ListaNotas;
    }

    public void setListaNotas(T[] ListaNotas) {
        this.ListaNotas = ListaNotas;
    }

  
    
    

   
   
   

    
    
    
}
