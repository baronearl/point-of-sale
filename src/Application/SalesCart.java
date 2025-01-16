/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

import javafx.beans.property.*;


public class SalesCart {
    
    private final SimpleIntegerProperty id;
    private final SimpleIntegerProperty quantity;
    private final SimpleStringProperty item;
    private final SimpleDoubleProperty unitPrice;
    private final SimpleDoubleProperty total;
    private final SimpleStringProperty credit;
    
    public SalesCart(Integer id, Integer quantity, String item, double unitPrice, double total, String credit){
        super();
        this.id = new SimpleIntegerProperty(id);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.item = new SimpleStringProperty(item);
        this.unitPrice = new SimpleDoubleProperty(unitPrice);
        this.total = new SimpleDoubleProperty(total);
        this.credit = new SimpleStringProperty(credit);
    }

    public Integer getId() {
        return id.get();
    }

    public Integer getQuantity() {
        return quantity.get();
    }

    public String getItem() {
        return item.get();
    }

    public double getUnitPrice() {
        return unitPrice.get();
    }

    public double getTotal() {
        return total.get();
    }

    public String getCredit() {       
        return credit.get();
    }
}
