package Application;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class PartPaymentTable {
    
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty customer_name;
    private final SimpleDoubleProperty amount;
    private final SimpleStringProperty date;

    public PartPaymentTable(Integer id, String customer_name, Double amount, String date) {
        super();
        this.id = new SimpleIntegerProperty(id);
        this.customer_name = new SimpleStringProperty(customer_name);
        this.amount = new SimpleDoubleProperty(amount);
        this.date = new SimpleStringProperty(date);;
    }

    public Integer getId() {
        return id.get();
    }

    public String getCustomer_name() {
        return customer_name.get();
    }

    public Double getAmount() {
        return amount.get();
    }
    
    public String getDate() {
        return date.get();
    }
    
}
