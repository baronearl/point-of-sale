package Application;

import System.Hash;
import System.Init;
import System.SQLiteConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author mac some sql queries need to be modified. All queries here are based
 * on SQLITE and not on MYSQL
 *
 */
public class AdminController implements Initializable {

    @FXML
    private DatePicker salesDatePicker, stockDate, debtorDate, searchDate;
    @FXML
    private Label salesTotalAmount, totalSalesLabel, totalExpenses, totalExpensesLabel, netTotalPrice, totalCredits;
    @FXML
    private Button summarizeSales, saveBtnCreate, resetBtnCreate, editUser, refreshUser, deleteUser, updateUser, customersBtn, salesByMonthBtn, salesByYearBtn, checkStockBtn, searchSalesBtn,
            editBtnProduct, refreshProduct, delBtnProduct, addProductsBtn, stockBtnSave, refreshDebtors, viewDebtors, searchDebtors, productUpdateBtn, loginAsSales, viewDetailsBtn, clearSearch, clearSearchDebtor;
    @FXML
    private TextField addProductNameField, addProductPriceField, addProductSKUField, firstNameCreate,
            lastNameCreate, usernameCreate, fNameEdit, lNameEdit, unameEdit, passwordEdit, productNameEdit,
            productPriceEdit, productSkuEdit, stockQuantity, debtorName, debtorAmount, searchText, itemFieldSearch;
    @FXML
    private PasswordField passwordCreate;
    @FXML
    private ComboBox accessCreate, genderCreate, roleEdit, genderEdit, stockProduct;
    @FXML
    private Tab manageUsersTab, manageProductsTab;
    @FXML
    private TableView<Users> usersTable;
    @FXML
    private TableColumn<Users, Integer> user_id;
    @FXML
    private TableColumn<Users, String> username;
    @FXML
    private TableColumn<Users, String> role;
    @FXML
    private TableView<Products> productsTable;
    @FXML
    private TableColumn<Products, Integer> p_id;
    @FXML
    private TableColumn<Products, String> p_name;
    @FXML
    private TableColumn<Products, Double> p_price;
    @FXML
    private TableColumn<Products, String> p_sku;
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
    private TableView<Debtors> debtorsTable;
    @FXML
    private TableColumn<Debtors, Integer> debtorsId;
    @FXML
    private TableColumn<Debtors, String> debtorsName;
    @FXML
    private TableColumn<Debtors, Double> debtorsAmount;
    @FXML
    private TableColumn<Debtors, String> debtorsDate;
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
    @FXML
    private ListView listStockProducts;
    @FXML
    private AnchorPane root;

    public ObservableList<String> accessLevels = FXCollections.observableArrayList("sales", "admin", "superadmin");
    public ObservableList<String> gender = FXCollections.observableArrayList("male", "female");
    public ObservableList<Users> usersList = FXCollections.observableArrayList();
    public ObservableList<Products> productsList = FXCollections.observableArrayList();
    public ObservableList<SalesTable> salesList = FXCollections.observableArrayList();
    public ObservableList<String> productStockList = FXCollections.observableArrayList();
    public ObservableList<Debtors> debtorsList = FXCollections.observableArrayList();
    public ObservableList<ExpensesTable> expensesList = FXCollections.observableArrayList();
    public ObservableList<SalesTable> creditsList = FXCollections.observableArrayList();

    private Integer userId, pId, sId;
    public String remainingQtyId, summaryDate, currentUser;
    ResultSet rst;
    PreparedStatement pst;
    Boolean autodetect = false;
    Double totalDailyAmount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accessCreate.setItems(accessLevels);
        roleEdit.setItems(accessLevels);
        genderCreate.setItems(gender);
        genderEdit.setItems(gender);
        viewAllUsers();
        viewAllProducts();
        viewAllSales();
        viewAllExpenses();
        summarizeSales.setVisible(false);
        this.setIcons();
    }

    public void setIcons() {
        saveBtnCreate.setGraphic(new ImageView("/Images/account-plus.png"));
        resetBtnCreate.setGraphic(new ImageView("/Images/lock-reset.png"));
        editUser.setGraphic(new ImageView("/Images/account-edit.png"));
        refreshUser.setGraphic(new ImageView("/Images/refresh.png"));
        deleteUser.setGraphic(new ImageView("/Images/delete.png"));
        editBtnProduct.setGraphic(new ImageView("/Images/account-edit.png"));
        refreshProduct.setGraphic(new ImageView("/Images/refresh.png"));
        delBtnProduct.setGraphic(new ImageView("/Images/delete.png"));
        addProductsBtn.setGraphic(new ImageView("/Images/content-save.png"));
        refreshDebtors.setGraphic(new ImageView("/Images/refresh.png"));
        viewDebtors.setGraphic(new ImageView("/Images/eye.png"));
        searchDebtors.setGraphic(new ImageView("/Images/magnify.png"));
        productUpdateBtn.setGraphic(new ImageView("/Images/update.png"));
        updateUser.setGraphic(new ImageView("/Images/update.png"));
        customersBtn.setGraphic(new ImageView("/Images/account-group.png"));
        loginAsSales.setGraphic(new ImageView("/Images/login.png"));
        salesByMonthBtn.setGraphic(new ImageView("/Images/point-of-sale.png"));
        salesByYearBtn.setGraphic(new ImageView("/Images/point-of-sale.png"));
        viewDetailsBtn.setGraphic(new ImageView("/Images/eye.png"));
        summarizeSales.setGraphic(new ImageView("/Images/clipboard-list.png"));
        searchSalesBtn.setGraphic(new ImageView("/Images/magnify.png"));
        clearSearch.setGraphic(new ImageView("/Images/close-circle.png"));
        clearSearchDebtor.setGraphic(new ImageView("/Images/close-circle.png"));
    }

    public void addProducts(ActionEvent event) {
        if (addProductNameField.getText().trim().isEmpty()) {
            Init.alertMsg("Missing field", "Please enter the Product Name");
        } else if (addProductPriceField.getText().trim().isEmpty()) {
            Init.alertMsg("Missing field", "Please enter the Product Price");
        } else if (addProductSKUField.getText().trim().isEmpty()) {
            Init.alertMsg("Missing field", "Please enter the Product SKU");
        } else {
            //Check if a product with sku already exists in the database
            String name = addProductNameField.getText().trim();
            Double price = Double.parseDouble(addProductPriceField.getText().trim());
            String sku = addProductSKUField.getText().trim();
            String[] searchName = {"sku"};
            String[] searchValue = {sku};
            ResultSet rs = SQLiteConnection.select("products", searchName, searchValue, "id");
            try {
                if (rs.next()) {
                    Init.alertMsg("SKU Duplicate", "Sorry, a product already exists with this SKU '" + sku + "'");
                } else {
                    String[] names = {"name", "price", "sku"};
                    String[] values = {name, String.valueOf(price), sku};
                    SQLiteConnection.insert("products", names, values);
                    Init.alertMsg("Success", name + " Product added successfully");
                    addProductNameField.clear();
                    addProductPriceField.clear();
                    addProductSKUField.clear();
                }
            } catch (SQLException ex) {
                Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void createUser(ActionEvent evt) {
        if (firstNameCreate.getText().trim().isEmpty()) {
            Init.alertMsg("Missing field", "Please enter the first name");
        } else if (lastNameCreate.getText().trim().isEmpty()) {
            Init.alertMsg("Missing field", "Please enter the last nrice");
        } else if (usernameCreate.getText().trim().isEmpty()) {
            Init.alertMsg("Missing field", "Please enter the username");
        } else if (passwordCreate.getText().trim().isEmpty()) {
            Init.alertMsg("Missing field", "Please enter the password");
        } else if (accessCreate.getValue() == null) {
            Init.alertMsg("Missing field", "Please select the access level for this user");
        } else if (genderCreate.getValue() == null) {
            Init.alertMsg("Missing field", "Please select the gender");
        } else {
            String fname = Init.filterStrings(firstNameCreate.getText().trim());
            String lname = Init.filterStrings(lastNameCreate.getText().trim());
            String uname = Init.filterStrings(usernameCreate.getText().trim());
            String pass = Hash.create(passwordCreate.getText());
            String role = String.valueOf(accessCreate.getValue());
            String genderuser = String.valueOf(genderCreate.getValue());
            String[] searchUname = {"username"};
            String[] searchValue = {uname};
            ResultSet rs = SQLiteConnection.select("users", searchUname, searchValue, "user_id");
            try {
                if (rs.next()) {
                    Init.alertMsg("Username Duplicate", "Sorry, a user already exists with this username '" + uname + "'");
                } else {
                    String[] names = {"fname", "lname", "username", "password", "role", "gender"};
                    String[] values = {fname, lname, uname, pass, role, genderuser};
                    SQLiteConnection.insert("users", names, values);
                    Init.alertMsg("Success", "User '" + fname + " " + lname + "' saved successfully");
                    clearUsersField();
                }
            } catch (SQLException ex) {
                Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void resetUsersField(ActionEvent evt) {
        clearUsersField();
    }

    public void clearUsersField() {
        firstNameCreate.clear();
        lastNameCreate.clear();
        usernameCreate.clear();
        passwordCreate.clear();
        accessCreate.valueProperty().set(null);
        genderCreate.valueProperty().set(null);
    }

    public void viewAllUsers() {
        manageUsersTab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
                if (manageUsersTab.isSelected()) {
                    getUsers();
                }
            }
        });
    }

    public void refreshUsers(ActionEvent evt) {
        getUsers();
    }

    public void getUsers() {
        usersList.clear();
        String[] names = {};
        String[] values = {};
        ResultSet rs = SQLiteConnection.select("SELECT * FROM users WHERE username != 'root'", names, values);
        try {
            while (rs.next()) {
                usersList.add(new Users(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
            user_id.setCellValueFactory(new PropertyValueFactory<>("user_id"));
            username.setCellValueFactory(new PropertyValueFactory<>("username"));
            role.setCellValueFactory(new PropertyValueFactory<>("role"));
            usersTable.setItems(usersList);
        } catch (SQLException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editUserAction(ActionEvent evt) {
        ObservableList<Users> userSelected, allUsers;
        allUsers = usersTable.getItems();
        userSelected = usersTable.getSelectionModel().getSelectedItems();
        if (userSelected.isEmpty()) {
            Init.alertMsg("No selection made", "Please select User from the Table first");
        } else {
            Users user = userSelected.get(0);
            this.userId = user.getUser_id();
            get_UserWithID(user.getUser_id());
        }
    }

    public void get_UserWithID(Integer user_id) {
        try {
            String[] names = {"user_id"};
            String[] values = {String.valueOf(user_id)};
            ResultSet rs = SQLiteConnection.select("SELECT * FROM users", names, values);
            while (rs.next()) {
                fNameEdit.setText(rs.getString(2));
                lNameEdit.setText(rs.getString(3));
                unameEdit.setText(rs.getString(4));
                roleEdit.getSelectionModel().select(rs.getString(6));
                genderEdit.getSelectionModel().select(rs.getString(7));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteUserAction(ActionEvent evt) {
        ObservableList<Users> userSelected, allUsers;
        allUsers = usersTable.getItems();
        userSelected = usersTable.getSelectionModel().getSelectedItems();
        if (userSelected.isEmpty()) {
            Init.alertMsg("No selection made", "Please select User from the Table first");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete this user?", ButtonType.YES, ButtonType.CANCEL);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                Users user = userSelected.get(0);
                SQLiteConnection.Delete("users", "user_id", user.getUser_id());
                getUsers();
            }
        }
    }

    public void updateUserAction(ActionEvent evt) {
        try {
            String userName = unameEdit.getText();
            String[] nameSearch = {};
            String[] valueSearch = {};
            ResultSet rs = SQLiteConnection.select("SELECT user_id FROM users WHERE username = '" + userName + "' AND user_id !='" + this.userId + "'", nameSearch, valueSearch);
            if (rs.next()) {
                Init.alertMsg("Error", "Username already exists");
            } else {
                if (fNameEdit.getText().trim().isEmpty()) {
                    Init.alertMsg("Missing field", "Please enter the first name");
                } else if (lNameEdit.getText().trim().isEmpty()) {
                    Init.alertMsg("Missing field", "Please enter the last name");
                } else if (unameEdit.getText().trim().isEmpty()) {
                    Init.alertMsg("Missing field", "Please enter the username");
                } else if (roleEdit.getValue() == null) {
                    Init.alertMsg("Missing field", "Please select the access level for this user");
                } else if (genderEdit.getValue() == null) {
                    Init.alertMsg("Missing field", "Please select the gender");
                } else {
                    if (!passwordEdit.getText().trim().isEmpty()) {
                        String fname = Init.filterStrings(fNameEdit.getText().trim());
                        String lname = Init.filterStrings(lNameEdit.getText().trim());
                        String uname = Init.filterStrings(unameEdit.getText().trim());
                        String pass = Hash.create(passwordEdit.getText());
                        String role = String.valueOf(roleEdit.getValue());
                        String genderuser = String.valueOf(genderEdit.getValue());

                        String[] names = {"fname", "lname", "username", "password", "role", "gender"};
                        String[] values = {fname, lname, uname, pass, role, genderuser};
                        SQLiteConnection.update("users", names, values, "user_id", this.userId);
                        Init.alertMsg("Success", "User '" + fname + " " + lname + "' updated successfully");
                    } else {
                        String fname = Init.filterStrings(fNameEdit.getText().trim());
                        String lname = Init.filterStrings(lNameEdit.getText().trim());
                        String uname = Init.filterStrings(unameEdit.getText().trim());
                        String pass = Hash.create(passwordEdit.getText());
                        String role = String.valueOf(roleEdit.getValue());
                        String genderuser = String.valueOf(genderEdit.getValue());

                        String[] names = {"fname", "lname", "username", "role", "gender"};
                        String[] values = {fname, lname, uname, role, genderuser};
                        SQLiteConnection.update("users", names, values, "user_id", this.userId);
                        Init.alertMsg("Success", "User '" + fname + " " + lname + "' updated successfully");
                    }

                    fNameEdit.clear();
                    lNameEdit.clear();
                    unameEdit.clear();
                    passwordEdit.clear();
                    this.userId = null;
                    this.getUsers();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void viewAllProducts() {
        manageProductsTab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
                if (manageProductsTab.isSelected()) {
                    getProducts();
                }
            }
        });
    }

    public void refreshProducts(ActionEvent evt) {
        getProducts();
    }

    private void getProducts() {
        productsList.clear();
        String[] names = {};
        String[] values = {};
        ResultSet rs = SQLiteConnection.select("SELECT * FROM products", names, values);
        try {
            while (rs.next()) {
                productsList.add(new Products(rs.getInt("id"), rs.getString("name"), Double.valueOf(rs.getString("price")), rs.getString("sku")));
            }
            p_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            p_name.setCellValueFactory(new PropertyValueFactory<>("name"));
            p_sku.setCellValueFactory(new PropertyValueFactory<>("sku"));
            p_price.setCellValueFactory(new PropertyValueFactory<>("price"));
            productsTable.setItems(productsList);
        } catch (SQLException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editProductAction(ActionEvent evt) {
        ObservableList<Products> productSelected, allProducts;
        allProducts = productsTable.getItems();
        productSelected = productsTable.getSelectionModel().getSelectedItems();
        if (productSelected.isEmpty()) {
            Init.alertMsg("No selection made", "Please select Product from the Table first");
        } else {
            Products product = productSelected.get(0);
            this.pId = product.getId();
            get_ProductWithID(product.getId());
        }
    }

    public void deleteProduct(ActionEvent evt) {
        ObservableList<Products> productSelected, allProducts;
        allProducts = productsTable.getItems();
        productSelected = productsTable.getSelectionModel().getSelectedItems();
        if (productSelected.isEmpty()) {
            Init.alertMsg("No selection made", "Please select Product from the Table first");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete this product?", ButtonType.YES, ButtonType.CANCEL);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                Products product = productSelected.get(0);
                SQLiteConnection.Delete("products", "id", product.getId());
                getProducts();
            }
        }
    }

    private void get_ProductWithID(Integer id) {
        try {
            String[] names = {"id"};
            String[] values = {String.valueOf(id)};
            ResultSet rs = SQLiteConnection.select("SELECT * FROM products", names, values);
            while (rs.next()) {
                productNameEdit.setText(rs.getString("name"));
                productPriceEdit.setText(rs.getString("price"));
                productSkuEdit.setText(rs.getString("sku"));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateProductAction(ActionEvent evt) {
        Connection conn = new SQLiteConnection().Connector();
        try {
            String productSku = productSkuEdit.getText();
            //ResultSet rs = SQLiteConnection.select("SELECT id FROM products WHERE sku = '" + productSku + "' AND id !='" + this.pId + "'", skuNameSearch, skuValueSearch);
            String sql = "SELECT id FROM products WHERE sku = '" + productSku + "' AND id !='" + this.pId + "'";
            pst = conn.prepareStatement(sql);
            rst = pst.executeQuery();
            if (rst.next()) {
                Init.alertMsg("Error", "SKU already exists");
            } else {
                if (productNameEdit.getText().trim().isEmpty()) {
                    Init.alertMsg("Missing field", "Please enter the product name");
                } else if (productPriceEdit.getText().trim().isEmpty()) {
                    Init.alertMsg("Missing field", "Please enter the product price");
                } else if (productSkuEdit.getText().trim().isEmpty()) {
                    Init.alertMsg("Missing field", "Please enter the product sku");
                } else {
                    String pname = Init.filterStrings(productNameEdit.getText().trim());
                    String pprice = Init.filterStrings(productPriceEdit.getText().trim());
                    String psku = Init.filterStrings(productSkuEdit.getText().trim());

                    String[] names = {"name", "sku", "price"};
                    String[] values = {pname, psku, pprice};
                    //SQLiteConnection.update("products", names, values, "id", this.pId);
                    String updateSql = "UPDATE products SET name='" + pname + "', sku='" + psku + "', price='" + pprice + "' WHERE id='" + this.pId + "'";
                    pst = conn.prepareStatement(updateSql);
                    pst.execute();
                    Init.alertMsg("Success", "Product '" + pname + " (" + psku + ")' updated successfully");
                    productNameEdit.clear();
                    productPriceEdit.clear();
                    productSkuEdit.clear();
                    this.pId = null;
                    getProducts();
                }
            }
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
    }

    private void viewAllSales() {
        productsList.clear();
        String[] names = {};
        String[] values = {};
        ResultSet rs = SQLiteConnection.select("SELECT s.id, s.customer_name, s.date, si.sales_id, SUM(si.total) as total FROM sales as s LEFT JOIN sales_items as si ON s.id = si.sales_id GROUP BY si.sales_id ORDER by s.id DESC LIMIT 100", names, values);
        try {
            while (rs.next()) {
                System.out.println(rs.getString("customer_name"));
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

    private void viewAllExpenses() {
        expensesList.clear();
        String[] names = {};
        String[] values = {};
        ResultSet rs = SQLiteConnection.select("SELECT * FROM expenditure ORDER by id DESC LIMIT 20", names, values);
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

    public void salesViewDetails(ActionEvent evt) {
        ObservableList<SalesTable> salesSelected, allSales;
        allSales = salesTable.getItems();
        salesSelected = salesTable.getSelectionModel().getSelectedItems();
        if (salesSelected.isEmpty()) {
            Init.alertMsg("No selection made", "Please select an Item from the Table first");
        } else {
            SalesTable sales = salesSelected.get(0);
            this.sId = sales.getId();
            try {
                Stage primaryStage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                Pane root = loader.load(getClass().getResource("/Resources/FXMLViewSalesDetails.fxml").openStream());
                ViewSalesItem viewSales = (ViewSalesItem) loader.getController();
                viewSales.ViewSales(sales.getId(), sales.getCustomer_name(), Double.valueOf(sales.getTotal()), sales.getDate());
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.setTitle("View Sales Details");
                //primaryStage.setMaxWidth(600.0);
                //primaryStage.setMaxHeight(600.0);
                primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
                primaryStage.show();
            } catch (IOException ex) {
                Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void searchSales(ActionEvent evt) {
        if (salesDatePicker.getValue() == null) {
            Init.alertMsg("Missing field", "Please select date first");
        } else {
            salesList.clear();
            LocalDate ld = salesDatePicker.getValue();
            String date = Init.dateFormat(ld);
            this.summaryDate = date;
            String[] names = {};
            String[] values = {};
            double totalPrice = 0;
            ResultSet rs = SQLiteConnection.select("SELECT s.id, s.customer_name, s.date, si.sales_id, SUM(si.total) as total FROM sales as s LEFT JOIN sales_items as si ON s.id = si.sales_id WHERE date = '" + date + "' GROUP BY si.sales_id", names, values);
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
                summarizeSales.setVisible(true);
                this.totalDailyAmount = totalPrice;
                //Call expenses
                getExpenses(date, totalPrice);
            } catch (SQLException ex) {
                Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void getExpenses(String date, Double totalSales) {
        expensesList.clear();
        String[] names = {"date"};
        String[] values = {date};
        double totalPrice = 0;
        ResultSet rs = SQLiteConnection.select("SELECT * FROM expenditure", names, values);
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
            totalExpensesLabel.setText("Total Expenses   => ");
            totalExpenses.setText("N" + formatter.format(totalPrice));

            Double netTotal = totalSales - totalPrice;
            getCredits(date, netTotal);
        } catch (SQLException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getCredits(String date, Double totalSales) {
        creditsList.clear();
        this.summaryDate = date;
        String[] names = {};
        String[] values = {};
        double totalPrice = 0;
        ResultSet rs = SQLiteConnection.select("SELECT s.id, s.customer_name, s.date, si.sales_id, SUM(si.total) as total FROM sales as s LEFT JOIN sales_items as si ON s.id = si.sales_id WHERE si.credit = 'Yes' AND date = '" + date + "' GROUP BY si.sales_id", names, values);
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

            Double netTotal = totalSales - totalPrice;
            netTotalPrice.setText("N" + formatter.format(netTotal));
        } catch (SQLException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clearSearch(ActionEvent evt) {
        salesList.clear();
        salesDatePicker.setValue(null);
        viewAllSales();
        viewAllExpenses();
        summarizeSales.setVisible(false);
        salesTotalAmount.setText("");
        totalSalesLabel.setText("");
        totalExpensesLabel.setText("");
        totalExpenses.setText("");
        netTotalPrice.setText("");
    }
    
    public void clearDebtorFields(ActionEvent evt){
        searchText.clear();
        searchDate.setValue(null); 
        getDebtors();
    }


    public void searchItems() {
        String search = Init.filterStrings(itemFieldSearch.getText().trim());
        if (search.isEmpty()) {
            productStockList.clear();
            listStockProducts.getItems().clear();
            getAllProducts();
        } else {
            String[] names = {};
            String[] values = {};
            ResultSet rs = SQLiteConnection.select("SELECT name, sku FROM products WHERE name LIKE '%" + search + "%'", names, values);
            try {
                productStockList.clear();
                listStockProducts.getItems().clear();
                while (rs.next()) {
                    productStockList.add(rs.getString("name") + " - " + rs.getString("sku"));
                    listStockProducts.setItems(productStockList);
                }
            } catch (SQLException ex) {
                Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void getAllProducts() {
        productStockList.clear();
        String[] names = {};
        String[] values = {};
        ResultSet rs = SQLiteConnection.select("SELECT name, sku FROM products", names, values);
        try {
            while (rs.next()) {
                productStockList.add(rs.getString("name") + " - " + rs.getString("sku"));
                listStockProducts.setItems(productStockList);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveStock(ActionEvent evt) {
        if (listStockProducts.getSelectionModel().getSelectedItems() == null) {
            Init.alertMsg("Missing field", "Please select the Product Name");
        } else if (stockQuantity.getText().trim().isEmpty()) {
            Init.alertMsg("Missing field", "Please enter the Quantity");
        } else if (stockDate.getValue() == null) {
            Init.alertMsg("Missing field", "Please enter the Date");
        } else {
            Integer quantity = Integer.parseInt(Init.updateZeros(stockQuantity.getText()));
            String product = String.valueOf(listStockProducts.getSelectionModel().getSelectedItem());
            LocalDate ld = stockDate.getValue();
            String date = Init.dateFormat(ld);

            String[] names = {"product_name", "quantity", "date"};
            String[] values = {product, quantity.toString(), date};
            //insert into stock history
            SQLiteConnection.insert("stock_history", names, values);
            String qtyId = getRemainingQtyAndId(product);

            String[] splitQtyId = qtyId.split("_");
            int pid = Integer.parseInt(splitQtyId[0]);
            int qty = Integer.parseInt(splitQtyId[1]);
            int total = quantity + qty;
            String[] nameUpdate = {"quantity"};
            String[] valueUpdate = {String.valueOf(total)};
            //update quantity stock
            SQLiteConnection.update("products", nameUpdate, valueUpdate, "id", pid);
            stockQuantity.clear();
            itemFieldSearch.clear();
            listStockProducts.getItems().clear();
            getAllProducts();
            Init.alertMsg("Success", "Stock Saved");
        }
    }

    public String getRemainingQtyAndId(String s) {
        Connection conn = new SQLiteConnection().Connector();
        String[] combined = s.split("-");
        String skuValue = combined[1].trim();

        try {
            String sql = "SELECT id, quantity FROM products WHERE sku = '" + skuValue + "'";
            pst = conn.prepareStatement(sql);
            rst = pst.executeQuery();
            if (rst.next()) {
                return rst.getString("id") + "_" + rst.getInt("quantity");
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
        return "";
    }

    public void getDebtors() {
        debtorsList.clear();
        String[] names = {};
        String[] values = {};
        ResultSet rs = SQLiteConnection.select("SELECT i.sales_id, s.customer_name, SUM(i.total) as total, s.date FROM sales_items as i LEFT JOIN sales as s ON s.id = i.sales_id WHERE i.credit = 'Yes' GROUP BY s.customer_name", names, values);
        try {
            while (rs.next()) {
                debtorsList.add(new Debtors(rs.getInt("sales_id"), rs.getString("customer_name"), Double.valueOf(rs.getString("total")), rs.getString("date")));
            }
            debtorsId.setCellValueFactory(new PropertyValueFactory<>("id"));
            debtorsName.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
            debtorsAmount.setCellValueFactory(new PropertyValueFactory<>("total"));
            debtorsDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            debtorsTable.setItems(debtorsList);
        } catch (SQLException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void debtorsTab(Event evt) {
        getDebtors();
    }

    public void refreshDebtors(ActionEvent evt) {
        getDebtors();
    }

    public void debtorViewDetails(ActionEvent evt) {
        ObservableList<Debtors> debtorsSelected, allDebtors;
        allDebtors = debtorsTable.getItems();
        debtorsSelected = debtorsTable.getSelectionModel().getSelectedItems();
        if (debtorsSelected.isEmpty()) {
            Init.alertMsg("No selection made", "Please select Debtor from the Table first");
        } else {
            Debtors debtor = debtorsSelected.get(0);
            try {
                Stage primaryStage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                Pane root = loader.load(getClass().getResource("/Resources/FXMLDebtorHistory.fxml").openStream());
                FXMLDebtorHistory dHistory = (FXMLDebtorHistory) loader.getController();
                dHistory.getHistory(debtor.getCustomer_name());
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.setTitle("Debt History of " + debtor.getCustomer_name());
                //primaryStage.setMaxWidth(600.0);
                //primaryStage.setMaxHeight(590.0);
                primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
                primaryStage.show();
            } catch (IOException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void searchDebtors(ActionEvent evt) {
        autodetect = false;
        if (searchText.getText().trim().isEmpty() && searchDate.getValue() == null) {
            Init.alertMsg("Missing field", "Please enter the Name or Date");
        } else {
            searchText();
        }
    }

    public void handleSearchType() {
        autodetect = true;
        searchText();
    }

    public void searchText() {
        ResultSet rs;
        if (!searchText.getText().trim().isEmpty()) {
            String search = Init.filterStrings(searchText.getText().trim());
            String[] names = {};
            String[] values = {};
            rs = SQLiteConnection.select("SELECT i.sales_id, s.customer_name, SUM(i.total) as total, s.date FROM sales_items as i LEFT JOIN sales as s ON s.id = i.sales_id WHERE credit= 'Yes' AND customer_name LIKE '%" + search + "%' GROUP by s.customer_name", names, values);
        }else{
            LocalDate ld = searchDate.getValue();
            String date = Init.dateFormat(ld);
            String[] names = {};
            String[] values = {};
            rs = SQLiteConnection.select("SELECT i.sales_id, s.customer_name, SUM(i.total) as total, s.date FROM sales_items as i LEFT JOIN sales as s ON s.id = i.sales_id WHERE credit= 'Yes' AND date = '" + date + "' GROUP by s.customer_name", names, values);
        }
        try {
            debtorsList.clear();
            while (rs.next()) {
                debtorsList.add(new Debtors(rs.getInt("sales_id"), rs.getString("customer_name"), Double.valueOf(rs.getString("total")), rs.getString("date")));
            }
            debtorsId.setCellValueFactory(new PropertyValueFactory<>("id"));
            debtorsName.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
            debtorsAmount.setCellValueFactory(new PropertyValueFactory<>("total"));
            debtorsDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            debtorsTable.setItems(debtorsList);
            if (!autodetect && debtorsList.size() == 0) {
                Init.alertMsg("Not found", "Customer name not found");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void checkStock(ActionEvent evt) {
        try {
            Stage primaryStage = new Stage();
            Pane root = FXMLLoader.load(getClass().getResource("/Resources/FXMLViewStocks.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Stock Details/History");
            //primaryStage.setMaxWidth(1020.0);
            //primaryStage.setMaxHeight(800.0);
            primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void monthlySales(ActionEvent evt) {
        try {
            Stage primaryStage = new Stage();
            Pane root = FXMLLoader.load(getClass().getResource("/Resources/FXMLViewMonthlySales.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("View Monthly Sales");
            //primaryStage.setMaxWidth(513.0);
            //primaryStage.setMaxHeight(620.0);
            primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void yearlySales(ActionEvent evt) {
        try {
            Stage primaryStage = new Stage();
            Pane root = FXMLLoader.load(getClass().getResource("/Resources/FXMLViewYearlySales.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("View Yearly Sales");
            //primaryStage.setMaxWidth(700.0);
            //primaryStage.setMaxHeight(620.0);
            primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void summarySales(ActionEvent evt) {
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/FXMLSalesSummary.fxml"));
            Parent root = (Parent) loader.load();
            FXMLSalesSummary salesSummary = (FXMLSalesSummary) loader.getController();
            salesSummary.viewSummary(this.summaryDate);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            //primaryStage.setMaxWidth(529.0);
            //primaryStage.setMaxHeight(515.0);
            primaryStage.setTitle("Sales Summary for " + this.summaryDate);
            primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loginAsSales(ActionEvent evt) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to login as Sales?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            try {
                Stage primaryStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/FXMLSalesDashboard.fxml"));
                Parent root = (Parent) loader.load();
                ((Node) evt.getSource()).getScene().getWindow().hide();
                SalesDashboardController salesPersons = (SalesDashboardController) loader.getController();
                salesPersons.salesPerson(this.currentUser);
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                //primaryStage.setMaxWidth(1020.0);
                //primaryStage.setMaxHeight(800.0);
                primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
                primaryStage.show();
            } catch (IOException ex) {
                Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setUsername(String username) {
        this.currentUser = username;
    }

    public void closeApp(ActionEvent evt) {
        Platform.exit();
        System.exit(0);
    }

    public void signOutApp(ActionEvent evt) {
        try {
            root.getScene().getWindow().hide();
            Stage primaryStage = new Stage();
            Pane rootS = FXMLLoader.load(getClass().getResource("/Resources/FXMLDocument.fxml"));
            Scene scene = new Scene(rootS);
            primaryStage.setScene(scene);
            //primaryStage.setMaxWidth(1020.0);
            //primaryStage.setMaxHeight(800.0);
            primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(SalesDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            //primaryStage.setMaxWidth(600.0);
            //primaryStage.setMaxHeight(420.0);
            primaryStage.setTitle("About Software");
            primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(SalesDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handleStringDigits() {
        if (!addProductPriceField.getText().trim().matches("\\d*")) {
            addProductPriceField.setText(addProductPriceField.getText().trim().replaceAll("[^\\d]", ""));
        }
        if (!addProductSKUField.getText().trim().matches("[a-zA-Z0-9\\s]*")) {
            addProductSKUField.setText(addProductSKUField.getText().trim().replaceAll("[^a-zA-Z0-9\\s]*", ""));
        }
        //if (!addProductSKUField.getText().trim().matches("[^-]*")) {
        //  addProductSKUField.setText(addProductSKUField.getText().trim().replaceAll("[-]*", ""));
        //}
        if (!stockQuantity.getText().trim().matches("\\d*")) {
            stockQuantity.setText(stockQuantity.getText().trim().replaceAll("[^\\d]", ""));
        }
    }

    public void viewAllCustomers(ActionEvent evt) {
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/FXMLCustomersView.fxml"));
            Parent roots = (Parent) loader.load();
            FXMLCustomersView customer = (FXMLCustomersView) loader.getController();
            Scene scene = new Scene(roots);
            primaryStage.setScene(scene);
            //primaryStage.setMaxWidth(600.0);
            primaryStage.setTitle("Customers");
            primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSettings(ActionEvent evt) {
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/FXMLSettings.fxml"));
            Parent roots = (Parent) loader.load();
            Settings customer = (Settings) loader.getController();
            customer.getSettings();
            Scene scene = new Scene(roots);
            primaryStage.setScene(scene);
            //primaryStage.setMaxWidth(600.0);
            primaryStage.setTitle("Customers");
            primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
