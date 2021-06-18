package utilitaire;

import controller.GameController;

import java.io.*;

public class DataManager {

    public static int rows = 3;
    public static int columns = 3;
    public static int winningCombo = 3;

    public static String gameMode;
    public static GameBoard gameBoard;
    public static Player playerOne;
    public static Player playerTwo;

    public static void setGameMode(String gameMode) {
        DataManager.gameMode = gameMode;
    }

    public static void setPlayerOne(Player playerOne) {
        DataManager.playerOne = playerOne;
    }

    public static void setPlayerTwo(Player playerTwo) {
        DataManager.playerTwo = playerTwo;
    }

    public static void setRows(int rows) {
        DataManager.rows = rows;
    }

    public static void setColumns(int columns) {
        DataManager.columns = columns;
    }

    public static void setWinningCombo(int winningCombo) {
        DataManager.winningCombo = winningCombo;
    }

    public static void setGameBoard(GameBoard gameBoard) {
        DataManager.gameBoard = gameBoard;
    }

    public static void save() throws IOException {
        // Make sure that the "Save" directory is created


        // Save the data inside of non-static members for quick/easy serialization
        Data save = new Data(rows, columns, winningCombo, gameMode, gameBoard, playerOne, playerTwo);
        String savePath = "./resources/dataset/saves/" + playerOne.getName() + " vs " + playerTwo.getName() + ".ser";

        // Serialize the data
        try {
            FileOutputStream fileOut = new FileOutputStream(savePath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(save);
            out.close();
            fileOut.close();

            System.out.println("Game saved in: \"" + savePath + "\"");
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't save the file.");
        }
    }

    public static void load(File file) throws IOException, ClassNotFoundException {
        FileInputStream fileIn;
        ObjectInputStream in;
        try {
            fileIn = new FileInputStream(file.getPath());
            in = new ObjectInputStream(fileIn);
        } catch (StreamCorruptedException e) {
            System.out.println("Couldn't load the file.");
            return;
        }

        Data save = (Data) in.readObject();
        rows = save.getRows();
        columns = save.getColumns();
        winningCombo = save.getWinningCombo();

        gameMode = save.getGameMode();
        gameBoard = save.getGameBoard();
        playerOne = save.getPlayerOne();
        playerTwo = save.getPlayerTwo();

        in.close();
        fileIn.close();

        // JavaFX objects are not serializable, so we have to re-init them (marked as transient in the code)
        gameBoard.initPanes();
        playerOne.initTransients();
        playerTwo.initTransients();
    }
}