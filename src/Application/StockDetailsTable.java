/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

import javafx.beans.property.*;

public class StockDetailsTable {
    
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty quantity;
    
    public StockDetailsTable(String product, Integer qty){
        super();
        this.name = new SimpleStringProperty(product);
        this.quantity = new SimpleIntegerProperty(qty);
    }

    public String getName() {
        return name.get();
    }

    public Integer getQuantity() {
        return quantity.get();
    }
    
    
}
