/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author mac
 */
public class FXMLAboutSoftware implements Initializable {

    @FXML
    private TextArea aboutText;
    @FXML
    private String text;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.text = "This is a software for meeting up your daily sales in a Retail, Pharamacy, \n"
                + "Wholesale, Factory, Industries etc. It has already been designed to suit \n"
                + "your needs at all times and can also be modified at your request. \n"
                + "This software cannot be reproduced without prior consent from the Company.\n\n\n"
                + "POS version 1.1 \n\n"
                + "Proudly powered by Baronearl ICT Firm";
    }

    void viewAboutUs() {
        aboutText.setText(this.text);
    }

}
