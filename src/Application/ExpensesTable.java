/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

import javafx.beans.property.*;

public class ExpensesTable {

    private final IntegerProperty id;
    private final StringProperty comment;
    private final DoubleProperty amount;
    private final StringProperty date;
    private final StringProperty sales_person;

    public ExpensesTable(Integer id, Double amount, String comment, String sales, String date) {
        super();
        this.id = new SimpleIntegerProperty(id);
        this.comment = new SimpleStringProperty(comment);
        this.amount = new SimpleDoubleProperty(amount);
        this.sales_person = new SimpleStringProperty(sales);
        this.date = new SimpleStringProperty(date);
    }

    public Integer getId() {
        return id.get();
    }

    public String getComment() {
        return comment.get();
    }

    public Double getAmount() {
        return amount.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getSales_person() {
        return sales_person.get();
    }

    

}
