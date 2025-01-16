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
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

public class ViewStock implements Initializable {

    @FXML
    private Button printStockBtn;
    @FXML
    private TableView<StockDetailsTable> stockDetailsTable;
    @FXML
    private TableColumn<StockDetailsTable, String> stockDetailsProduct;
    @FXML
    private TableColumn<StockDetailsTable, Integer> stockDetailsQty;
    @FXML
    private TableView<StockHistoryTable> stockHistoryTable;
    @FXML
    private TableColumn<StockHistoryTable, Integer> historyId;
    @FXML
    private TableColumn<StockHistoryTable, String> historyName;
    @FXML
    private TableColumn<StockHistoryTable, Integer> historyQty;
    @FXML
    private TableColumn<StockHistoryTable, String> historyDate;

    public ObservableList<StockDetailsTable> stockList = FXCollections.observableArrayList();
    public ObservableList<StockHistoryTable> historyList = FXCollections.observableArrayList();
    @FXML
    private TextArea printingArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getView();
        getHistory();
        printStockBtn.setGraphic(new ImageView("/Images/printer.png"));
    }

    public void getView() {
        String[] names = {};
        String[] values = {};
        ResultSet rs = SQLiteConnection.select("SELECT name, quantity FROM products", names, values);
        try {
            while (rs.next()) {
                System.out.println(rs.getString("name"));
                stockList.add(new StockDetailsTable(rs.getString("name"), rs.getInt("quantity")));
            }
            stockDetailsProduct.setCellValueFactory(new PropertyValueFactory<>("name"));
            stockDetailsQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            stockDetailsTable.setItems(stockList);
        } catch (SQLException ex) {
            Logger.getLogger(ViewStock.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getHistory(){
        String[] names = {};
        String[] values = {};
        ResultSet rs = SQLiteConnection.select("SELECT * FROM stock_history ORDER by id DESC LIMIT 100", names, values);
        try {
            while (rs.next()) {
                //System.out.println(rs.getString("name"));
                historyList.add(new StockHistoryTable(rs.getInt("id"), rs.getString("product_name"), rs.getInt("quantity"), rs.getString("date")));
            }
            historyId.setCellValueFactory(new PropertyValueFactory<>("id"));
            historyName.setCellValueFactory(new PropertyValueFactory<>("product_name"));
            historyQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            historyDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            stockHistoryTable.setItems(historyList);
        } catch (SQLException ex) {
            Logger.getLogger(ViewStock.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void printStock(ActionEvent evt){
        HashMap<String, String> itemsHashMap = new HashMap<String, String>();
        
        String title = "Stock List \n";
        StockDetailsTable stock;
            for (int i = 0; i < stockDetailsTable.getItems().size(); i++) {
                stock = stockDetailsTable.getItems().get(i);
                itemsHashMap.put(i + "_" + stock.getQuantity(), stock.getName());
            }   
        Init.printStock(itemsHashMap, title);
    }

}
