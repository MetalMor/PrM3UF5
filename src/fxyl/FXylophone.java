package fxyl;

import constants.Constants;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe que hereta d'Application. Per executar el programa del xilòfon.
 *
 * 220116
 * @author mor
 */
public class FXylophone extends Application {
    
    
    
    public static void main(String[] args) {
        Application.launch(FXylophone.class, args);
    }
    
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
}