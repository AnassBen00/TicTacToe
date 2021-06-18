package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.*;

public class Config {

    @FXML
    TextField facileH, facileR, facilenbLayers;

    @FXML
    TextField difficileH, difficleR, difficilenbLayers;

    @FXML
    Button buttonValider;

    public void setFacileH(int facileH) {
        this.facileH.setText(String.valueOf(facileH));
    }

    public void setFacileR(int facileR) {
        this.facileR.setText(String.valueOf(facileR));
    }

    public void setFacilenbLayers(double facilenbLayers) {
        this.facilenbLayers.setText(String.valueOf(facilenbLayers));
    }

    public void setDifficileH(int difficileH) {
        this.difficileH.setText(String.valueOf(difficileH)) ;
    }

    public void setDifficleR(int difficleR) {
        this.difficleR.setText(String.valueOf(difficleR));
    }

    public void setDifficilenbLayers(double difficilenbLayers) {
        this.difficilenbLayers.setText(String.valueOf(difficilenbLayers));
    }

    public void validerConfig(ActionEvent actionEvent) throws IOException {
        FileWriter fin = new FileWriter("./resources/config");

        BufferedWriter b = new BufferedWriter(fin);

        b.write(Integer.parseInt(facileH.getText())+";"+new Double(facilenbLayers.getText())+";"+Integer.parseInt(facileR.getText()));
        b.newLine();
        b.write(Integer.parseInt(difficileH.getText())+";"+new Double(difficilenbLayers.getText())+";"+Integer.parseInt(difficleR.getText()));
        b.close();

    }
}
