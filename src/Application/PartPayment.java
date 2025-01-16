/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

import System.Init;
import System.SQLiteConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author mac
 */
public class PartPayment implements Initializable {

    private String debtorName;
    @FXML
    private TextField debtorsName, debtorsAmount;
    @FXML
    private DatePicker debtorsDate;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        debtorsDate.setValue(LocalDate.now());
    }

    public void setDebtorName(String debtorName) {
        debtorsName.setText(debtorName); 
    }
    
    public void updatePartPayment(ActionEvent evt){
        if (debtorsName.getText().trim().isEmpty()) {
            Init.alertMsg("Missing field", "Please enter the Debtor's Name");
        } else if (debtorsAmount.getText().trim().isEmpty()) {
            Init.alertMsg("Missing field", "Please enter the Amount");
        } else if(debtorsDate.getValue() == null){
             Init.alertMsg("Missing field", "Please enter the Date");
        }else{
            String name = debtorsName.getText().trim();
            String amount = debtorsAmount.getText().trim();
            LocalDate ld = debtorsDate.getValue();
            String date = Init.dateFormat(ld);
            String[] names = {"customer_name", "amount", "date"};
            String[] values = {name, amount, date};
            SQLiteConnection.insert("part_payment_history", names, values);
            Init.alertMsg("Message", "Payment Updated");
        }
    }
    
    public void handleStringDigits() {
        if (!debtorsAmount.getText().trim().matches("\\d*")) {
            debtorsAmount.setText(debtorsAmount.getText().trim().replaceAll("[^\\d]", ""));
        }
    }

}
