package System;

import Application.SerializeLocalObjects;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class MYSQLConnection {

    static Connection conn = null;
    static ResultSet rs = null;
    static PreparedStatement pst = null;
    static String namesList = "";
    static String valuesList = "";
    static String whereClause, setClause = "";

    public String[] configSettings() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File("phoenixPOSConfig/configOln.txt")));
            SerializeLocalObjects sContents = (SerializeLocalObjects) in.readObject();
            String[] values = {sContents.ipAddressOnline, sContents.dbNameOnline, sContents.dbUsernameOnline, sContents.dbPasswordOnline};
            return values;
        } catch (Exception e) {
            System.out.print(e);
        }
        return null;
    }

    public MYSQLConnection() {
        String[] config = configSettings();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + config[0] + "/" + config[1] + "?serverTimezone=UTC", config[2], config[3]);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Connection Connector() {
        String[] config = new MYSQLConnection().configSettings();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + config[0] + "/" + config[1] + "?serverTimezone=UTC", config[2], config[3]);
            return conn;
        } catch (Exception ex) {
            Logger.getLogger(SQLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static boolean isConnected() {
        Connection conn = Connector();
        try {
            if (!conn.isClosed()) {
                Init.alertMsg("Message", "Its connected");
            } else {
                Init.alertMsg("Message", "Not connected");
            }
            return !conn.isClosed();
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static ResultSet select(String table, String[] names, String[] values, boolean all) {

        for (int i = 0; i < names.length; i++) {
            if (i != (names.length - 1)) {
                whereClause += names[i] + " = '" + values[i] + "' AND ";
            } else {
                whereClause += names[i] + " = '" + values[i] + "'";
            }
        }
        conn = new SQLiteConnection().Connector();
        try {
            String sql = "SELECT * FROM " + table + " WHERE " + whereClause;
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //return rs;
        } catch (SQLException e) {
            System.out.println(e);
        }
        whereClause = "";
        return rs;
    }

    public static ResultSet select(String table, String[] names, String[] values, String selected) {

        for (int i = 0; i < names.length; i++) {
            if (i != (names.length - 1)) {
                whereClause += names[i] + " = '" + values[i] + "' AND ";
            } else {
                whereClause += names[i] + " = '" + values[i] + "'";
            }
        }
        conn = new SQLiteConnection().Connector();
        try {
            String sql = "SELECT " + selected + " FROM " + table + " WHERE " + whereClause;
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
        } catch (SQLException e) {
            System.out.println(e);
        }
        whereClause = "";
        return rs;
    }

    public static ResultSet select(String table, boolean all) {
        conn = new SQLiteConnection().Connector();
        try {
            String sql = "SELECT * FROM " + table;
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return rs;
    }

    public static ResultSet select(String table, String selected) {
        conn = new SQLiteConnection().Connector();
        try {
            String sql = "SELECT " + selected + " FROM " + table;
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return rs;
    }

    public static Integer insert(String table, String[] names, String[] values) {
        try {
            for (int i = 0; i < names.length; i++) {
                if (i != (names.length - 1)) {
                    namesList += names[i] + ",";
                    valuesList += "'" + values[i] + "',";
                } else {
                    namesList += names[i];
                    valuesList += "'" + values[i] + "'";
                }
            }
            String sql = "INSERT INTO " + table + " (" + namesList + ") VALUES (" + valuesList + ")";
            System.out.println(sql);
            pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //for (int i = 0; i < values.length; i++) {
            //  pst.setString(i + 1, values[i]);
            //  System.out.println(i+1 + " " + values[i]);
            //}
            pst.execute();
            namesList = "";
            valuesList = "";
            ResultSet rs = pst.getGeneratedKeys();
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public static boolean update(String table, String[] names, String[] values, String columnName, Integer id) {
        for (int i = 0; i < names.length; i++) {
            if (i != (names.length - 1)) {
                setClause += names[i] + " = '" + values[i] + "', ";
            } else {
                setClause += names[i] + " = '" + values[i] + "'";
            }
        }
        conn = new SQLiteConnection().Connector();
        try {
            String sql = "UPDATE " + table + " SET " + setClause + " WHERE " + columnName + "='" + id + "'";
            pst = conn.prepareStatement(sql);
            pst.execute();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                pst.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(SQLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        setClause = "";
        return true;
    }

    public static ResultSet select(String query, String[] names, String[] values) {
        String where = "";
        whereClause = "";
        if (names.length > 0) {
            for (int i = 0; i < names.length; i++) {
                if (i != (names.length - 1)) {
                    whereClause += names[i] + " = '" + values[i] + "' AND ";
                } else {
                    whereClause += names[i] + " = '" + values[i] + "'";
                }
            }
            where = "WHERE " + whereClause;
        }
        conn = new SQLiteConnection().Connector();
        try {
            String sql = query + " " + where;
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return rs;
    }

    public static void Delete(String table, String condition, Integer id) {
        String sql = "DELETE FROM " + table + " WHERE " + condition + " = '" + id + "'";
        try {
            pst = conn.prepareStatement(sql);
            pst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void restoreBackUp(){
        try {
            /*NOTE: String s is the mysql file name including the .sql in its name*/
            /*NOTE: Getting path to the Jar file being executed*/
            /*NOTE: YourImplementingClass-> replace with the class executing the code*/
            CodeSource codeSource =  MYSQLConnection.class.getProtectionDomain().getCodeSource();
            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            String jarDir = jarFile.getParentFile().getPath();

            /*NOTE: Creating Database Constraints*/
             String dbName = "YourDBName";
             String dbUser = "YourUserName";
             String dbPass = "YourUserPassword";

            /*NOTE: Creating Path Constraints for restoring*/
            String restorePath = "/home/baronearl/public_html/barupload/backup.sql";

            /*NOTE: Used to create a cmd command*/
            /*NOTE: Do not create a single large string, this will cause buffer locking, use string array*/
            String[] executeCmd = new String[]{"/Applications/AMPPS/mysql/bin/mysqldump", dbName, "-u" + dbUser, "-p" + dbPass, "-e", " source " + restorePath};

            /*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();

            /*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
            if (processComplete == 0) {
                Init.alertMsg(null, "Successfully restored from SQL : ");
            } else {
                JOptionPane.showMessageDialog(null, "Error at restoring");
            }


        } catch (URISyntaxException | IOException | InterruptedException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, "Error at Restoredbfromsql" + ex.getMessage());
        }
    }

}
