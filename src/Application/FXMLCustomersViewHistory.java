/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

import System.SQLiteConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author mac
 */
public class FXMLCustomersViewHistory implements Initializable {

    @FXML
    private Label nameField, dateField, totalField;
    
    private Integer id;
    
    @FXML
    private TableView<SalesItemTable> customersItemTable;
    @FXML
    private TableColumn<SalesItemTable, Integer> customersId;
    @FXML
    private TableColumn<SalesItemTable, String> customersItem;
    @FXML
    private TableColumn<SalesItemTable, Integer> customersQuantity;
    @FXML
    private TableColumn<SalesItemTable, Double> customersPrice;
    @FXML
    private TableColumn<SalesItemTable, Double> customersTotal;
    @FXML
    private TableColumn<SalesItemTable, String> customersCredit;

    public ObservableList<SalesItemTable> customersItemList = FXCollections.observableArrayList();

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    void getViewCustomers(Integer id, String customerName, Double total, String date) {
       this.id = id;
        nameField.setText(customerName);
        dateField.setText(date);
        getCustomersHistory();
    }

    private void getCustomersHistory() {
        customersItemList.clear();
        String[] names = {};
        String[] values = {};
        ResultSet rs = SQLiteConnection.select("SELECT id, quantity, price, item, total, credit, SUM(total) as net_total FROM sales_items WHERE sales_id = '" + this.id + "' GROUP BY id", names, values);
        double totalPrice = 0;
        try {
            while (rs.next()) {
                customersItemList.add(new SalesItemTable(rs.getInt("id"), rs.getInt("quantity"), Double.valueOf(rs.getString("price")), rs.getString("item"), Double.valueOf(rs.getString("total")), rs.getString("credit")));
                totalPrice = totalPrice + Double.valueOf(rs.getString("total"));
            }
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            totalField.setText("N" + formatter.format(totalPrice));

            customersId.setCellValueFactory(new PropertyValueFactory<>("id"));
            customersItem.setCellValueFactory(new PropertyValueFactory<>("item"));
            customersQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            customersPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
            customersTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
            customersCredit.setCellValueFactory(new PropertyValueFactory<>("credit"));
            customersItemTable.setItems(customersItemList);
        } catch (SQLException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
