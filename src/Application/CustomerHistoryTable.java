/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author mac
 */
public class CustomerHistoryTable {
    
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty customer_name;
    private final SimpleDoubleProperty total;
    private final SimpleStringProperty date;

    public CustomerHistoryTable(Integer id, String customer_name, Double total, String date) {
        super();
        this.id = new SimpleIntegerProperty(id);
        this.customer_name = new SimpleStringProperty(customer_name);
        this.total = new SimpleDoubleProperty(total);
        this.date = new SimpleStringProperty(date);
    }

    public Integer getId() {
        return id.get();
    }

    public String getCustomer_name() {
        return customer_name.get();
    }

    public Double getTotal() {
        return total.get();
    }
    
    public String getDate() {
        return date.get();
    }
    
}
