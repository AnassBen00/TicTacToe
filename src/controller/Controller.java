package controller;

import ai.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import utilitaire.NumberFormater;

import java.io.IOException;
import java.util.Arrays;

public class Controller {

    @FXML
    private TextField outputText;
    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private ProgressBar pb;
    @FXML
    private ProgressIndicator pi;

    private Thread progressBarThread;
    private Task<Void> task;

    private String fileModel;
    private MultiLayerPerceptron net;

    @FXML
    public void initialize() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../application/Difficulty.fxml"));
        Parent root = loader.load();
        DifficultyController controller = loader.getController();
        fileModel = DifficultyController.fileModel;
        System.out.println("022222" +fileModel);

        net = controller.getNet();
        //startTest();
    }


    public void startTest() {
        stopButton.setDisable(false);
        startButton.setDisable(true);
        task =  task1();


        pb.setProgress(0);
        pi.setProgress(0);



        pb.progressProperty().bind(task.progressProperty());
        pi.progressProperty().bind(task.progressProperty());
        progressBarThread =  new Thread(task);
        progressBarThread.start();


        pb.progressProperty().bind(task.progressProperty());
        pi.progressProperty().bind(task.progressProperty());
        task.progressProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                pb.progressProperty().bind(task.progressProperty());
                pi.progressProperty().bind(task.progressProperty());
            }
        });
        task.messageProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                System.out.println("++++++++++++++++++++"+task.getMessage());
                outputText.setText(task.getMessage());
            }
        });
    }


    private Task<Void> task1() {
        return new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                //boolean deletePrevious = true;

                double startTime = System.nanoTime();

                int currentTrainingCount = 0;
                int epochs = 1_000_000;
                int currentTrainingTotal = 1_000_000;

                // Format big numbers into more readable ones
                System.out.println("Beginning training for " + NumberFormater.formatNumber(epochs) + " epochs...");

                // Beginning
                int total = 0;
                try {
                    int[] layers = new int[]{ 9, 9, 9};

                    double error = 0.0 ;

                    net = MultiLayerPerceptron.load("./resources/models/"+fileModel);
                    if (net == null) {
                        net = new MultiLayerPerceptron(layers, 0.1, new SigmoidalTransferFunction());
                    }
                    //TRAINING ...
                    for (int i = 0; i < epochs; i++) {
                        currentTrainingCount++;

                        // Generate a game where either the AI or the player has won
                        do {
                            Trainer.reset();
                            Trainer.generateGame();
                        } while (Trainer.winner == 0);

                        // Train the AI (not) to reproduce each step depending on if it has won or not
                        for (int j = 0; j < Trainer.states.size(); j++) {

                            // Give the AI each step as an input
                            double[] inputs = Trainer.states.get(j);
                            double[] outputs = new double[inputs.length];
                            Arrays.fill(outputs, 0);

                            try {
                                if (Trainer.winner == Trainer.ai) {
                                    outputs[Trainer.nextTile.get(j)] = 1;
                                }
                                // Following else is to uncomment only if we do not fill the outputs with '0', otherwise it's useless
//                        else {
//                            outputs[Trainer.nextTile.get(j)] = 0;
//                        }
                            } catch (IndexOutOfBoundsException e) {
                                continue;
                            }

                            error += net.backPropagate(inputs, outputs);
                            updateProgress(i,epochs);
                        }

                        if ( i % (epochs / 100) == 0) {
                            System.out.println("Error at step " + NumberFormater.formatNumber(i) + " is " + (error/(double)i));
                            updateMessage("Error at step " + NumberFormater.formatNumber(i) + " is " + (error/(double)i));
                        }
                    }

                    error /= epochs ;
                    System.out.println("Error is " + error);

                    System.out.println("Learning completed!");
                    updateMessage("Learning completed!");
                    stopButton.setDisable(true);

                    // Save the training
                    net.trainingCount += epochs;
                    net.save("./resources/models/"+fileModel);

                    total = net.trainingCount;
                }
                catch (Exception e) {
                    System.out.println("AI.train()");
                    e.printStackTrace();
                    System.exit(-1);
                }

                double endTime = System.nanoTime();
                System.out.println("Done in: " + (endTime - startTime) / 1_000_000_000);

                return null;
            }
        };
    }




    public void buttonStopActionPerformed(ActionEvent actionEvent) {
        stopButton.setDisable(false);
        stopButton.setDisable(true);
        task.cancel(true);

        progressBarThread.stop();

        outputText.setText(task.getMessage());
        //progressBar.setProgress(0);
        //progressIndicator.setProgress(0);


    }

}