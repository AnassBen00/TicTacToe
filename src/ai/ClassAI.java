package ai;


import controller.DifficultyController;
import utilitaire.DataManager;
import utilitaire.NumberFormater;
import utilitaire.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class ClassAI {

    static String mediumAIPath = "JavaAI/medium_AI.ser";
    static String hardAIPath = "JavaAI/hard_AI.ser";

    static int currentTrainingCount;
    static int currentTrainingTotal;


    public static int play(int availableTiles) {
        /*// Easy AI (random AI)
        if (DataManager.gameMode.equals("Player vs Easy AI")) {
            return ThreadLocalRandom.current().nextInt(0, availableTiles);
        }

        // Else it's either a medium or a hard AI (both using a neural network)
        // Get the neural network accordingly to the AI difficulty chosen
        MultiLayerPerceptron net = null;

        // Medium AI (neural network trained 1M times)
        if (DataManager.gameMode.equals("Player vs Medium AI")) {
            net = MultiLayerPerceptron.load(mediumAIPath);
        }
        else if (DataManager.gameMode.equals("Player vs Hard AI")) {
            net = MultiLayerPerceptron.load(hardAIPath);
        }*/
        MultiLayerPerceptron net = MultiLayerPerceptron.load("./resources/models/"+DifficultyController.fileModel);



        // Fill the inputs accordingly to the current game board's state
        double[] inputs = new double[9];
        Arrays.fill(inputs, 0);
        for (int i = 0; i < DataManager.gameBoard.getTiles().size(); i++) {
            for (int j = 0; j < DataManager.gameBoard.getTiles().get(0).size(); j++) {
                // If the tile is owned by the player
                Player tileOwner = DataManager.gameBoard.getTiles().get(i).get(j).getPlayer();
                if (tileOwner == DataManager.playerOne) {
//                    System.out.println("Tile (" + i + ", " + j + ") is possessed by " + tileOwner.pseudo + " and is set to -1");
                    inputs[i*3 + j] = -1;
                }
                // Else if it's owned by the AI
                else if (tileOwner == DataManager.playerTwo) {
//                    System.out.println("Tile (" + i + ", " + j + ") is possessed by " + tileOwner.pseudo + " and is set to 1");
                    inputs[i*3 + j] = 1;
                }
            }
        }

//        System.out.println("Gameboard inputs are: ");
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {
//                System.out.print(inputs[i*3 + j] + "\t");
//            }
//            System.out.println();
//        }
//        System.out.println();

        // Get the outputs of the neural network (the weight of each tile)
        assert net != null;
        double[] outputs = net.forwardPropagation(inputs);

//        System.out.println("AI outputs are: ");
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {
//                System.out.print(outputs[i*3 + j] + "\t");
//            }
//            System.out.println();
//        }
//        System.out.println();

        // Choose the tile that has the more weight
        int chosenTileIndex;
        int availableTileIndex = -1;
        do {
            chosenTileIndex = 0;
            double chosenTileWeight = outputs[0];
            for (int i = 1; i < outputs.length; i++) {
                if (outputs[i] > chosenTileWeight) {
                    chosenTileIndex = i;
                    chosenTileWeight = outputs[i];
                }
            }

//            System.out.println("Chosen tile index: " + chosenTileIndex);

            if (inputs[chosenTileIndex] != 0) {
//                System.out.println("- Tile was already owned.");
                outputs[chosenTileIndex] = 0;
            }
            else {
                // Get the available tile index instead of the tile index
                availableTileIndex = chosenTileIndex;
                for (int i = 0; i < chosenTileIndex; i++) {
                    if (inputs[i] != 0) {
                        availableTileIndex--;
                    }
                }

//                System.out.println("Available tile index: " + chosenTileIndex);
            }
        } while (availableTileIndex == -1);

        return availableTileIndex;
    }

   /* static void initMediumAndHardAIs() {
        // Create the dir if it does not exist
        new File("JavaAI").mkdirs();

        // Train the medium AI if it's not already done
        File mediumAI = new File(mediumAIPath);
        if (!mediumAI.exists()) {
            train(mediumAIPath, 1_000_000, true);
        }

        // Copy the medium AI to the "hard AI" if there is none
        File hardAI = new File(hardAIPath);
        if (!hardAI.exists()) {
            try {
                Files.copy(mediumAI.toPath(), hardAI.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ignored) {  }

            // Train a bit more the hard AI
            train(hardAIPath, 1_000_000, false);
        }
    }*/

    static int train(String path, int epochs, boolean deletePrevious) {
        double startTime = System.nanoTime();

        currentTrainingCount = 0;
        currentTrainingTotal = epochs;

        System.out.println("------------------------------");
        System.out.println("--         TRAINING         --");
        System.out.println("------------------------------");

        // Format big numbers into more readable ones
        System.out.println("Beginning training for " + NumberFormater.formatNumber(epochs) + " epochs...");

        // Beginning
        int total = 0;
        try {
            int[] layers = new int[]{ 9, 9, 9 };

            double error = 0.0 ;

            // Create a new multi layer perceptron if we want to override the previous
            MultiLayerPerceptron net;
            if (deletePrevious) {
                net = new MultiLayerPerceptron(layers, 0.1, new SigmoidalTransferFunction());
            }
            // Else, load the previous (if there is one) and continue the training from where we left
            else {
                net = MultiLayerPerceptron.load(path);
                if (net == null) {
                    net = new MultiLayerPerceptron(layers, 0.1, new SigmoidalTransferFunction());
                }
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
                }

                if ( i % (epochs / 100) == 0) {
                    System.out.println("Error at step " + NumberFormater.formatNumber(i) + " is " + (error/(double)i));
                }
            }

            error /= epochs ;
            System.out.println("Error is " + error);

            System.out.println("Learning completed!");

            // Save the training
            net.trainingCount += epochs;
            net.save(path);

            total = net.trainingCount;
        }
        catch (Exception e) {
            System.out.println("AI.train()");
            e.printStackTrace();
            System.exit(-1);
        }

        double endTime = System.nanoTime();
        System.out.println("Done in: " + (endTime - startTime) / 1_000_000_000);

        return total;
    }
}
