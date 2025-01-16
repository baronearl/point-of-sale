package Application;

import javafx.beans.property.*;

public class StockHistoryTable {
   
    private SimpleIntegerProperty id;
    private SimpleStringProperty product_name;
    private SimpleIntegerProperty quantity;
    private SimpleStringProperty date;
    
    public StockHistoryTable(Integer id, String name, Integer quantity, String date){
        super();
        this.id = new SimpleIntegerProperty(id);
        this.product_name = new SimpleStringProperty(name);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.date = new SimpleStringProperty(date);
    }

    public Integer getId() {
        return id.get();
    }

    public String getProduct_name() {
        return product_name.get();
    }

    public Integer getQuantity() {
        return quantity.get();
    }

    public String getDate() {
        return date.get();
    }
    
    
}
