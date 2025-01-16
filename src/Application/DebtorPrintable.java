/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

import System.Init;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DebtorPrintable implements Printable {

    public HashMap<String, String> debtorHistoryDetail, item;

    public DebtorPrintable(HashMap<String, String> debtor, HashMap<String, String> items) {
        debtorHistoryDetail = debtor;
        item = items;
    }

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
            throws PrinterException {
              
        int result = NO_SUCH_PAGE;
        if (pageIndex == 0) {

            Graphics2D g2d = (Graphics2D) graphics;

            double width = pageFormat.getImageableWidth();

            g2d.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());

            ////////// code by alqama//////////////
            FontMetrics metrics = g2d.getFontMetrics(new Font("Arial", Font.BOLD, 7));
            //    int idLength=metrics.stringWidth("000000");
            //int idLength=metrics.stringWidth("00");
            int idLength = metrics.stringWidth("000");
            int amtLength = metrics.stringWidth("000000");
            int qtyLength = metrics.stringWidth("00000");
            int priceLength = metrics.stringWidth("000000");
            int prodLength = (int) width - idLength - amtLength - qtyLength - priceLength - 17;

          
            int productPosition = 0;
            int discountPosition = prodLength + 5;
            int pricePosition = discountPosition + idLength + 10;
            int qtyPosition = pricePosition + priceLength + 4;
            int amtPosition = qtyPosition + qtyLength;

            try {
                /*Draw Header*/
                int y = 20;
                int yShift = 10;
                int headerRectHeight = 15;
                int headerRectHeighta = 40;

                HashMap companyDetails = Init.companyDetails();

                ///////////////// Product price Get ///////////
                g2d.setFont(new Font("Monospaced", Font.PLAIN, 9));
                g2d.drawString("-------------------------------------", 12, y);
                y += yShift;
                g2d.drawString(" " + companyDetails.get("company_name") + " ", 12, y);
                y += yShift;
                g2d.drawString(" " + companyDetails.get("address") + " ", 12, y);
                y += yShift;
                g2d.drawString(" " + companyDetails.get("phone") + " ", 12, y);
                y += yShift;
                g2d.drawString("-------------------------------------", 12, y);
                y += yShift;
                g2d.drawString("Name: " + debtorHistoryDetail.get("customer_name"), 10, y);
                y += yShift;
                y += headerRectHeight;

                g2d.drawString("-------------------------------------", 10, y);
                y += yShift;
                g2d.drawString(" Date    Amount                         ", 10, y);
                y += yShift;
                g2d.drawString("-------------------------------------", 10, y);

                y += headerRectHeight;
                DecimalFormat formatter = new DecimalFormat("#,###.00");

                Iterator hmIterator = item.entrySet().iterator();

                while (hmIterator.hasNext()) {
                    Map.Entry mapElement = (Map.Entry) hmIterator.next();
                    String date = mapElement.getValue().toString();
                    String key = mapElement.getKey().toString();
                    
                    String[] qntyKey = key.split("_");
                    String amount = qntyKey[1];
 
                    g2d.drawString(" " + date + "  " + amount + "     ", 10, y);
                    y += yShift;
                    
                }

                g2d.drawString("-------------------------------------", 10, y);
                y += yShift;
                g2d.drawString("Balance = " + formatter.format(Double.valueOf(debtorHistoryDetail.get("totalSales"))), 10, y);
                y += yShift;
                g2d.drawString("-------------------------------------", 10, y);
                y += yShift;
                g2d.drawString("          Signature         ", 10, y);
                y += yShift;
                g2d.drawString("-------------------------------------", 10, y);
                y += yShift;
                g2d.drawString("*************************************", 10, y);
                y += yShift;
                g2d.drawString("    THANKS FOR PATRONIZING US   ", 10, y);
                y += yShift;
                g2d.drawString("*************************************", 10, y);
                y += yShift;
//            g2d.setFont(new Font("Monospaced",Font.BOLD,10));
//            g2d.drawString("Customer Shopping Invoice", 30,y);y+=yShift; 
            } catch (Exception r) {
                r.printStackTrace();
            }

            result = PAGE_EXISTS;
        }
        return result;
    }

}
