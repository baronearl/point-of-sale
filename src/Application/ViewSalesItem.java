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

public class ViewSalesItem implements Initializable {

    @FXML
    private Label nameField, dateField, totalField;
    @FXML
    private TableView<SalesItemTable> salesItemTable;
    @FXML
    private TableColumn<SalesItemTable, Integer> salesId;
    @FXML
    private TableColumn<SalesItemTable, String> salesItem;
    @FXML
    private TableColumn<SalesItemTable, Integer> salesQuantity;
    @FXML
    private TableColumn<SalesItemTable, Double> salesPrice;
    @FXML
    private TableColumn<SalesItemTable, Double> salesTotal;
    @FXML
    private TableColumn<SalesItemTable, String> salesCredit;

    public ObservableList<SalesItemTable> salesItemList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void ViewSales(Integer id, String customerName, Double total, String date) {
        nameField.setText(customerName);
        dateField.setText(date);

        String[] names = {"sales_id"};
        String[] values = {id.toString()};
        ResultSet rs = SQLiteConnection.select("SELECT * FROM sales_items", names, values);
        double totalPrice = 0;
        try {
            while (rs.next()) {
                salesItemList.add(new SalesItemTable(rs.getInt("id"), rs.getInt("quantity"), Double.valueOf(rs.getString("price")), rs.getString("item"), Double.valueOf(rs.getString("total")), rs.getString("credit")));
                totalPrice += Double.valueOf(rs.getString("total"));
            }
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            totalField.setText("N" + formatter.format(totalPrice));
            
            salesId.setCellValueFactory(new PropertyValueFactory<>("id"));
            salesItem.setCellValueFactory(new PropertyValueFactory<>("item"));
            salesQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            salesPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
            salesTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
            salesCredit.setCellValueFactory(new PropertyValueFactory<>("credit"));
            salesItemTable.setItems(salesItemList);
        } catch (SQLException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
