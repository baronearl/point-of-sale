package Application;

import javafx.beans.property.*;


public class Products {
    
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;
    private final SimpleDoubleProperty price;
    private final SimpleStringProperty sku;
    
    public Products(Integer id, String name, Double price, String sku){
        super();
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.sku = new SimpleStringProperty(sku);
    }

    public Integer getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public Double getPrice() {
        return price.get();
    }

    public String getSku() {
        return sku.get();
    }
    
}

