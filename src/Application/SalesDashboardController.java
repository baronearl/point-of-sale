package Application;

import System.Init;
import System.MYSQLConnection;
import System.SQLiteConnection;
import java.awt.print.PrinterException;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.CodeSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JTextArea;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import org.controlsfx.control.textfield.TextFields;
import org.omg.CORBA.NameValuePair;

public class SalesDashboardController implements Initializable {

    @FXML
    private TableView<SalesCart> salesTable;
    @FXML
    private TableColumn<SalesCart, Integer> id;
    @FXML
    private TableColumn<SalesCart, Integer> quantity;
    @FXML
    private TableColumn<SalesCart, String> item;
    @FXML
    private TableColumn<SalesCart, Double> unitPrice;
    @FXML
    private TableColumn<SalesCart, Double> total;
    @FXML
    private TableColumn<SalesCart, String> credit;
    @FXML
    private TextField sNoField, qtyField, priceField, totalField, customerName, itemFieldSearch, customerPhone;
    @FXML
    private Button addCartBtn, resetBtn, deleteBtn, saveProductBtn, expensesBtn, clearBtn;
    @FXML
    private Label serialNoLabel, qtyLabel, itemLabel, unitPriceLabel, totalLabel, netTotalValue, netTotalField, timeLabel, loggedInAs, companyName;
    @FXML
    private ComboBox creditField;
    @FXML
    private DatePicker salesDate;
    @FXML
    private AnchorPane root;
    @FXML
    private ListView itemListView;

    String currentUser;

    public ObservableList<SalesCart> list = FXCollections.observableArrayList();
    public ObservableList<String> productsList = FXCollections.observableArrayList();
    public ObservableList<String> creditList = FXCollections.observableArrayList("No", "Yes");
    public ObservableList<String> customersList = FXCollections.observableArrayList();
    //public HashMap <String, String> hashMap, itemsHashMap = new HashMap<String, String>();
    JTextArea textArea;
    String receipt;
    Menu dateMenu;
    ResultSet rst, rsti;
    PreparedStatement pst, psti;
    String companyFullName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getProducts();
        addCustomers();
        getCompanyName();
        creditField.setItems(creditList);
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        item.setCellValueFactory(new PropertyValueFactory<>("item"));
        unitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        total.setCellValueFactory(new PropertyValueFactory<>("total"));
        credit.setCellValueFactory(new PropertyValueFactory<>("credit"));
        salesTable.setItems(list);
        Timer timer = new Timer(true); //set it as a deamon
        timer.schedule(new MyTimer(), 0, 1000);
        sNoField.setText("1");
        salesDate.setValue(LocalDate.now());
        String[] valuesNot = {"Yes", "No"};
        TextFields.bindAutoCompletion(customerName, customersList);
        addCartBtn.setGraphic(new ImageView("/Images/cart-plus.png"));
        expensesBtn.setGraphic(new ImageView("/Images/clipboard-list.png"));
        deleteBtn.setGraphic(new ImageView("/Images/delete.png"));
        resetBtn.setGraphic(new ImageView("/Images/lock-reset.png"));
        saveProductBtn.setGraphic(new ImageView("/Images/content-save.png"));
        clearBtn.setGraphic(new ImageView("/Images/delete-empty.png"));
    }

    public void getCompanyName() {
        try {
            String[] name = {"id"};
            String[] value = {"1"};
            ResultSet rs = SQLiteConnection.select("SELECT company_name FROM settings", name, value);
            if (rs.next()) {
                this.companyFullName = rs.getString("company_name");
                companyName.setText(rs.getString("company_name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addCustomers() {
        try {
            String[] name = {};
            String[] value = {};
            ResultSet rs = SQLiteConnection.select("SELECT customer_name FROM sales GROUP BY customer_name", name, value);
            while (rs.next()) {
                customersList.add(rs.getString("customer_name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addItemtoCart(ActionEvent event) {
        if (qtyField.getText().trim().isEmpty()) {
            Init.alertMsg("Missing field", "Please enter the Quantity");
        } else if (itemListView.getSelectionModel().getSelectedItems() == null) {
            Init.alertMsg("Missing field", "Please select a Product Item");
        } else if (priceField.getText().trim().isEmpty()) {
            Init.alertMsg("Missing field", "Please enter the Unit price");
        } else {
            Integer sn = Integer.parseInt(sNoField.getText().trim());
            Integer qty = Integer.parseInt(qtyField.getText().trim());
            String items = String.valueOf(itemListView.getSelectionModel().getSelectedItem());
            Double price = Double.parseDouble(priceField.getText().trim());
            Double totalPrice = qty * price; //Double.parseDouble(totalField.getText().trim());
            String creditF;
            if (creditField.getValue() == null) {
                creditF = "No";
            } else {
                creditF = String.valueOf(creditField.getValue());
            }
            SalesCart sales = new SalesCart(sn, qty, items, price, totalPrice, creditF);
            salesTable.getItems().add(sales);
            sumTablePrice();
            clearFields();
            sNoField.setText(String.valueOf(salesTable.getItems().size() + 1));
        }
    }

    public void deleteItemFromCart(ActionEvent event) {
        ObservableList<SalesCart> itemSelected, allItems;
        allItems = salesTable.getItems();
        itemSelected = salesTable.getSelectionModel().getSelectedItems();
        itemSelected.forEach(allItems::remove);
        sumTablePrice();
    }

    public void getProducts() {
        String[] names = {};
        String[] values = {};
        ResultSet rs = SQLiteConnection.select("SELECT name, sku FROM products", names, values);
        try {
            while (rs.next()) {
                productsList.add(rs.getString("name") + " - " + rs.getString("sku"));
                //itemField.setItems(productsList);
                itemListView.setItems(productsList);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handleStringDigits() {
        if (!customerName.getText().trim().matches("[^-]*")) {
            customerName.setText(customerName.getText().trim().replaceAll("[-]*", ""));
        }
        if (!customerPhone.getText().trim().matches("\\d*")) {
            customerPhone.setText(customerPhone.getText().trim().replaceAll("[^\\d]", ""));
        }
    }

    public void calculateTotalPrice() {
        if (!priceField.getText().trim().matches("\\d*")) {
            priceField.setText(priceField.getText().trim().replaceAll("[^\\d]", ""));
        }
        if (!qtyField.getText().trim().matches("\\d*")) {
            qtyField.setText(qtyField.getText().trim().replaceAll("[^\\d]", ""));
        }
        if (!qtyField.getText().trim().isEmpty() && !priceField.getText().trim().isEmpty()) {
            Integer qty = Integer.parseInt(Init.filterPrice(qtyField.getText().trim()));
            Double price = Double.parseDouble(Init.filterPrice(priceField.getText().trim()));
            double totalPrice = qty * price;
            totalField.setText(String.valueOf(totalPrice));
        }
    }

    public void searchItems() {
        String search = Init.filterStrings(itemFieldSearch.getText().trim());
        if (search.isEmpty()) {
            productsList.clear();
            itemListView.getItems().clear();
            getProducts();
        } else {
            String[] names = {};
            String[] values = {};
            ResultSet rs = SQLiteConnection.select("SELECT name, sku FROM products WHERE name LIKE '%" + search + "%'", names, values);
            try {
                productsList.clear();
                itemListView.getItems().clear();
                while (rs.next()) {
                    productsList.add(rs.getString("name") + " - " + rs.getString("sku"));
                    itemListView.setItems(productsList);
                }
            } catch (SQLException ex) {
                Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void sumTablePrice() {
        double totalPrice = 0;
        for (SalesCart value : salesTable.getItems()) {
            totalPrice = totalPrice + value.getTotal();
        }
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        netTotalValue.setText("N" + formatter.format(totalPrice));
    }

    public void saveProduct(ActionEvent event) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        HashMap<String, String> itemsHashMap = new HashMap<String, String>();

        if (customerName.getText().trim().isEmpty()) {
            Init.alertMsg("Missing field", "Please enter the Customer Name");
        } else if (salesDate.getValue() == null) {
            Init.alertMsg("Missing field", "Please enter the sales date");
        } else if (salesTable.getItems().isEmpty()) {
            Init.alertMsg("Missing field", "Your cart is empty. Please add items to cart");
        } else {
            double totalPrice = 0;
            for (SalesCart value : salesTable.getItems()) {
                totalPrice = totalPrice + value.getTotal();
            }
            String name = customerName.getText().trim();
            String phone = customerPhone.getText().trim();
            LocalDate ld = salesDate.getValue();
            String date = Init.dateFormat(ld);
            String totalSales = String.valueOf(totalPrice);

            hashMap.put("customer_name", name);
            hashMap.put("date", date);
            hashMap.put("totalSales", totalSales);

            String[] names = {"customer_name", "phone", "date", "total", "sales_person"};
            String[] values = {name, phone, date, totalSales, this.currentUser};
            int lastId = SQLiteConnection.insert("sales", names, values);
            SalesCart cartT;
            for (int i = 0; i < salesTable.getItems().size(); i++) {
                cartT = salesTable.getItems().get(i);
                itemsHashMap.put(i + "_" + cartT.getQuantity(), cartT.getItem() + "_" + cartT.getTotal());

                String[] nameSales = {"sales_id", "quantity", "item", "price", "total", "credit"};
                String[] valueSales = {"" + lastId, "" + cartT.getQuantity(), "" + cartT.getItem(), "" + cartT.getUnitPrice(), "" + cartT.getTotal(), "" + cartT.getCredit()};
                SQLiteConnection.insert("sales_items", nameSales, valueSales);
                //Update the stock
                updateStock(cartT.getItem(), cartT.getQuantity());
            }

            if (lastId != 0) {
                try {
                    Stage primaryStage = new Stage();
                    FXMLLoader loader = new FXMLLoader();
                    Pane root = loader.load(getClass().getResource("/Resources/FXMLPreviewReceipt.fxml").openStream());
                    PreviewReceipt pReceipt = (PreviewReceipt) loader.getController();
                    pReceipt.preview(hashMap, itemsHashMap);
                    Scene scene = new Scene(root);
                    primaryStage.setScene(scene);
                    primaryStage.setTitle("Print Receipt");
                    primaryStage.setMaxWidth(600.0);
                    primaryStage.setMaxHeight(600.0);
                    primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
                    primaryStage.show();
                    clearAllCartItems();
                } catch (IOException ex) {
                    Logger.getLogger(SalesDashboardController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            sNoField.setText("1");
        }
    }

    private void updateStock(String product, Integer quantity) {
        Connection conn = new SQLiteConnection().Connector();
        String[] combined = product.split("-");
        String skuValue = combined[1].trim();
        try {
            String sql = "SELECT id, quantity FROM products WHERE sku = '" + skuValue + "'";
            pst = conn.prepareStatement(sql);
            rst = pst.executeQuery();
            if (rst.next()) {
                Integer subtract = rst.getInt("quantity") - quantity;
                String sqlUpdate = "UPDATE products SET quantity = '" + subtract + "' WHERE id ='" + rst.getInt("id") + "'";
                pst = conn.prepareStatement(sqlUpdate);
                pst.execute();
            }
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

    public class MyTimer extends TimerTask {

        @Override
        public void run() {
            Calendar calendar = new GregorianCalendar();
            String hour = String.format("%02d", calendar.get(Calendar.HOUR));
            String minute = String.format("%02d", calendar.get(Calendar.MINUTE));
            String second = String.format("%02d", calendar.get(Calendar.SECOND));

            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            String day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));

            String time = "Time: (" + hour + ":" + minute + ":" + second + ")";
            String date = " Date: (" + day + "/" + String.format("%02d", (month + 1)) + "/" + year + ")";

            Platform.runLater(() -> {
                timeLabel.setText(date + " " + time);
            });

        }
    }

    public void clearCartItems(ActionEvent evt) {
        clearAllCartItems();
        Init.alertMsg("Message", "Cart items cleared successfully");
    }

    private void clearFields() {
        sNoField.clear();
        qtyField.clear();
        priceField.clear();
        totalField.clear();
        itemFieldSearch.clear();
        itemListView.getSelectionModel().clearSelection();
        creditField.valueProperty().set(null);
        productsList.clear();
        itemListView.getItems().clear();
        getProducts();
    }

    public void clearAllCartItems() {
        customerName.clear();
        salesTable.getItems().clear();
    }

    public void resetCartFields() {
        clearFields();
    }

    public void signOutApp(ActionEvent evt) {
        try {
            root.getScene().getWindow().hide();
            Stage primaryStage = new Stage();
            Pane root = FXMLLoader.load(getClass().getResource("/Resources/FXMLDocument.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setMaxWidth(1020.0);
            primaryStage.setMaxHeight(800.0);
            primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(SalesDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addExpenditure(ActionEvent evt) {
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("/Resources/FXMLSalesExpenditure.fxml").openStream());
            FXMLSalesExpenditure salesExpenditure = (FXMLSalesExpenditure) loader.getController();
            salesExpenditure.setUser(this.currentUser);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Add Expenditure/Expenses");
            primaryStage.setMaxWidth(441.0);
            primaryStage.setMaxHeight(441.0);
            primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(SalesDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeApp(ActionEvent evt) {
        Platform.exit();
        System.exit(0);
    }

    public void salesPerson(String username) {
        this.currentUser = username;
        loggedInAs.setText("Logged in as: " + username);
    }

    public void showAbout(ActionEvent evt) {
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/FXMLAboutSoftware.fxml"));
            Parent roots = (Parent) loader.load();
            FXMLAboutSoftware about = (FXMLAboutSoftware) loader.getController();
            about.viewAboutUs();
            Scene scene = new Scene(roots);
            primaryStage.setScene(scene);
            primaryStage.setMaxWidth(600.0);
            primaryStage.setMaxHeight(420.0);
            primaryStage.setTitle("About Software");
            primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(SalesDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void syncToServer(ActionEvent evt) {
        this.syncSales();
        this.syncSalesItem();
        this.syncPartPaymentHistory();
        this.syncExpenditure();
        this.syncStockHistory();
        this.syncProducts();
        this.updateProducts();
        this.deleteProducts();
    }

    public void backUptoServer(ActionEvent evt){
               this.syncMain(); 
    }
    
    private void syncSales() {
        Connection connMYSQL = new MYSQLConnection().Connector();
        try {
            Connection conn = new SQLiteConnection().Connector();
            String sql = "SELECT * FROM sales WHERE synced = '0'";
            pst = conn.prepareStatement(sql);
            rst = pst.executeQuery();
            while (rst.next()) {
                String[] names = {"customer_name", "phone", "date", "total", "sales_person"};
                String[] values = {rst.getString("customer_name"), rst.getString("phone"), rst.getString("date"), rst.getString("total"), rst.getString("sales_person")};
                MYSQLConnection.insert("sales", names, values);

                String sqlUpdate = "UPDATE sales SET synced = '1' WHERE  id ='" + rst.getInt("id") + "'";
                pst = conn.prepareStatement(sqlUpdate);
                pst.execute();
            }

            Init.alertMsg("Message", "Sync completed");

        } catch (SQLException ex) {
            Logger.getLogger(SalesDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void syncSalesItem() {
        try {
            Connection conn = new SQLiteConnection().Connector();
            Connection connMYSQL = new MYSQLConnection().Connector();

            String sqlSalesItems = "SELECT * FROM sales_items WHERE synced = '0'";
            pst = conn.prepareStatement(sqlSalesItems);
            rst = pst.executeQuery();
            while (rst.next()) {
                System.out.println(rst.getString("item"));
                //String[] names = {"sales_id", "﻿quantity", "price", "item", "total", "credit"};
                //String[] values = {rsti.getString("sales_id"), rsti.getString("quantity"), rsti.getString("price"), rsti.getString("item"), rsti.getString("total"), rsti.getString("credit")};
                String insertMysql = "INSERT INTO sales_items (sales_id, quantity, price, item, total, credit) VALUES ('" + rst.getString("sales_id") + "', '" + rst.getString("quantity") + "', '" + rst.getString("price") + "', '" + rst.getString("item") + "', '" + rst.getString("total") + "', '" + rst.getString("credit") + "')";
                System.out.println(insertMysql);
                //MYSQLConnection.insert("sales_items", names, values);
                pst = connMYSQL.prepareStatement(insertMysql, Statement.RETURN_GENERATED_KEYS);
                pst.execute();

                String sqlUpdate = "UPDATE sales_items SET synced = '1' WHERE  id ='" + rst.getInt("id") + "'";
                pst = conn.prepareStatement(sqlUpdate);
                pst.execute();

                //if (rst.getString("credit").equals("Yes")) {
                //String[] nameChanges = {"sales_item"};
                //String[] valueChanges = {rst.getString("id")};
                //SQLiteConnection.insert("track_sales", nameChanges, valueChanges);
                //}
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void syncPartPaymentHistory() {
        try {
            Connection conn = new SQLiteConnection().Connector();
            Connection connMYSQL = new MYSQLConnection().Connector();

            String sqlSalesItems = "SELECT * FROM part_payment_history WHERE synced = '0'";
            pst = conn.prepareStatement(sqlSalesItems);
            rst = pst.executeQuery();
            while (rst.next()) {
                String insertMysql = "INSERT INTO part_payment_history (customer_name, amount, date) VALUES ('" + rst.getString("customer_name") + "', '" + rst.getString("amount") + "', '" + rst.getString("date") + "')";
                pst = connMYSQL.prepareStatement(insertMysql, Statement.RETURN_GENERATED_KEYS);
                pst.execute();

                String sqlUpdate = "UPDATE part_payment_history SET synced = '1' WHERE  id ='" + rst.getInt("id") + "'";
                pst = conn.prepareStatement(sqlUpdate);
                pst.execute();
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void syncExpenditure() {
        try {
            Connection conn = new SQLiteConnection().Connector();
            Connection connMYSQL = new MYSQLConnection().Connector();

            String sqlSalesItems = "SELECT * FROM expenditure WHERE synced = '0'";
            pst = conn.prepareStatement(sqlSalesItems);
            rst = pst.executeQuery();
            while (rst.next()) {
                String insertMysql = "INSERT INTO expenditure (amount, comment, date, sales_person) VALUES ('" + rst.getString("amount") + "', '" + rst.getString("comment") + "', '" + rst.getString("date") + "', '" + rst.getString("sales_person") + "')";
                pst = connMYSQL.prepareStatement(insertMysql, Statement.RETURN_GENERATED_KEYS);
                pst.execute();

                String sqlUpdate = "UPDATE expenditure SET synced = '1' WHERE  id ='" + rst.getInt("id") + "'";
                pst = conn.prepareStatement(sqlUpdate);
                pst.execute();
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void syncStockHistory() {
        try {
            Connection conn = new SQLiteConnection().Connector();
            Connection connMYSQL = new MYSQLConnection().Connector();

            String sqlSalesItems = "SELECT * FROM stock_history WHERE synced = '0'";
            pst = conn.prepareStatement(sqlSalesItems);
            rst = pst.executeQuery();
            while (rst.next()) {
                String insertMysql = "INSERT INTO stock_history (product_name, quantity, date) VALUES ('" + rst.getString("product_name") + "', '" + rst.getString("quantity") + "', '" + rst.getString("date") + "')";
                pst = connMYSQL.prepareStatement(insertMysql, Statement.RETURN_GENERATED_KEYS);
                pst.execute();

                String sqlUpdate = "UPDATE stock_history SET synced = '1' WHERE  id ='" + rst.getInt("id") + "'";
                pst = conn.prepareStatement(sqlUpdate);
                pst.execute();
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void syncProducts() {
        try {
            Connection conn = new SQLiteConnection().Connector();
            Connection connMYSQL = new MYSQLConnection().Connector();

            String sqlSalesItems = "SELECT * FROM products WHERE synced = '0'";
            pst = conn.prepareStatement(sqlSalesItems);
            rst = pst.executeQuery();
            while (rst.next()) {
                String insertMysql = "INSERT INTO products (name, sku, price, quantity) VALUES ('" + rst.getString("name") + "', '" + rst.getString("sku") + "', '" + rst.getString("price") + "', '" + rst.getString("quantity") + "')";
                pst = connMYSQL.prepareStatement(insertMysql, Statement.RETURN_GENERATED_KEYS);
                pst.execute();

                String sqlUpdate = "UPDATE products SET synced = '1' WHERE  id ='" + rst.getInt("id") + "'";
                pst = conn.prepareStatement(sqlUpdate);
                pst.execute();
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateProducts() {
        try {
            Connection conn = new SQLiteConnection().Connector();
            Connection connMYSQL = new MYSQLConnection().Connector();

            String sqlSalesItems = "SELECT * FROM table_to_update";
            pst = conn.prepareStatement(sqlSalesItems);
            rst = pst.executeQuery();
            while (rst.next()) {
                //if (rst.getString("type").equals("sales_items")) {
                String sqlUpdate = "UPDATE " + rst.getString("type") + " SET credit = 'No' WHERE id ='" + rst.getInt("type_id") + "'";
                pst = connMYSQL.prepareStatement(sqlUpdate);
                pst.execute();
                //}
                SQLiteConnection.Delete("table_to_update", "id", rst.getInt("id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void deleteProducts() {
        try {
            Connection conn = new SQLiteConnection().Connector();
            Connection connMYSQL = new MYSQLConnection().Connector();

            String sqlSalesItems = "SELECT * FROM table_to_delete";
            pst = conn.prepareStatement(sqlSalesItems);
            rst = pst.executeQuery();
            while (rst.next()) {
                //Delete from sales_items table
                MYSQLConnection.Delete("sales_items", "id", rst.getInt("type_id"));
                //Delete from local table
                SQLiteConnection.Delete("table_to_delete", "id", rst.getInt("id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void syncMain() {
        String[] config = new SQLiteConnection().configSettings();
        if (Init.checkInternet()) {
            try {

                /*NOTE: Getting path to the Jar file being executed*/
 /*NOTE: YourImplementingClass-> replace with the class executing the code*/
                CodeSource codeSource = this.getClass().getProtectionDomain().getCodeSource();
                File jarFile = new File("phoenixPOSConfig/backup.sql");
                String jarDir = jarFile.getParentFile().getPath();


                /*NOTE: Creating Database Constraints*/
                String dbName = config[1];
                String dbUser = config[2];
                String dbPass = config[3];


                /*NOTE: Creating Path Constraints for backup saving*/
 /*NOTE: Here the backup is saved in a folder called backup with the name backup.sql*/
                String savePath = new File("phoenixPOSConfig/backup.sql").toString();

                /*NOTE: Used to create a cmd command*/
                String executeCmd = "C:\\Program Files (x86)\\Ampps\\AMPPS\\mysql\\bin\\mysqldump -u" + dbUser + " -p" + dbPass + " --database " + dbName + " -r " + savePath;

                /*NOTE: Executing the command here*/
                Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
                int processComplete = runtimeProcess.waitFor();

                /*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
                if (processComplete == 0) {
                    if (uploadBackUp()) {
                        Init.alertMsg("Message", "Backup Complete");
                    }
                } else {
                    Init.alertMsg("Message", "Backup Failure");
                }

            } catch (IOException | InterruptedException ex) {
                Init.alertMsg("Error at Backuprestore", ex.getMessage());
            }
        } else {
            Init.alertMsg("Message", "Please check your internet connection");
        }
    }

    private boolean uploadBackUp() {
        File originalFile = new File("phoenixPOSConfig/backup.sql");
        String encodedBase64 = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
            byte[] bytes = new byte[(int) originalFile.length()];
            fileInputStreamReader.read(bytes);
            encodedBase64 = new String(Base64.getEncoder().encode(bytes));
            System.out.println(encodedBase64);
            //processSending(encodedBase64, url);

            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost("http://baronearl.com/barupload/file.php");

// Request parameters and other properties.
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(2);
            params.add(new BasicNameValuePair("name", this.companyFullName.trim().replaceAll("[^A-Za-z0-9]+", "_").toLowerCase()));
            params.add(new BasicNameValuePair("file", encodedBase64));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

//Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                try (InputStream instream = entity.getContent()) {
                    // do something useful
                }
            }
            return true;

        } catch (FileNotFoundException e) {
            Init.alertMsg("Error", e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Init.alertMsg("Error", e.getMessage());
            e.printStackTrace();
        }
        return false;

    }

}
