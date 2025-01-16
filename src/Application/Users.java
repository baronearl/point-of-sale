/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

import javafx.beans.property.*;


public class Users {
    
    private final SimpleIntegerProperty user_id;
    private final SimpleStringProperty username;
    private final SimpleStringProperty role;
    
    public Users(Integer id, String username, String role){
        super();
        this.user_id = new SimpleIntegerProperty(id);
        this.username = new SimpleStringProperty(username);
        this.role = new SimpleStringProperty(role);
    }

     public Integer getUser_id() {
        return user_id.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getRole() {
        return role.get();
    }
    
}
