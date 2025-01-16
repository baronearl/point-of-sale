/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

import javafx.beans.property.*;

public class SalesTable {

    private final IntegerProperty id;
    private final StringProperty customer_name;
    private final DoubleProperty total;
    private final StringProperty date;

    public SalesTable(Integer id, String cName, Double total, String date) {
        super();
        this.id = new SimpleIntegerProperty(id);
        this.customer_name = new SimpleStringProperty(cName);
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
