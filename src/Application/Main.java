/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Hp15
 */
public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Resources/FXMLDocument.fxml"));
        stage.setTitle("Point of Sale");
        stage.setOnCloseRequest(e -> closeApp()); 
        Scene scene = new Scene(root);
        //stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/Images/icon.png")));
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png"))); 
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void closeApp() {
        System.out.println("File saved");
    }
    
}
