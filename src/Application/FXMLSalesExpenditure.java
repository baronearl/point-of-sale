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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class FXMLSalesExpenditure implements Initializable {

    @FXML
    private DatePicker expensesDate;
    @FXML
    private TextField expensesAmount;
    @FXML
    private TextArea expensesComment;
    private String currentUser;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        expensesDate.setValue(LocalDate.now());
    } 
    
    public void saveExpenses(ActionEvent evt){
        if (expensesAmount.getText().trim().isEmpty()) {
            Init.alertMsg("Missing field", "Please enter the Amount");
        } else if (expensesDate.getValue() == null) {
            Init.alertMsg("Missing field", "Please select the Date");
        } else if (expensesComment.getText().trim().isEmpty()) {
            Init.alertMsg("Missing field", "Please enter the Comment/Reason for the Expenses");
        } else {
            String amount = expensesAmount.getText().trim();
            LocalDate ld = expensesDate.getValue();
            String date = Init.dateFormat(ld);
            String comment = expensesComment.getText().trim();
            
            String[] names = {"amount", "comment", "date", "sales_person"};
            String[] values = {amount, comment, date, this.currentUser};
            SQLiteConnection.insert("expenditure", names, values);
            Init.alertMsg("Message", "Expenses Saved");
            expensesAmount.clear();
            expensesComment.clear();
        }
    }

    void setUser(String currentUser) {
        this.currentUser = currentUser;
    }
    
}
