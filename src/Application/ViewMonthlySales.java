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
import java.text.DecimalFormat;
import java.time.LocalDate;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ViewMonthlySales implements Initializable {

    @FXML
    private DatePicker salesDatePicker;
    @FXML
    private Label salesTotalAmount, totalSalesLabel, totalCredits, totalExpenses, netTotalSales;
    @FXML
    private TextField mYearSelect;
    @FXML
    private ComboBox monthSelect;
    @FXML
    private TableView<SalesTable> salesTable, debtorSalesTable;
    @FXML
    private TableColumn<SalesTable, Integer> salesId, debtorSalesId;
    @FXML
    private TableColumn<SalesTable, String> salesCustomerName, debtorSalesCustomerName;
    @FXML
    private TableColumn<SalesTable, Double> salesTotalPrice, debtorSalesTotalPrice;
    @FXML
    private TableColumn<SalesTable, String> salesDate, debtorSalesDate;
    @FXML
    private TableView<ExpensesTable> expensesTable;
    @FXML
    private TableColumn<ExpensesTable, Integer> expensesId;
    @FXML
    private TableColumn<ExpensesTable, Double> expensesAmount;
    @FXML
    private TableColumn<ExpensesTable, String> expensesComment;
    @FXML
    private TableColumn<ExpensesTable, String> expensesSales;
    @FXML
    private TableColumn<ExpensesTable, String> expensesDate;

    public ObservableList<SalesTable> salesList = FXCollections.observableArrayList();
    public ObservableList<Products> productsList = FXCollections.observableArrayList();
    private ObservableList<Integer> monthOptions = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
    public ObservableList<ExpensesTable> expensesList = FXCollections.observableArrayList();
    public ObservableList<SalesTable> creditsList = FXCollections.observableArrayList();
    private String summaryDate;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        monthSelect.setItems(monthOptions);
        viewAllSales();
        viewAllCredits();
        viewAllExpenses();
    }

    public void salesViewDetails(ActionEvent evt) {
        ObservableList<SalesTable> salesSelected, allSales;
        allSales = salesTable.getItems();
        salesSelected = salesTable.getSelectionModel().getSelectedItems();
        if (salesSelected.isEmpty()) {
            Init.alertMsg("No selection made", "Please select an Item from the Table first");
        } else {
            SalesTable sales = salesSelected.get(0);
            try {
                Stage primaryStage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                Pane root = loader.load(getClass().getResource("/Resources/FXMLViewSalesDetails.fxml").openStream());
                ViewSalesItem viewSales = (ViewSalesItem) loader.getController();
                viewSales.ViewSales(sales.getId(), sales.getCustomer_name(), Double.valueOf(sales.getTotal()), sales.getDate());
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.setTitle("View Sales Details");
                primaryStage.setMaxWidth(600.0);
                primaryStage.setMaxHeight(600.0);
                primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
                primaryStage.show();
            } catch (IOException ex) {
                Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void clearSearch(ActionEvent evt) {
        salesList.clear();
        salesDatePicker.setValue(null);
        viewAllSales();
        salesTotalAmount.setText("");
        totalSalesLabel.setText("");
    }

    public void searchSales(ActionEvent evt) {
        if (monthSelect.getValue() == null) {
            Init.alertMsg("Missing field", "Please select month");
        } else if (mYearSelect.getText().trim().isEmpty()) {
            Init.alertMsg("Missing field", "Please select year");
        } else {
            salesList.clear();
            String monthYear = monthSelect.getValue() + "-" + mYearSelect.getText();
            String[] names = {};
            String[] values = {};
            double totalPrice = 0;

            ResultSet rs = SQLiteConnection.select("SELECT s.id, s.customer_name, s.date, SUM(si.total) as total FROM sales as s LEFT JOIN sales_items as si ON s.id = si.sales_id WHERE s.date LIKE '%" + monthYear + "' GROUP BY si.sales_id ORDER BY s.id DESC", names, values);
            try {
                while (rs.next()) {
                    salesList.add(new SalesTable(rs.getInt("id"), rs.getString("customer_name"), Double.valueOf(rs.getString("total")), rs.getString("date")));
                    totalPrice = totalPrice + Double.valueOf(rs.getString("total"));
                }
                salesId.setCellValueFactory(new PropertyValueFactory<>("id"));
                salesCustomerName.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
                salesTotalPrice.setCellValueFactory(new PropertyValueFactory<>("total"));
                salesDate.setCellValueFactory(new PropertyValueFactory<>("date"));
                salesTable.setItems(salesList);

                DecimalFormat formatter = new DecimalFormat("#,###.00");
                salesTotalAmount.setText("N" + formatter.format(totalPrice));
                totalSalesLabel.setText("Total");
                
               Double credits = getCredits(monthYear);
               Double expenses = getExpenses(monthYear);
               
               Double netTotal = totalPrice - (credits + expenses);
               netTotalSales.setText("N" + formatter.format(netTotal));
            } catch (SQLException ex) {
                Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void viewAllSales() {
        productsList.clear();
        String[] names = {};
        String[] values = {};
        ResultSet rs = SQLiteConnection.select("SELECT s.id, s.customer_name, s.date, SUM(si.total) as total FROM sales as s LEFT JOIN sales_items as si ON s.id = si.sales_id GROUP BY si.sales_id ORDER by s.id DESC LIMIT 100", names, values);
        try {
            while (rs.next()) {
                salesList.add(new SalesTable(rs.getInt("id"), rs.getString("customer_name"), Double.valueOf(rs.getString("total")), rs.getString("date")));
            }
            salesId.setCellValueFactory(new PropertyValueFactory<>("id"));
            salesCustomerName.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
            salesTotalPrice.setCellValueFactory(new PropertyValueFactory<>("total"));
            salesDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            salesTable.setItems(salesList);
        } catch (SQLException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
    private void viewAllCredits() {
        creditsList.clear();
        String[] names = {};
        String[] values = {};
        ResultSet rs = SQLiteConnection.select("SELECT s.id, s.customer_name, s.date, SUM(si.total) as total FROM sales as s LEFT JOIN sales_items as si ON s.id = si.sales_id WHERE si.credit = 'Yes' GROUP BY si.sales_id ORDER BY s.id DESC LIMIT 100", names, values);
        try {
            while (rs.next()) {
                creditsList.add(new SalesTable(rs.getInt("id"), rs.getString("customer_name"), Double.valueOf(rs.getString("total")), rs.getString("date")));
            }
            debtorSalesId.setCellValueFactory(new PropertyValueFactory<>("id"));
            debtorSalesCustomerName.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
            debtorSalesTotalPrice.setCellValueFactory(new PropertyValueFactory<>("total"));
            debtorSalesDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            debtorSalesTable.setItems(creditsList);
        } catch (SQLException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private Double getCredits(String date){
        creditsList.clear();
        this.summaryDate = date;
        String[] names = {};
        String[] values = {};
        double totalPrice = 0;
        ResultSet rs = SQLiteConnection.select("SELECT s.id, s.customer_name, s.date, SUM(si.total) as total FROM sales as s LEFT JOIN sales_items as si ON s.id = si.sales_id WHERE si.credit = 'Yes' AND date LIKE '%" + date + "' GROUP BY si.sales_id", names, values);
        try {
            while (rs.next()) {
                creditsList.add(new SalesTable(rs.getInt("id"), rs.getString("customer_name"), Double.valueOf(rs.getString("total")), rs.getString("date")));
                totalPrice = totalPrice + Double.valueOf(rs.getString("total"));
            }
            debtorSalesId.setCellValueFactory(new PropertyValueFactory<>("id"));
            debtorSalesCustomerName.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
            debtorSalesTotalPrice.setCellValueFactory(new PropertyValueFactory<>("total"));
            debtorSalesDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            debtorSalesTable.setItems(creditsList);

            DecimalFormat formatter = new DecimalFormat("#,###.00");
            totalCredits.setText("N" + formatter.format(totalPrice));
        } catch (SQLException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return totalPrice;
    }
    
    
    private void viewAllExpenses(){
        expensesList.clear();
        String[] names = {};
        String[] values = {};
        ResultSet rs = SQLiteConnection.select("SELECT * FROM expenditure ORDER BY id DESC LIMIT 100", names, values);
        try {
            while (rs.next()) {
                expensesList.add(new ExpensesTable(rs.getInt("id"), Double.valueOf(rs.getString("amount")), rs.getString("comment"), rs.getString("sales_person"), rs.getString("date")));
            }
            expensesId.setCellValueFactory(new PropertyValueFactory<>("id"));
            expensesAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
            expensesComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
            expensesSales.setCellValueFactory(new PropertyValueFactory<>("sales_person"));
            expensesDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            expensesTable.setItems(expensesList);
        } catch (SQLException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Double getExpenses(String date){
        expensesList.clear();
        String[] names = {};
        String[] values = {};
        double totalPrice = 0;
        ResultSet rs = SQLiteConnection.select("SELECT * FROM expenditure WHERE date LIKE '%" + date + "'", names, values);
        try {
            while (rs.next()) {
                expensesList.add(new ExpensesTable(rs.getInt("id"), Double.valueOf(rs.getString("amount")), rs.getString("comment"), rs.getString("sales_person"), rs.getString("date")));
                totalPrice = totalPrice + Double.valueOf(rs.getString("amount"));
            }
            expensesId.setCellValueFactory(new PropertyValueFactory<>("id"));
            expensesAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
            expensesComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
            expensesSales.setCellValueFactory(new PropertyValueFactory<>("sales_person"));
            expensesDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            expensesTable.setItems(expensesList);

            DecimalFormat formatter = new DecimalFormat("#,###.00");
            totalExpenses.setText("N" + formatter.format(totalPrice));
        } catch (SQLException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return totalPrice;
    }

}
