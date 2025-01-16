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
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author mac
 */
public class FXMLSalesSummary implements Initializable {

    @FXML
    private AnchorPane summary;
    @FXML
    private Label summaryTitle, totalSummarySales, totalExpenses, totalSales;
    @FXML
    private TableView<SalesSummaryTable> summaryTable;
    @FXML
    private TableColumn<SalesSummaryTable, Integer> quantity;
    @FXML
    private TableColumn<SalesSummaryTable, String> item;
    @FXML
    private TableColumn<SalesSummaryTable, Double> price;

    public ObservableList<SalesSummaryTable> summaryList = FXCollections.observableArrayList();
    @FXML
    public TextArea printArea;

    public String salesDate, salesTotalReceipt;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void viewSummary(String summaryDate) {
        salesDate = summaryDate;
        String[] names = {};
        String[] values = {};
        double totalPrice = 0;
        ResultSet rs = SQLiteConnection.select("SELECT SUM(i.quantity) as quantity, i.item, SUM(i.total) as total FROM sales_items as i LEFT JOIN sales as s ON s.id = i.sales_id WHERE s.date = '" + summaryDate + "' GROUP BY i.item", names, values);
        try {
            while (rs.next()) {
                summaryList.add(new SalesSummaryTable(Integer.parseInt(rs.getString("quantity")), rs.getString("item"), Double.valueOf(rs.getString("total"))));
                totalPrice = totalPrice + Double.valueOf(rs.getString("total"));
            }
            salesTotalReceipt = String.valueOf(totalPrice);
            
            quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            item.setCellValueFactory(new PropertyValueFactory<>("item"));
            price.setCellValueFactory(new PropertyValueFactory<>("total"));
            summaryTable.setItems(summaryList);
            summaryTitle.setText("Sales Summary for " + summaryDate);

            //DecimalFormat formatter = new DecimalFormat("#,###.00");
            //totalSummarySales.setText("N" + formatter.format(totalPrice));
        } catch (SQLException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }

        String[] nameExp = {"date"};
        String[] valueExp = {summaryDate};
        double totalPriceExp = 0;
        ResultSet rst = SQLiteConnection.select("SELECT * FROM expenditure", nameExp, valueExp);
        try {
            while (rst.next()) {
                totalPriceExp = totalPriceExp + Double.valueOf(rst.getString("amount"));
            }

            //Display Total expenses for the day
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            totalSales.setText("N" + formatter.format(totalPrice));
            totalExpenses.setText("N" + formatter.format(totalPriceExp));
            //subtract sales for the date from the expenses
            Double netTotal = totalPrice - totalPriceExp;
            totalSummarySales.setText("N" + formatter.format(netTotal));
        } catch (SQLException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printSummary(ActionEvent evt) {
        HashMap<String, String> itemsHashMap = new HashMap<String, String>();
        HashMap<String, String> salesItem = new HashMap<String, String>();
        salesItem.put("customer_name", "Stock Details For");
        salesItem.put("date", salesDate);
        salesItem.put("totalSales", salesTotalReceipt);

        SalesSummaryTable sales;
        for (int i = 0; i < summaryTable.getItems().size(); i++) {
            sales = summaryTable.getItems().get(i);
            itemsHashMap.put(i + "_" + sales.getQuantity(), sales.getItem() + "_" + sales.getTotal());
        }
        Init.print(salesItem, itemsHashMap);
    }

}
