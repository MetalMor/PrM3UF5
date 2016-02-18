package fxyl;

import constants.Constants;
import exc.WrongNoteException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Classe parametritzada (que podria no ser-ho) que implementa la inserció i
 * extracció d'objectes Note a un fitxer XML.
 * 
 * Help: http://java.sys-con.com/node/37803
 * 
 * EN PROCESO CHAVALES, NO SÉ SI ESTO SERÁ ÚTIL PERO IMPLEMENTA VARIOS
 * REQUISITOS DE LA APLICACIÓN
 *
 * 020216
 * @author mor
 */
public class FXylophoneXML<T> extends ArrayList {
    
    private T t;
    
    private List<Note> noteRecording = new ArrayList<>();
    private List<Note> notePlaying = new ArrayList<>();
    private NoteComparator nc = new NoteComparator();
    
    private String fileName;
    
    public boolean recordNote(Note n) {
        
        if(!noteRecording.add(n))
            return false;
        
        return true;
        
    }
    
    public FXylophoneXML() { }
    
    public FXylophoneXML(ArrayList<Note> noteRecording) {
        
        this.noteRecording = noteRecording;
        
    }
    
    /**
     * Funció que construeix un element <Note>.
     * Per tal de poder guardar les dades dels missatges MIDI a un XML, cal
     * convertir-les a un format XML. Aquesta funció s'encarrega de crear un 
     * element XML a partir d'una nota enviat per paràmetre.
     * 
     * @param n Nota de MIDI.
     * @param doc Objecte Document al qual s'afegeix l'element.
     * @return Element
     */
    public Element XMLcreateNoteElement(Note n, Document doc) {
        
        Element note = doc.createElement("Note");
        
        System.out.println(Integer.toString(n.getValue()));
        System.out.println(Long.toString(n.getTimestamp()));
        
        /**
         * Elements fills de <Note>
         */
        Element value = doc.createElement("Value");
        value.appendChild(
                doc.createTextNode(Integer.toString(n.getValue())));
        Element timestamp = doc.createElement("Timestamp");
        timestamp.appendChild(
                doc.createTextNode(Long.toString(n.getTimestamp())));
        
        note.appendChild(value);
        note.appendChild(timestamp);
        
        return note;
            
    }
    
    /**
     * Funció Note -> XML.
     * Guarda les dades d'un conjunt d'objectes Note en un document XML 
     * utilitzant l'API DOM.
     * 
     * @throws ParserConfigurationException
     * @throws WrongNoteException
     * @throws TransformerException 
     */
    public void notesToXML() throws ParserConfigurationException, 
            WrongNoteException, TransformerException {
        
        noteRecording.sort(nc);
        
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        
        Document docNode = docBuilder.newDocument();
        Element rootElement = docNode.createElement("SoundFile");
        docNode.appendChild(rootElement);
        
        for (Note note : noteRecording) {
            
            /**
             * Si no és una nota de MIDI, llença una excepció.
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
        StreamResult outXML = new StreamResult(new File(this.fileName));

        transf.transform(orig, outXML);
        
    }
    
    /**
     * Funció per extreure un conjunt de notes d'un fitxer XML.
     * Per reproduir les notes guardades a un arxiu XML s'han de desar en una
     * llista d'objectes Note.
     * 
     * @return
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws WrongNoteException 
     */
    public List<Note> XMLtoNotes() throws IOException, SAXException, 
            ParserConfigurationException, WrongNoteException {
        
        File midiXml = new File(this.getFileName());
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(midiXml);
        doc.getDocumentElement().normalize();

        NodeList nodes = doc.getElementsByTagName("Note");
        
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                
                Element element = (Element) node;
                Note noteToPlay = new NoteImpl(
                        Integer.parseInt(getContent("Value", element)),
                        Long.parseLong(getContent("Timestamp", element))
                );
                
                if(!recordNote(noteToPlay))
                    throw new WrongNoteException();
                
            }
        }
        
        notePlaying.sort(nc);
        
        for (Note note : notePlaying)
            System.out.println(note);
        
        return notePlaying;
        
    }
    
    /**
     * Funció per recuperar el valor d'un node XML.
     * 
     * @param etiqueta Node que desitgem extreure de l'XML.
     * @param element Element a on cercar el node.
     * @return El valor del node en forma de String.
     */
    private static String getContent(String etiqueta, Element element) {
        NodeList nodes = element.getElementsByTagName(etiqueta).item(0).getChildNodes();
        Node node = (Node) nodes.item(0);
        return node.getNodeValue();
    }
    
    public void setT(T t) {
        this.t = t;
    }

    public void setNoteRecording(ArrayList<Note> noteRecording) {
        this.noteRecording = noteRecording;
    }

    public void setNotePlaying(List<Note> notePlaying) {
        this.notePlaying = notePlaying;
    }

    public void setNc(NoteComparator nc) {
        this.nc = nc;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName + Constants.EXT;
    }
    
    public T getT() {
        return t;
    }

    public List<Note> getNoteRecording() {
        return noteRecording;
    }

    public List<Note> getNotePlaying() {
        return notePlaying;
    }

    public NoteComparator getNc() {
        return nc;
    }

    public String getFileName() {
        return fileName;
    }
    
}
