/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

import System.Init;
import System.SQLiteConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author mac
 */
public class Settings implements Initializable {

    @FXML
    private TextField companyName, companyAddress, companyPhone;
    
    ResultSet rs;
    PreparedStatement ps;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public void getSettings(){
        Connection conn = new SQLiteConnection().Connector();
        try {
            String[] names = {"id"};
            String[] values = {"1"};
            String sql = "SELECT * FROM settings WHERE id = '1'";
            ps = conn.prepareStatement(sql);
            rs= ps.executeQuery();
            if(rs.next()){
                companyName.setText(rs.getString("company_name"));
                companyAddress.setText(rs.getString("address"));
                companyPhone.setText(rs.getString("phone"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                rs.close();
                ps.close();
                //conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void saveSettings(ActionEvent evt){
        if(companyName.getText().trim().isEmpty()){
            Init.alertMsg("Message", "Please enter the Company Name");
        }else if(companyAddress.getText().trim().isEmpty()){
            Init.alertMsg("Message", "Please enter the Company Address");
        }else if(companyPhone.getText().trim().isEmpty()){
            Init.alertMsg("Message", "Please enter the Company Phone");
        }else{
            String name = companyName.getText().trim();
            String address = companyAddress.getText().trim();
            String phone = companyPhone.getText().trim();
            String[] values = {name, address, phone};
            String[] names = {"company_name", "address", "phone"};
            System.out.println(name + " " +  address + " " + phone);
            SQLiteConnection.update("settings", names, values, "id", 1);
            Init.alertMsg("Message", "Company Details updated");
        }
            
        
    }
    
}
