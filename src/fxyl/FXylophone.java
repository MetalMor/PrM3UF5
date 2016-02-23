package fxyl;

//<editor-fold defaultstate="collapsed" desc="Imports.">
import constants.Constants;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
//</editor-fold>

/**
 * Classe que hereta d'Application. Per executar el programa del xilòfon.
 *
 * @version 220116
 * @author mor
 */
public class FXylophone extends Application {
    
    //<editor-fold defaultstate="collapsed" desc="Mètode principal.">
    /**
     * Mètode principal de l'aplicació.
     *
     * @param args Array d'arguments de la classe String.
     */
    public static void main(String[] args) {
        Application.launch(FXylophone.class, args);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Mètode inicialitzador de l'aplicació.">
    /**
     * Mètode cridat per obrir l'aplicació.
     *
     * @param stage Context gràfic de l'aplicació.
     */
    @Override
    public void start(Stage stage) {
        
        Parent arrel;
        try {
            
            arrel = FXMLLoader.load(getClass().getResource(Constants.VIEW_FILE));
            
            stage.setTitle(Constants.TITLE);
            stage.setScene(new Scene(arrel, Constants.W_WIDTH, Constants.W_HEIGHT));
            stage.show();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
//</editor-fold>
    
}
