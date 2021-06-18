package controller;

import ai.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MenuController {

    @FXML
    MenuItem configItem ;

    @FXML
    Button HvHButton;

    @FXML
    Button HvAiButton;

    @FXML
    Button ContinueButton;

    @FXML
    CheckBox FacileCheckBox;

    @FXML
    CheckBox DifficileCheckBox;

    public static String fileModelFacile;
    public static String fileModelDifficile;
    public static String fileModel;
    public static MultiLayerPerceptron net;
    public static String SelectedMode;


    public static String getFileModel() {
        return fileModel;
    }

    public static void setFileModel(String fileModel) {
        MenuController.fileModel = fileModel;
    }

    public static void setSelectedMode(String selectedMode) {
        SelectedMode = selectedMode;
    }

    public String getFileModelFacile() {
        return fileModelFacile;
    }

    public void setFileModelFacile(String fileModelFacile) {
        this.fileModelFacile = fileModelFacile;
    }

    public String getFileModelDifficile() {
        return fileModelDifficile;
    }

    public void setFileModelDifficile(String fileModelDifficile) {
        this.fileModelDifficile = fileModelDifficile;
    }

    public MultiLayerPerceptron getNet() {
        return net;
    }

    public void setNet(MultiLayerPerceptron net) {
        this.net = net;
    }

    public void modelItemActionPerformed(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("../application/models.fxml"));
        Parent root = loader.load();
        Stage stage=new Stage();
        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void configItemActionPerformed(ActionEvent actionEvent) throws IOException {

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

        fileModelFacile = "mlp_"+hf+"_"+lrf+"_"+lf+".srl";

        fileModelDifficile = "mlp_"+hd+"_"+lrd+"_"+ld+".srl";


        if(new File(fileModelFacile).exists()){
            System.out.println("******* Model Loaded ********");
            MultiLayerPerceptron net = MultiLayerPerceptron.load("models/"+fileModelFacile);
        }
        else{


            int[] layers = new int[lf+2];
            layers[0] = 9;
            for (int i = 0; i < lf; i++){
                layers[i+1] = hf;
            }
            layers[layers.length-1] = 9;


            setNet(new MultiLayerPerceptron(layers, lf, new SigmoidalTransferFunction()));

        }
        bin.close();

        Stage stage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../application/Config.fxml"));

        Parent root = loader.load();
        stage.setTitle("Tic Tac Toe");
        stage.setScene(new Scene(root, 300, 275));
        stage.show();

        Config config = loader.getController();

        config.setDifficileH(hd);
        config.setDifficilenbLayers(lrd);
        config.setDifficleR(ld);

        config.setFacileH(hf);
        config.setFacilenbLayers(lrf);
        config.setFacileR(lf);
    }


    public void StartPlaying(String mode) throws IOException {

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

        if (mode.equals("Facile")){
            fileModel = "mlp_"+hf+"_"+lrf+"_"+lf+".srl";
        }
        else {
            fileModel = "mlp_"+hd+"_"+lrd+"_"+ld+".srl";
        }

        if(new File("./resources/models/"+fileModel).exists()){
            System.out.println("******* Model Loaded ********");
            MultiLayerPerceptron net = MultiLayerPerceptron.load("./resources/models/"+fileModel);
        }

        else{

            int[] layers = new int[lf+2];
            layers[0] = 9;
            for (int i = 0; i < lf; i++){
                layers[i+1] = hf;
            }
            layers[layers.length-1] = 9;

            setNet(new MultiLayerPerceptron(layers, lf, new SigmoidalTransferFunction()));

        }
        bin.close();
    }

    public void LaunchHvAiButton(ActionEvent actionEvent) {
        FacileCheckBox.setDisable(false);
        DifficileCheckBox.setDisable(false);

        if (!FacileCheckBox.isIndeterminate() || !DifficileCheckBox.isIndeterminate()){
            ContinueButton.setDisable(false);
        }
    }

    public void FacileModeAction(ActionEvent actionEvent) {

        if (FacileCheckBox.isSelected()){
            System.out.println("check boooox ************");
            setSelectedMode(FacileCheckBox.getText());
        }else{
            setSelectedMode(null);
        }


/*        FacileCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                // TODO Auto-generated method stub
                System.out.println("NEW ************"+newValue);
                System.out.println("OLd ************"+oldValue);
                if(newValue){
                    System.out.println("check boooox ************");
                    setSelectedMode(FacileCheckBox.getText());
                }else{
                    System.out.println("***facile option not Ticked***");
                }
            }
        });*/
    }

 /*   public void CheckBoxVerification(String value){
        if (value == "facile"){
            ContinueOnAction(value);
        }
    }*/

    public void ContinueOnAction(ActionEvent actionEvent) throws IOException {
        if(SelectedMode != null){
            StartPlaying(SelectedMode);
            if(!new File("./resources/models/"+fileModel).exists()){
                Parent root = FXMLLoader.load(getClass().getResource("../application/sample.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Tic Tac Toe");
                stage.setScene(new Scene(root));
                stage.show();
            }
        }

    }

    public void DifficileModeAction(ActionEvent actionEvent) {
        DifficileCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                // TODO Auto-generated method stub
                if(newValue){
                    setSelectedMode(DifficileCheckBox.getText());
                }else{
                    System.out.println("***facile option not Ticked***");
                }
            }
        });
    }
}