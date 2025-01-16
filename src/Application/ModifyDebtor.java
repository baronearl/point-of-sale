/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

import System.Init;
import System.SQLiteConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ModifyDebtor implements Initializable {

    private Integer debtorId;
    @FXML
    private TextField quantity, total;
    @FXML
    private AnchorPane root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setModifyDebtorView(Integer id) {
        this.debtorId = id;
        try {
            String[] names = {"id"};
            String[] values = {String.valueOf(id)};
            ResultSet rs = SQLiteConnection.select("SELECT * FROM sales_items", names, values);
            while (rs.next()) {
                quantity.setText(rs.getString("quantity"));
                total.setText(rs.getString("total"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveModification(ActionEvent evt) {
        if (quantity.getText().trim().isEmpty()) {
            Init.alertMsg("Missing field", "Please enter the Quantity");
        } else if (total.getText().trim().isEmpty()) {
            Init.alertMsg("Missing field", "Please enter the Total");
        } else {
            String qty = Init.filterPrice(quantity.getText().trim());
            String totalPrice = total.getText().trim();

            String[] names = {"quantity", "total"};
            String[] values = {qty, totalPrice};
            SQLiteConnection.update("sales_items", names, values, "id", this.debtorId);
            root.getScene().getWindow().hide();
            setModifyDebtorView(this.debtorId);
            Init.alertMsg("Success", "List updated successfully");
        }
    }

}
