package Application;

import System.Init;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class PreviewReceipt implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private TextArea receiptPrintArea;

    String receiptStarts;
    public HashMap<String, String> customerDetail, item;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void preview(HashMap<String, String> customerDetails, HashMap<String, String> items) {
        customerDetail = customerDetails;
        item = items;
  
        // Iterate through the hashmap 
        // and add some bonus marks for every student  
        
        receiptStarts = "Name: " + customerDetails.get("customer_name") + "\n";
        receiptStarts += "Date: " + customerDetails.get("date") + "\n\n";
        receiptStarts += "Qty \t Item \t Price \n\n";

        DecimalFormat formatter = new DecimalFormat("#,###.00");
        items.forEach((key, value) -> {
            String[] qntyKey = key.split("_");
            String qty = qntyKey[1];

            String[] itemPrice = value.split("_");
            String item = itemPrice[0];
            //Split the item more to remove the sku
            String[] getRealItem = item.split("-");
            String mainItem = getRealItem[0].trim();
            String price = itemPrice[1];

            receiptStarts += qty + "\t" + mainItem + "\t" + formatter.format(Double.valueOf(price)) + "\n\n";
        });
        receiptStarts += "\n";
        receiptStarts += "Total = " + formatter.format(Double.valueOf(customerDetails.get("totalSales"))) + "\n";
        receiptPrintArea.setText(receiptStarts);
    }

    public void printReceipt(ActionEvent evt) {
        Init.print(customerDetail, item);
    }

    public void closeReceipt(ActionEvent evt) {
        root.getScene().getWindow().hide();
    }

}
