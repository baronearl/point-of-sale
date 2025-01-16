package System;

import Application.BillPrintable;
import Application.DebtorPrintable;
import Application.StockPrintable;
import Application.SuperAdminController;
import com.sun.javafx.print.PrintHelper;
import com.sun.javafx.print.Units;
import java.awt.Color;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.FontMetrics;
import javafx.print.Printer;
/**import javafx.print.PrinterJob;**/
import javafx.scene.Node;
import javafx.scene.control.Alert;
import java.awt.print.PrinterJob;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.scene.transform.Scale;

public class Init {

    public static void main(String[] args) {

    }

    public static String filterStrings(String s) {
        return s.replaceAll("[^a-zA-Z0-9\\s]", "");
    }

    public static String filterPrice(String s) {
        return s.replaceAll("[^0-9]", "");
    }

    public static String updateZeros(String s) {
        return s.replaceAll("[^0-9]", "0");
    }

    public static void alertMsg(String header, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(header);
        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static String dateFormat(LocalDate date) {
        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return (date != null) ? dateFormatter.format(date) : "";
    }

    public static void print(HashMap<String, String> customerdetails, HashMap<String, String> items){
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPrintable(new BillPrintable(customerdetails, items), new Init().getPageFormat(pj));
        try {
            pj.print();

        } catch (PrinterException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void printStock(HashMap<String, String> customerdetails, String title){
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPrintable(new StockPrintable(customerdetails, title), new Init().getPageFormat(pj));
        try {
            pj.print();
        } catch (PrinterException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void printDebtor(HashMap<String, String> customerdetails, HashMap<String, String> debtorHistory){
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPrintable(new DebtorPrintable(customerdetails, debtorHistory), new Init().getPageFormat(pj));
        try {
            pj.print();
        } catch (PrinterException ex) {
            ex.printStackTrace();
        }
    }


    public static HashMap<String, String> companyDetails() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        try {
            String[] names = {"id"};
            String[] values = {"1"};
            ResultSet rs = SQLiteConnection.select("SELECT * FROM settings", names, values);
            while (rs.next()) {
                hashMap.put("company_name", rs.getString("company_name"));
                hashMap.put("address", rs.getString("address"));
                hashMap.put("phone", rs.getString("phone"));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hashMap;
    }

    public static boolean checkInternet() {
        String site = "https://www.google.com";
        Socket sock = new Socket();
        InetSocketAddress addr = new InetSocketAddress(site, 80);
        try {
            sock.connect(addr, 3000);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                sock.close();
            } catch (IOException e) {
            }
        }
    }
    
     public PageFormat getPageFormat(PrinterJob pj) {

        PageFormat pf = pj.defaultPage();
        Paper paper = pf.getPaper();

        double middleHeight = 8.0;
        double headerHeight = 2.0;
        double footerHeight = 2.0;
        double width = convert_CM_To_PPI(8);      //printer know only point per inch.default value is 72ppi
        double height = paper.getHeight(); //convert_CM_To_PPI(headerHeight + middleHeight + footerHeight);
        paper.setSize(width, height);
        paper.setImageableArea(
                0,
                10,
                width,
                height - convert_CM_To_PPI(1)
        );   //define boarder size    after that print area width is about 180 points

        pf.setOrientation(PageFormat.PORTRAIT);           //select orientation portrait or landscape but for this time portrait
        pf.setPaper(paper);

        return pf;
    }

    protected static double convert_CM_To_PPI(double cm) {
        return toPPI(cm * 0.393600787);
    }

    protected static double toPPI(double inch) {
        return inch * 72d;
    }

}
