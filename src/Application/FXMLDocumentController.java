/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

import System.Hash;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author Hp15
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Label labelStatus;
    @FXML
    private TextField usernameField, passwordField;
    @FXML
    private Button loginBtn;
    @FXML
    private ComboBox accessLevelCombo;

    Stage window;

    public ObservableList<String> acessLevels = FXCollections.observableArrayList("sales", "admin", "superadmin");

    @FXML
    private void handleLogin(ActionEvent event) {
        try {
            String username = usernameField.getText();
            String password = Hash.create(passwordField.getText());
            String role = String.valueOf(accessLevelCombo.getValue());
            String[] names = {"username", "password", "role"};
            String[] values = {username, password, role};
            ResultSet rs = SQLiteConnection.select("SELECT user_id FROM users", names, values);
            if (rs.next()) {
                ((Node) event.getSource()).getScene().getWindow().hide();
                Stage primaryStage = new Stage();
                window = primaryStage;
                FXMLLoader loaders = new FXMLLoader();
                try {
                    if (role == "sales") {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/FXMLSalesDashboard.fxml"));
                        Parent root = (Parent) loader.load();
                        SalesDashboardController salesPersons = (SalesDashboardController) loader.getController();
                        salesPersons.salesPerson(username);
                        Scene scene = new Scene(root);
                        primaryStage.setScene(scene);
                        //primaryStage.setMaxWidth(1020.0);
                        //primaryStage.setMaxHeight(800.0);
                        primaryStage.setOnCloseRequest(e -> {
                            e.consume();
                            closeRequest();
                        });
                        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
                        primaryStage.show();
                    } else if (role == "admin") {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/FXMLAdmin.fxml"));
                        Parent root = (Parent) loader.load();
                        AdminController adminController = (AdminController) loader.getController();
                        adminController.setUsername(username);
                        Scene scene = new Scene(root);
                        primaryStage.setScene(scene);
                        //primaryStage.setMaxWidth(1020.0);
                        //primaryStage.setMaxHeight(800.0);
                        primaryStage.setOnCloseRequest(e -> {
                            e.consume();
                            closeRequest();
                        });
                        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
                        primaryStage.show();
                    } else if (role == "superadmin") {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/FXMLSuperAdmin.fxml"));
                        Parent root = (Parent) loader.load();
                        SuperAdminController superAdmin = (SuperAdminController) loader.getController();
                        superAdmin.setUsername(username);
                        Scene scene = new Scene(root);
                        primaryStage.setScene(scene);
                        //primaryStage.setMaxWidth(1020.0);
                        //primaryStage.setMaxHeight(800.0);
                        primaryStage.setOnCloseRequest(e -> {
                            e.consume();
                            closeRequest();
                        });
                        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
                        primaryStage.show();
                    }
                    rs.close();
                } catch (IOException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                Init.alertMsg("Login Failed", "Username and Password is not correct");
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        accessLevelCombo.setItems(acessLevels);
        loginBtn.setGraphic(new ImageView("/Images/login.png"));
    }

    private void closeRequest() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please save all data before closing the App. Continue?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            window.close();
        }
    }

    public void developersMenu(ActionEvent evt) {
        try {
            Stage primaryStage = new Stage();
            window = primaryStage;
            FXMLLoader loaders = new FXMLLoader();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/FXMLDevelopers.fxml"));
            Parent root = (Parent) loader.load();
            DevelopersController developerAdmin = (DevelopersController) loader.getController();
            
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            //primaryStage.setMaxWidth(1020.0);
            //primaryStage.setMaxHeight(800.0);
            primaryStage.setOnCloseRequest(e -> {
                e.consume();
                closeRequest();
            });
            primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/icon.png")));
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
