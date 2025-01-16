/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

import System.Init;
import System.MYSQLConnection;
import System.SQLiteConnection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author mac
 */
public class DevelopersController implements Initializable {

    @FXML
    private TextField localIpAddress, localDBName, localDBUsername, onlineIpAddress, onlineDBName, onlineDBUsername;
    @FXML
    private PasswordField localDBPassword, onlineDBPassword;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void saveLocalConfig(ActionEvent evt) {
        if (localIpAddress.getText().trim().isEmpty()) {
            Init.alertMsg("Message", "Please enter the IP Address");
        } else if (localDBName.getText().trim().isEmpty()) {
            Init.alertMsg("Message", "Please enter the Database Name");
        } else if (localDBUsername.getText().trim().isEmpty()) {
            Init.alertMsg("Message", "Please enter the Database Username");
        } else if (localDBPassword.getText().trim().isEmpty()) {
            Init.alertMsg("Message", "Please enter the Database Password");
        } else {
            try {
                String ipAddress = localIpAddress.getText().trim();
                String dbName = localDBName.getText().trim();
                String dbUsername = localDBUsername.getText().trim();
                String dbPassword = localDBPassword.getText().trim();

                SerializeLocalObjects sContents = new SerializeLocalObjects(ipAddress, dbName, dbUsername, dbPassword);
                FileOutputStream fout = new FileOutputStream(new File("phoenixPOSConfig/config.txt"));
                ObjectOutputStream out = new ObjectOutputStream(fout);
                out.writeObject(sContents);
                out.flush();
                out.close();
                //String s = ipAddress + ":3306/"+dbName+","+dbUsername+","+dbPassword;
                //byte b[] = s.getBytes();
                //fout.write(b);
                //fout.close();
                Init.alertMsg("Message", "Configuration settings saved");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void checkLocalConnection(ActionEvent evt) {
        try {
            SQLiteConnection.isConnected();
            /**String currentDirectory = System.getProperty("user.dir");
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(currentDirectory + "config.txt"));
            SerializeLocalObjects sContents = (SerializeLocalObjects) in.readObject();
            Init.alertMsg("Message", sContents.ipAddress + " " + sContents.dbName + " " + sContents.dbUsername + " " + sContents.dbPassword);
            */
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    
    public void saveOnlineConfig(ActionEvent evt) {
        if (onlineIpAddress.getText().trim().isEmpty()) {
            Init.alertMsg("Message", "Please enter the Online IP Address");
        } else if (onlineDBName.getText().trim().isEmpty()) {
            Init.alertMsg("Message", "Please enter the Online Database Name");
        } else if (onlineDBUsername.getText().trim().isEmpty()) {
            Init.alertMsg("Message", "Please enter the Online Database Username");
        } else if (onlineDBPassword.getText().trim().isEmpty()) {
            Init.alertMsg("Message", "Please enter the Online Database Password");
        } else {
            try {
                String ipAddress = onlineIpAddress.getText().trim();
                String dbName = onlineDBName.getText().trim();
                String dbUsername = onlineDBUsername.getText().trim();
                String dbPassword = onlineDBPassword.getText().trim();

                SerializeLocalObjects sContents = new SerializeLocalObjects(ipAddress, dbName, dbUsername, dbPassword, "online");
                FileOutputStream fout = new FileOutputStream(new File("phoenixPOSConfig/configOln.txt"));
                ObjectOutputStream out = new ObjectOutputStream(fout);
                out.writeObject(sContents);
                out.flush();
                out.close();
                //String s = ipAddress + ":3306/"+dbName+","+dbUsername+","+dbPassword;
                //byte b[] = s.getBytes();
                //fout.write(b);
                //fout.close();
                Init.alertMsg("Message", "Configuration settings saved");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    
    public void checkOnlineConnection(ActionEvent evt) {
        try {
            MYSQLConnection.isConnected();
            /**String currentDirectory = System.getProperty("user.dir");
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(currentDirectory + "config.txt"));
            SerializeLocalObjects sContents = (SerializeLocalObjects) in.readObject();
            Init.alertMsg("Message", sContents.ipAddress + " " + sContents.dbName + " " + sContents.dbUsername + " " + sContents.dbPassword);
            */
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
