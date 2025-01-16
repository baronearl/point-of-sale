package Application;

import javafx.beans.property.*;

public class Debtors {

    private SimpleIntegerProperty id;
    private SimpleStringProperty customer_name;
    private SimpleDoubleProperty total;
    private SimpleStringProperty date;
    
    public Debtors(Integer id, String name, Double amount, String date){
        this.id = new SimpleIntegerProperty(id);
        this.customer_name = new SimpleStringProperty(name);
        this.total = new SimpleDoubleProperty(amount);
        this.date = new SimpleStringProperty(date);
    }

    public String getCustomer_name() {
        return customer_name.get();
    }

    public Integer getId() {
        return id.get();
    }

    public Double getTotal() {
        return total.get();
    }

    public String getDate() {
        return date.get();
    }
    
    
}
