package Application;

import javafx.beans.property.*;

public class SalesItemTable {

    private final IntegerProperty id;
    private final IntegerProperty quantity;
    private final DoubleProperty price;
    private final StringProperty item;
    private final DoubleProperty total;
    private final StringProperty credit;

    public SalesItemTable(Integer id, Integer qty, Double price, String item, Double total, String credit) {
        super();
        this.id = new SimpleIntegerProperty(id);
        this.quantity = new SimpleIntegerProperty(qty);
        this.price = new SimpleDoubleProperty(price);
        this.item = new SimpleStringProperty(item);
        this.total = new SimpleDoubleProperty(total);
        this.credit = new SimpleStringProperty(credit);
    }

    public Integer getId() {
        return id.get();
    }

    public Integer getQuantity() {
        return quantity.get();
    }

    public Double getPrice() {
        return price.get();
    }

    public String getItem() {
        return item.get();
    }

    public Double getTotal() {
        return total.get();
    }

    public String getCredit() {
        return credit.get();
    }

}
