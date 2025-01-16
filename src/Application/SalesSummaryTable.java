/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

import javafx.beans.property.*;

public class SalesSummaryTable {

    private final IntegerProperty quantity;
    private final StringProperty item;
    private final DoubleProperty total;

    public SalesSummaryTable(Integer quantity, String item, Double total) {
        super();
        this.quantity = new SimpleIntegerProperty(quantity);
        this.item = new SimpleStringProperty(item);
        this.total = new SimpleDoubleProperty(total);
    }

    public Integer getQuantity() {
        return quantity.get();
    }

    public String getItem() {
        return item.get();
    }

    public Double getTotal() {
        return total.get();
    }

}
