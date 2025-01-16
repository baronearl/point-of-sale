/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

import java.io.Serializable;


public class SerializeLocalObjects implements Serializable {
    
    public String ipAddress, dbName, dbUsername, dbPassword;
    public String ipAddressOnline, dbNameOnline, dbUsernameOnline, dbPasswordOnline, serverNameOnline;
    
    public SerializeLocalObjects(String ipAddress, String dbName, String dbUsername, String dbPassword){
        this.ipAddress = ipAddress;
        this.dbName = dbName;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
    }
    
    public SerializeLocalObjects(String ipAddressOnline, String dbNameOnline, String dbUsernameOnline, String dbPasswordOnline, String serverNameOnline){
        this.ipAddressOnline = ipAddressOnline;
        this.dbNameOnline = dbNameOnline;
        this.dbUsernameOnline = dbUsernameOnline;
        this.dbPasswordOnline = dbPasswordOnline;
        this.serverNameOnline = serverNameOnline;
    }
    
}
