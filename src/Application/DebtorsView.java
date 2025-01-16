package Application;

import System.Init;
import System.SQLiteConnection;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class DebtorsView implements Initializable {

    @FXML
    private Label nameField, dateField, totalField;
    @FXML
    private TableView<SalesItemTable> debtorsItemTable;
    @FXML
    private TableColumn<SalesItemTable, Integer> debtorsId;
    @FXML
    private TableColumn<SalesItemTable, String> debtorsItem;
    @FXML
    private TableColumn<SalesItemTable, Integer> debtorsQuantity;
    @FXML
    private TableColumn<SalesItemTable, Double> debtorsPrice;
    @FXML
    private TableColumn<SalesItemTable, Double> debtorsTotal;
    @FXML
    private TableColumn<SalesItemTable, String> debtorsCredit;

    public ObservableList<SalesItemTable> debtorsItemList = FXCollections.observableArrayList();

    private Integer id;
    ResultSet rst;
    PreparedStatement pst;
    Integer totalQty, productId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //this.ViewDebtors();
    }

    public void getViewDebtors(Integer id, String customerName, Double total, String date) {
        this.id = id;
        nameField.setText(customerName);
        dateField.setText(date);
        getDebtorsCredit();
    }

    public void refreshDebtorsView(ActionEvent evt) {
        getDebtorsCredit();
    }

    public void getDebtorsCredit() {
        debtorsItemList.clear();
        String[] names = {};
        String[] values = {};
        ResultSet rs = SQLiteConnection.select("SELECT id, quantity, price, item, total, credit, SUM(total) as net_total FROM sales_items WHERE sales_id = '" + this.id + "' AND credit = 'Yes' GROUP BY id", names, values);
        double totalPrice = 0;
        try {
            while (rs.next()) {
                debtorsItemList.add(new SalesItemTable(rs.getInt("id"), rs.getInt("quantity"), Double.valueOf(rs.getString("price")), rs.getString("item"), Double.valueOf(rs.getString("total")), rs.getString("credit")));
                totalPrice = totalPrice + Double.valueOf(rs.getString("total"));
            }
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            totalField.setText("N" + formatter.format(totalPrice));

            debtorsId.setCellValueFactory(new PropertyValueFactory<>("id"));
            debtorsItem.setCellValueFactory(new PropertyValueFactory<>("item"));
            debtorsQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            debtorsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
            debtorsTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
            debtorsCredit.setCellValueFactory(new PropertyValueFactory<>("credit"));
            debtorsItemTable.setItems(debtorsItemList);
        } catch (SQLException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clearAllDebts(ActionEvent evt) {
        Connection conn = new SQLiteConnection().Connector();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Clear debts of this Customer?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            try {
                String sql = "SELECT id FROM sales_items WHERE credit = 'Yes' AND sales_id = '" + this.id + "'";
                System.out.println(sql);
                pst = conn.prepareStatement(sql);
                rst = pst.executeQuery();
                while (rst.next()) {
                    String updateSql = "UPDATE sales_items SET credit = 'No' WHERE id='" + rst.getInt("id") + "'";
                    pst = conn.prepareStatement(updateSql);
                    pst.execute();

                    String sqlInsert = "INSERT INTO table_to_update (type, type_id) VALUES ('sales_items', '" + rst.getInt("id") + "')";
                    pst = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                    pst.execute();
                }
                Init.alertMsg("Message", "Customer's credit details updated successfully");
            } catch (SQLException ex) {
                Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {

                try {
                    rst.close();
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }

    public void partPayment(ActionEvent evt) {
        String debtorName = nameField.getText();
        try {
            ObservableList<SalesItemTable> debtorsSelected, allDebtors;
            allDebtors = debtorsItemTable.getItems();
            debtorsSelected = debtorsItemTable.getSelectionModel().getSelectedItems();
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/FXMLPartPayment.fxml"));
            Parent root = (Parent) loader.load();
            PartPayment partPayment = (PartPayment) loader.getController();
            partPayment.setDebtorName(debtorName);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setMaxWidth(446.0);
            primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(DebtorsView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void modifyDebtor(ActionEvent evt) {
        ObservableList<SalesItemTable> debtorsSelected, allDebtors;
        allDebtors = debtorsItemTable.getItems();
        debtorsSelected = debtorsItemTable.getSelectionModel().getSelectedItems();
        if (debtorsSelected.isEmpty()) {
            Init.alertMsg("No selection made", "Please select Item from the Table first");
        } else {
            try {
                Stage primaryStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/FXMLModifyDebtor.fxml"));
                Parent root = (Parent) loader.load();
                ModifyDebtor modifyDebtor = (ModifyDebtor) loader.getController();
                SalesItemTable debtor = debtorsSelected.get(0);
                Integer debtorId = debtor.getId();
                modifyDebtor.setModifyDebtorView(debtorId);
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.setMaxWidth(446.0);
                primaryStage.setMaxHeight(320.0);
                primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
                primaryStage.show();
            } catch (IOException ex) {
                Logger.getLogger(DebtorsView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void returnedDebt(ActionEvent evt) {
        Connection conn = new SQLiteConnection().Connector();
        ObservableList<SalesItemTable> debtorsSelected, allDebtors;
        allDebtors = debtorsItemTable.getItems();
        debtorsSelected = debtorsItemTable.getSelectionModel().getSelectedItems();
        if (debtorsSelected.isEmpty()) {
            Init.alertMsg("No selection made", "Please select Item from the Table first");
        } else {
            SalesItemTable debtor = debtorsSelected.get(0);
            Integer qty = debtor.getQuantity();
            String item = debtor.getItem();
            String[] nameSku = item.split("-");
            String sku = nameSku[1].trim();
            //System.out.println(debtor.getQuantity() + " " + debtor.getItem());
            //System.out.println(sku);
            String[] name = {"name"};
            String[] value = {item};
            try {
                String sql = "SELECT id, quantity FROM products WHERE sku = '" + sku + "'";
                pst = conn.prepareStatement(sql);
                rst = pst.executeQuery();
                if (rst.next()) {
                    totalQty = rst.getInt("quantity") + qty;
                    productId = rst.getInt("id");
                }
                String sqlInsert = "INSERT INTO table_to_delete (type, type_id) VALUES ('sales_items', '" + debtor.getId() + "')";
                pst = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                pst.execute();
            } catch (SQLException ex) {
                Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    rst.close();
                    pst.close();
                    //conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DebtorsView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            String[] nameR = {"quantity"};
            String[] valueR = {totalQty.toString()};

            SQLiteConnection.Delete("sales_items", "id", debtor.getId());
            SQLiteConnection.update("products", nameR, valueR, "id", productId);
            Init.alertMsg("Success", "Debtor updated and Re-stocked");
        }
    }

}
