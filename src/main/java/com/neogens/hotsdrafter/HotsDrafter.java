package com.neogens.hotsdrafter;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author François
 */
public class HotsDrafter extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));      
        Scene scene = new Scene(root);       
        stage.setScene(scene);
        stage.show();
    }
 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        launch(args);  
       
    }

}
