package controller;

import ai.MultiLayerPerceptron;
import ai.SigmoidalTransferFunction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.*;

public class Settings {

    public static String fileModelFacile;
    public static String fileModelDifficile;

    @FXML
    public Button buttonValider;

    @FXML
    public TextField facileH, facileR, facilenbLayers;

    @FXML
    public TextField difficileH, difficleR, difficilenbLayers;

    @FXML
    public static TextField idColumn;

    @FXML
    public static TextField idRow;

    @FXML
    public Button idSaveButton;

    public MultiLayerPerceptron net;

    @FXML
    public Button idBackButton;

    public static int numColumn = 3;

    public static int numRow = 3;



    @FXML
    public void initialize() throws IOException {



        idBackButton.setStyle(
                "-fx-background-color: #FFFFFF; " +
                        "-fx-background-radius: 30em; " +
                        "-fx-min-width: 60px; " +
                        "-fx-min-height: 60px; " +
                        "-fx-max-width: 60px; " +
                        "-fx-max-height: 60px;"
        );

        Image backImage = new Image("file:resources/images/left-arrow.png");

        ImageView backView = new ImageView(backImage);

        backView.setFitHeight(23.0);
        backView.setFitWidth(35.0);
        backView.setX(231.0);
        backView.setY(228.0);
        backView.setPreserveRatio(true);

        idBackButton.setGraphic(backView);

        FileReader fin = new FileReader("./resources/config");

        BufferedReader bin = new BufferedReader(fin);

        String facile = bin.readLine();
        String diffcile = bin.readLine();

        String [] facileSplit = facile.split(";");
        String [] difficileSplit = diffcile.split(";");

        int hf = Integer.parseInt(facileSplit[0]);
        double lrf =  new Double(facileSplit[1]);
        int lf = Integer.parseInt(facileSplit[2]);

        int hd = Integer.parseInt(difficileSplit[0]);
        double lrd = new Double(difficileSplit[1]);
        int ld =Integer.parseInt(difficileSplit[2]);

        setDifficileH(hd);
        setDifficilenbLayers(lrd);
        setDifficleR(ld);

        setFacileH(hf);
        setFacilenbLayers(lrf);
        setFacileR(lf);

        bin.close();
    }


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

    public MultiLayerPerceptron getNet() {
        return net;
    }

    public void setNet(MultiLayerPerceptron net) {
        this.net = net;
    }


    public void validerConfig(ActionEvent actionEvent) throws IOException {
        FileWriter fin = new FileWriter("./resources/config");

        BufferedWriter b = new BufferedWriter(fin);

        b.write(Integer.parseInt(facileH.getText())+";"+new Double(facilenbLayers.getText())+";"+Integer.parseInt(facileR.getText()));
        b.newLine();
        b.write(Integer.parseInt(difficileH.getText())+";"+new Double(difficilenbLayers.getText())+";"+Integer.parseInt(difficleR.getText()));
        b.close();

    }

    public void ActionSaveDimensions(ActionEvent actionEvent) {
        numColumn = Integer.parseInt(idColumn.getText());
        System.out.println(numColumn);
        numRow = Integer.parseInt(idRow.getText());
        System.out.println(numRow);
    }

    public void configItemActionPerformed(ActionEvent actionEvent) throws IOException {



    }

    public void BackButtonAction(ActionEvent actionEvent) throws IOException {
        // Get the game scene
        Parent gameRoot = FXMLLoader.load(getClass().getResource("../application/mainWindow.fxml"));
        Scene gameScene = new Scene(gameRoot);

        // Get the stage
        Stage window = (Stage) idBackButton.getScene().getWindow();

        // Set the game scene to the stage
        window.setScene(gameScene);
        window.show();
        window.setTitle("Tic-Tac-Toe");
    }

    public void actionManageModels(ActionEvent actionEvent) throws IOException {
        // Get the game scene
        Parent gameRoot = FXMLLoader.load(getClass().getResource("../application/models.fxml"));
        Scene gameScene = new Scene(gameRoot);

        // Get the stage
        Stage window = (Stage) idBackButton.getScene().getWindow();

        // Set the game scene to the stage
        window.setScene(gameScene);
        window.show();
        window.setTitle("Tic-Tac-Toe");
    }
}