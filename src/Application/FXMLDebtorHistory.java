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
import java.util.HashMap;
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
import javafx.scene.control.TextArea;
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
public class FXMLDebtorHistory implements Initializable {

    @FXML
    private TableView<DebtorsHistoryTable> debtHistoryTable;
    @FXML
    private TableColumn<DebtorsHistoryTable, Integer> debtorHistorySN;
    @FXML
    private TableColumn<DebtorsHistoryTable, String> debtorHistorCustomerName;
    @FXML
    private TableColumn<DebtorsHistoryTable, Double> debtorHistoryTotal;
    @FXML
    private TableColumn<DebtorsHistoryTable, String> debtorHistoryDate;

    @FXML
    private TableView<PartPaymentTable> paymentHistoryTable;
    @FXML
    private TableColumn<PartPaymentTable, Integer> paymentHistorySN;
    @FXML
    private TableColumn<PartPaymentTable, String> paymentHistoryName;
    @FXML
    private TableColumn<PartPaymentTable, Double> paymentHistoryAmount;
    @FXML
    private TableColumn<PartPaymentTable, String> paymentHistoryDate;

    public ObservableList<DebtorsHistoryTable> debtorsItemList = FXCollections.observableArrayList();
    public ObservableList<PartPaymentTable> paymentList = FXCollections.observableArrayList();

    @FXML
    private Label totalDebtHistory, totalPartPayment, debtNetTotal;
    @FXML
    private TextArea partPaymentTextArea;

    private String customer;
    
    public Double difference;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void getHistory(String customer_name) {
        this.customer = customer_name;
        debtorsItemList.clear();
        String[] names = {};
        String[] values = {};
        ResultSet rs = SQLiteConnection.select("SELECT s.id, s.customer_name, SUM(i.total) as total, s.date FROM sales as s LEFT JOIN sales_items as i ON s.id = i.sales_id WHERE s.customer_name = '" + customer_name + "' AND i.credit = 'Yes' GROUP BY i.sales_id", names, values);
        double totalPrice = 0;
        try {
            while (rs.next()) {
                System.out.println(rs.getString("customer_name") + " " + customer_name);
                debtorsItemList.add(new DebtorsHistoryTable(rs.getInt("id"), rs.getString("customer_name"), Double.valueOf(rs.getString("total")), rs.getString("date")));
                totalPrice = totalPrice + Double.valueOf(rs.getString("total"));
            }
            //Get the payment history
            getPartPayment(customer_name, totalPrice);
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            totalDebtHistory.setText("N" + formatter.format(totalPrice));

            debtorHistorySN.setCellValueFactory(new PropertyValueFactory<>("id"));
            debtorHistorCustomerName.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
            debtorHistoryTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
            debtorHistoryDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            debtHistoryTable.setItems(debtorsItemList);
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getPartPayment(String customer_name, Double debtTotalPrice) {
        paymentList.clear();
        String[] names = {"customer_name"};
        String[] values = {customer_name};
        ResultSet rs = SQLiteConnection.select("SELECT * FROM part_payment_history", names, values);
        double totalPrice = 0;
        try {
            while (rs.next()) {
                paymentList.add(new PartPaymentTable(rs.getInt("id"), rs.getString("customer_name"), Double.valueOf(rs.getString("amount")), rs.getString("date")));
                totalPrice = totalPrice + Double.valueOf(rs.getString("amount"));
            }
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            totalPartPayment.setText("N" + formatter.format(totalPrice));

            paymentHistorySN.setCellValueFactory(new PropertyValueFactory<>("id"));
            paymentHistoryName.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
            paymentHistoryAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
            paymentHistoryDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            paymentHistoryTable.setItems(paymentList);

            difference = debtTotalPrice - totalPrice;

            debtNetTotal.setText("N" + formatter.format(difference));
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void viewDetailedHistory(ActionEvent evt) {
        ObservableList<DebtorsHistoryTable> debtorsSelected, allDebtors;
        allDebtors = debtHistoryTable.getItems();
        debtorsSelected = debtHistoryTable.getSelectionModel().getSelectedItems();
        if (debtorsSelected.isEmpty()) {
            Init.alertMsg("No selection made", "Please select Debtor from the Table first");
        } else {
            try {
                DebtorsHistoryTable debtor = debtorsSelected.get(0);
                Stage primaryStage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                Pane root = loader.load(getClass().getResource("/Resources/FXMLDebtorsView.fxml").openStream());
                DebtorsView viewDebtors = (DebtorsView) loader.getController();
                viewDebtors.getViewDebtors(debtor.getId(), debtor.getCustomer_name(), Double.valueOf(debtor.getTotal()), debtor.getDate());
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.setTitle("View Debtor Details");
                primaryStage.setMaxWidth(600.0);
                //primaryStage.setMaxHeight(590.0);
                primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
                primaryStage.show();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDebtorHistory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void refreshDebtHistory(ActionEvent evt) {
        getHistory(this.customer);
    }

    public void printPartPayment(ActionEvent evt) {
        HashMap<String, String> itemsHashMap = new HashMap<String, String>();
        HashMap<String, String> debtorItem = new HashMap<String, String>();
        debtorItem.put("customer_name", this.customer + ": Payment History");
        debtorItem.put("totalSales", String.valueOf(difference));

        PartPaymentTable payment;
        for (int i = 0; i < paymentHistoryTable.getItems().size(); i++) {
            payment = paymentHistoryTable.getItems().get(i);
            itemsHashMap.put(i + "_" + payment.getAmount(), payment.getDate());
        }
        Init.printDebtor(debtorItem, itemsHashMap);
    }

}
