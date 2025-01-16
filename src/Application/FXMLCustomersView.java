/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

import System.Init;
import System.SQLiteConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mac
 */
public class FXMLCustomersView implements Initializable {

    public ObservableList<CustomersViewTable> customersList = FXCollections.observableArrayList();

    @FXML
    private TextField searchText;
    @FXML
    private TableView<CustomersViewTable> customersTable;
    @FXML
    private TableColumn<CustomersViewTable, Integer> id;
    @FXML
    private TableColumn<CustomersViewTable, String> customer_name;
    @FXML
    private TableColumn<CustomersViewTable, Double> total;
    Boolean autodetect = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getCustomers();
    }

    public void getCustomers() {
        customersList.clear();
        String[] names = {};
        String[] values = {};
        ResultSet rs = SQLiteConnection.select("SELECT id, customer_name, SUM(total) as total FROM sales GROUP BY customer_name", names, values);
        try {
            while (rs.next()) {
                customersList.add(new CustomersViewTable(rs.getInt("id"), rs.getString("customer_name"), Double.valueOf(rs.getString("total"))));
            }
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            customer_name.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
            total.setCellValueFactory(new PropertyValueFactory<>("total"));
            customersTable.setItems(customersList);
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void viewCustomer(ActionEvent evt) {
        ObservableList<CustomersViewTable> customersSelected;
        customersSelected = customersTable.getSelectionModel().getSelectedItems();
        if (customersSelected.isEmpty()) {
            Init.alertMsg("No selection made", "Please select Customer from the Table first");
        } else {
            CustomersViewTable customers = customersSelected.get(0);
            try {
                Stage primaryStage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                Pane root = loader.load(getClass().getResource("/Resources/FXMLCustomerHistory.fxml").openStream());
                FXMLCustomerHistory cHistory = (FXMLCustomerHistory) loader.getController();
                cHistory.getHistory(customers.getCustomer_name());
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.setTitle("Customer: (" + customers.getCustomer_name() + ")");
                primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
                primaryStage.show();
            } catch (IOException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void handleSearch() {
        autodetect = true;
        searchText();
    }

    public void searchText() {
        String search = Init.filterStrings(searchText.getText().trim());
        String[] names = {};
        String[] values = {};
        //ResultSet rs = SQLiteConnection.select("SELECT id, customer_name, SUM(total) as total FROM sales GROUP BY customer_name", names, values);
        ResultSet rs = SQLiteConnection.select("SELECT id, customer_name, SUM(total) as total FROM sales WHERE customer_name LIKE '%" + search + "%' GROUP by customer_name", names, values);
        try {
            customersList.clear();
            while (rs.next()) {
                customersList.add(new CustomersViewTable(rs.getInt("id"), rs.getString("customer_name"), Double.valueOf(rs.getString("total"))));
            }
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            customer_name.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
            total.setCellValueFactory(new PropertyValueFactory<>("total"));
            customersTable.setItems(customersList);
            if (!autodetect && customersList.size() == 0) {
                Init.alertMsg("Not found", "Customer name not found");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void searchCustomer(ActionEvent evt) {
        autodetect = false;
        if (searchText.getText().trim().isEmpty()) {
            Init.alertMsg("Missing field", "Please enter the Name first");
        } else {
            searchText();
        }
    }

}
