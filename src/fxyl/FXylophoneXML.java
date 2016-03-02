package fxyl;

//<editor-fold defaultstate="collapsed" desc="Imports.">
import constants.ApplicationConstants;
import exc.WrongNoteException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import note.Note;
import note.NoteComparator;
import note.NoteImpl;
import note.NoteList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
//</editor-fold>

/**
 * Classe que implementa la inserció i extracció d'objectes Note a un fitxer
 * XML.
 *
 * @version 020216
 * @author mor
 */
public class FXylophoneXML {

    // PROPIETATS

    //<editor-fold defaultstate="collapsed" desc="Propietats.">
    /**
     * Estructura de dades per guardar temporalment objectes Note en el procés
     * de gravació/extracció de notes.
     */
    private List<Note> noteList = new NoteList<>();
    /**
     * Objecte que implementa Comparator per ordenar adequadament, segons
     * el seu timestamp, els objectes Note a la seva estructura de dades.
     */
    private NoteComparator nc = new NoteComparator();
    /**
     * Nom del fitxer xml en el qual es guardaran les dades dels objectes
     * Note (sense extensió!).
     */
    private String fileName;
    //</editor-fold>

    // MÈTODES

    //<editor-fold defaultstate="collapsed" desc="Constructors.">
    /**
     * Constructor sense arguments.
     */
    public FXylophoneXML() { }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Mètodes Note -> XML.">
    /**
     * Funció que construeix un element Note.
     * Per tal de poder guardar les dades dels missatges MIDI a un XML, cal
     * convertir-les a un format XML. Aquesta funció s'encarrega de crear un
     * element XML a partir d'una nota enviat per paràmetre.
     *
     * @param n Objecte de la classe Note.
     * @param doc Objecte Document al qual s'afegeix l'element.
     * @return Element
     */
    public Element XMLcreateNoteElement(Note n, Document doc) {

        Element note = doc.createElement(ApplicationConstants.NOTE_ELEMENT);

        Element value = doc.createElement(ApplicationConstants.VALUE_ELEMENT);
        value.appendChild(
                doc.createTextNode(Integer.toString(n.getValue())));
        Element timestamp = doc.createElement(ApplicationConstants.TIMESTAMP_ELEMENT);
        timestamp.appendChild(
                doc.createTextNode(Long.toString(n.getTimestamp())));

        note.appendChild(value);
        note.appendChild(timestamp);

        return note;

    }

    /**
     * Funció <code>Note</code> a XML.
     * Guarda les dades d'un conjunt d'objectes <code>Note</code> en un document
     * XML utilitzant l'API DOM.
     *
     * @throws ParserConfigurationException en cas d'error de configuració.
     * @throws WrongNoteException en cas de trobar una incoherència en un
     * objecte Note.
     * @throws TransformerException en cas de trobar problemes durant la
     * transformació en un document XML.
     */
    public void notesToXML() throws ParserConfigurationException,
            WrongNoteException, TransformerException {

        noteList.sort(getNc());

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document docNode = docBuilder.newDocument();
        Element rootElement = docNode.createElement(ApplicationConstants.SOUNDFILE_ELEMENT);
        docNode.appendChild(rootElement);

        for (Note note : noteList) {

            /**
             * Si no és una nota de MIDI, llença una excepció. No té sentit perquè
             * només s'utilitzen objectes de la classe <code>Note</code> en 
             * aquest programa, però em feia il·lusió posar aquesta excepció :D
             */
            if(!(note instanceof Note))
                throw new WrongNoteException();

            Note noteToRecord = (Note) note;

            rootElement.appendChild(
                    XMLcreateNoteElement(noteToRecord, docNode)
            );

        }

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transf = tf.newTransformer();
        DOMSource orig = new DOMSource(docNode);
        StreamResult outXML = new StreamResult(new File(this.getFileName()));

        transf.transform(orig, outXML);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Mètodes XML -> Note.">
    /**
     * La nota passada per paràmetre queda enregistrada a una estructura de
     * dades de tipus <code>List</code>.
     * 
     * @param n Objecte de la classe <code>Note</code>.
     * @return Valor booleà que indica si l'objecte s'ha guardat correctament.
     */
    public boolean recordNote(Note n) {

        System.out.println("XMLtoNote");
        System.out.println(n);
        if(!noteList.add(n))
            return false;

        return true;

    }

    /**
     * Funció per extreure un conjunt de notes d'un fitxer XML.
     * Per reproduir les notes guardades a un arxiu XML s'han de desar en una
     * llista d'objectes <code>Note</code>.
     *
     * @return La llista d'objectes de la classe <code>Note</code> construits a 
     * partir de les dades d'un fitxer XML.
     * @throws IOException si hi ha un error d'entrada/sortida de dades.
     * @throws SAXException si hi ha un error amb l'API SAX.
     * @throws ParserConfigurationException en cas d'error de configuració.
     * @throws WrongNoteException en cas de trobar una incoherència en un
     * objecte <code>Note</code>.
     */
    public List<Note> XMLtoNotes() throws IOException, SAXException,
            ParserConfigurationException, WrongNoteException {

        File midiXml = new File(this.getFileName());
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(midiXml);
        doc.getDocumentElement().normalize();

        NodeList nodes = doc.getElementsByTagName(ApplicationConstants.NOTE_ELEMENT);

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;
                Note noteToPlay = new NoteImpl(
                        Integer.parseInt(getContent(ApplicationConstants.VALUE_ELEMENT, element)),
                        Long.parseLong(getContent(ApplicationConstants.TIMESTAMP_ELEMENT, element))
                );

                if(!recordNote(noteToPlay))
                    throw new WrongNoteException();

            }
        }

        noteList.sort(nc);

        for (Note note : noteList)
            System.out.println(note);

        return noteList;

    }

    /**
     * Funció per recuperar el valor d'un node XML.
     *
     * @param etiqueta Valor <code>String</code> del node que desitgem extreure 
     * de l'XML.
     * @param element Objecte <code>Element</code> a on cercar el node.
     * @return El valor del node en forma de String.
     */
    private static String getContent(String etiqueta, Element element) {
        NodeList nodes = element.getElementsByTagName(etiqueta).item(0).getChildNodes();
        Node node = (Node) nodes.item(0);
        return node.getNodeValue();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters i setters">
    /**
     * Retorna l'estructura de dades que guarda objectes <code>Note</code> per 
     * gravar i reproduir.
     *
     * @return Llista d'objectes de la classe <code>Note</code>.
     */
    public List<Note> getNoteRecording() {
        return noteList;
    }

    /**
     * Retorna el comparador d'objectes <code>Note</code>.
     *
     * @return Objecte que implementa <code>Comparator</code>.
     */
    public NoteComparator getNc() {
        return nc;
    }

    /**
     * Retorna el nom del fitxer per guardar/reproduir.
     *
     * @return Valor <code>String</code> del nom del fitxer XML (sense extensió).
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Defineix l'objecte llista de notes.
     *
     * @param noteRecording Llista d'objectes de la classe <code>Note</code>.
     */
    public void setNoteRecording(NoteList<Note> noteRecording) {
        this.noteList = noteRecording;
    }

    /**
     * Defineix el comparador d'objectes <code>Note</code>.
     *
     * @param nc Objecte que implementa <code>Comparator</code>.
     */
    public void setNc(NoteComparator nc) {
        this.nc = nc;
    }

    /**
     * Defineix el nom del fitxer per guardar/reproduir notes.
     *
     * @param fileName Valor <code>String</code> del nom del fitxer XML (sense 
     * extensió).
     */
    public void setFileName(String fileName) {
        this.fileName = fileName + ApplicationConstants.EXT;
    }
    //</editor-fold>

}
