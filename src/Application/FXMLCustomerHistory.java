/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

import Application.CustomerHistoryTable;
import Application.DebtorsHistoryTable;
import System.Init;
import System.SQLiteConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class FXMLCustomerHistory implements Initializable {
    
    @FXML
    private TableView<CustomerHistoryTable> customerHistoryTable;
    @FXML
    private TableColumn<CustomerHistoryTable, Integer> customerHistorySN;
    @FXML
    private TableColumn<CustomerHistoryTable, String> customerHistorCustomerName;
    @FXML
    private TableColumn<CustomerHistoryTable, Double> customerHistoryTotal;
    @FXML
    private TableColumn<CustomerHistoryTable, String> customerHistoryDate;
    
    @FXML
    private Label titleLabel, totalCustomerHistory;
    
    public ObservableList<CustomerHistoryTable> customerItemList = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void getHistory(String customer_name) {
        customerItemList.clear();
        String[] names = {};
        String[] values = {};
        ResultSet rs = SQLiteConnection.select("SELECT s.id, s.customer_name, SUM(i.total) as total, s.date FROM sales as s LEFT JOIN sales_items as i ON s.id = i.sales_id WHERE s.customer_name = '" + customer_name + "' GROUP BY i.sales_id", names, values);
        double totalPrice = 0;
        try {
            while (rs.next()) {
                customerItemList.add(new CustomerHistoryTable(rs.getInt("id"), rs.getString("customer_name"), Double.valueOf(rs.getString("total")), rs.getString("date")));
                totalPrice = totalPrice + Double.valueOf(rs.getString("total"));
            }
            
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            totalCustomerHistory.setText("N" + formatter.format(totalPrice));
            titleLabel.setText(customer_name + " Sales History");
            
            customerHistorySN.setCellValueFactory(new PropertyValueFactory<>("id"));
            customerHistorCustomerName.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
            customerHistoryTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
            customerHistoryDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            customerHistoryTable.setItems(customerItemList);
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void viewDetailedHistory(ActionEvent evt){
        ObservableList<CustomerHistoryTable> customersSelected;
        customersSelected = customerHistoryTable.getSelectionModel().getSelectedItems();
        if (customersSelected.isEmpty()) {
            Init.alertMsg("No selection made", "Please select Customer from the Table first");
        } else {
            try {
                CustomerHistoryTable customer = customersSelected.get(0);
                Stage primaryStage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                Pane root = loader.load(getClass().getResource("/Resources/FXMLCustomersViewHistory.fxml").openStream());
                FXMLCustomersViewHistory viewCustomers = (FXMLCustomersViewHistory) loader.getController();
                viewCustomers.getViewCustomers(customer.getId(), customer.getCustomer_name(), Double.valueOf(customer.getTotal()), customer.getDate());
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.setTitle("View Customer Details");
                //primaryStage.setMaxWidth(600.0);
                //primaryStage.setMaxHeight(590.0);
                primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
                primaryStage.show();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDebtorHistory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
