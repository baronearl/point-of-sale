package System;

import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;

public class Constants {

    public static final String companyName = "Baronearl ICT Firm";
    public static final String companyAddress = "Kubwa, Abuja";
    public static final String companyPhone = "08064316861";
    public static final byte[] PASSWORD_HASH_KEY = new byte[]{1, 2, 3, 8, 1, 33, 45, 76, 71, 45};

    /**public void he() {

        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout
                = printer.createPageLayout(Paper.NA_LETTER, PageOrientation.PORTRAIT,
                        Printer.MarginType.DEFAULT);
        double scaleX = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
        double scaleY = pageLayout.getPrintableHeight()   / node.getBoundsInParent().getHeight();
        node.getTransforms().add(new Scale(scaleX, scaleY));

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            boolean success = job.printPage(node);
            if (success) {
                job.endJob();
            }
        }

    }**/
}
