package controller;

import GameAnimation.Animator;
import ai.ClassAI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import utilitaire.DataManager;
import utilitaire.GameBoard;
import utilitaire.Player;
import utilitaire.Tile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameController {


    @FXML
    private Label playerOneName;
    @FXML
    private Label playerOneScore;

    @FXML
    private Label playerTwoName;
    @FXML
    private Label playerTwoScore;

    @FXML
    private Pane gridToppingPane;

    @FXML
    private GridPane gridGame;

    @FXML
    private GridPane firstPlayerGridPane;

    @FXML
    private GridPane secondPlayerGridPane;

    @FXML
    private Label currentPlayerTurn;

    private boolean play;

    @FXML
    private Button newGameLauncher;

    @FXML
    private Button backToMenu;

    @FXML private ImageView playerOneImage;
    @FXML private ImageView playerTwoImage;
    @FXML private Pane playerOneImagePane;
    @FXML private Pane playerTwoImagePane;

    @FXML private Label saveLabel;




    @FXML
    public void backToMenuActionPerformed(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../application/mainWindow.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) backToMenu.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void launchNewGame() {
        System.out.println("##############################");
        System.out.println(DataManager.rows + " " + DataManager.columns + " " + DataManager.winningCombo);
        gridToppingPane.getChildren().clear();
        gridToppingPane.setDisable(true);
        DataManager.gameBoard = null;
        initialize();
    }

    private void doPlayerTurn(Tile tile, GameBoard gameBoard) {
        tile.setPlayer(gameBoard.getCurrentPlayer());
        System.out.println("- " + tile.getPlayer().getName() + " chose the tile: (" + tile.getX() + ", " + tile.getY() + ")");

        // Animate the player's shape on the tile
        Animator.animateClickedTile(tile);

        // Check if the current player has won
        checkForWinningPattern(tile);

        // Check if the game board is full and needs a reset
        if (gameBoard.isFull() && !gameBoard.getHasWinner()) {
            System.out.println("No player won this time!");
            play = false;
        }
        else {
            gameBoard.setTurn(gameBoard.getTurn()+1);
        }

        // If no one has won yet (and the gameBoard isn't filled), change the player turn
        if (play) {
            gameBoard.switchPlayerTurn();

            // Update the player turn label
            Animator.changeLabel(currentPlayerTurn, gameBoard.getCurrentPlayer().getName()+"'s Turn", gameBoard.getCurrentPlayer().getColor());
        }
    }
    private void doAITurn(GameBoard gameBoard, int sleepingTimeMillis) {
        // To ensure the player can't play during the AI turn
        gridToppingPane.setDisable(false);

        // Create a task that the AI thread will use
        Runnable AITask = () -> {
            double startTime = System.nanoTime();

            int chosenTile = ClassAI.play(gameBoard.getAvailableTiles());
            for (List<Tile> row : gameBoard.getTiles()) {
                for (Tile tile : row) {
                    if (tile.getPlayer() == null) {
                        if (chosenTile == 0) {
                            // Make the thread sleep if needed so the AI do not play "instantly"
                            double endTime = System.nanoTime();
                            int remainingTime = sleepingTimeMillis - (int)(endTime - startTime)/1_000_000;
                            if (remainingTime > 0) {
                                try {
                                    Thread.sleep(remainingTime);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            // Make the AI play (Platform.runLater() to avoid "IllegalStateException: Not on FX application thread" from GameAnimator
                            Platform.runLater(() -> doPlayerTurn(tile, gameBoard));
                            return;
                        }
                        else {
                            chosenTile--;
                        }
                    }
                }
            }
        };

        // Create a thread for the AI, using the AI task. The thread is essentially here to call "Thread.sleep()" without freezing the UI
        Thread AIThread = new Thread(AITask);
        AIThread.start();

        // Re-enable the player to play
        gridToppingPane.setDisable(true);
    }

    private void checkForWinningPattern(Tile tile) {
        // Try in an horizontal line
        checkInLine(tile, 1, 0);

        // Try in an top-left to bottom-right diagonal
        checkInLine(tile, 1, 1);

        // Try in an vertical line
        checkInLine(tile, 0, 1);

        // Try in an top-right to bottom-left diagonal
        checkInLine(tile, -1, 1);
    }

    private void checkInLine(Tile currentTile, int vectorX, int vectorY) {
        int combo = 1;

        // Will be used if the combo is reached to draw a line on it
        DataManager.gameBoard.setWinningTiles( new ArrayList<>());
        DataManager.gameBoard.getWinningTiles().add(currentTile);

        // Try in a direction
        combo = checkInDirection(currentTile, combo, vectorX, vectorY);

        // Try in the other direction
        checkInDirection(currentTile, combo, -vectorX, -vectorY);
    }

    private int checkInDirection(Tile currentTile, int currentCombo, int vectorX, int vectorY) {
        GameBoard gameBoard = DataManager.gameBoard;
        Player player = currentTile.getPlayer();
        int vectorMultiplier = 1;

        while (true) {
            try {
                Tile tile = gameBoard.getTileAt(currentTile.getX() + (vectorX * vectorMultiplier), currentTile.getY() + (vectorY * vectorMultiplier));
                if (tile.getPlayer() == player) {
                    currentCombo++;
                    gameBoard.getWinningTiles().add(tile);
                    if (currentCombo == gameBoard.getWinningCombo()) {
                        if (!gameBoard.getHasWinner()) {
                            System.out.println(tile.getPlayer().getName() + " won!");
                            currentPlayerTurn.setText(tile.getPlayer().getName() + " won!");
                            // Update the winner's score
                            tile.getPlayer().setScore(tile.getPlayer().getScore()+1);;
                            if (tile.getPlayer() == DataManager.playerOne) {
                                Animator.animateScore(playerOneScore, DataManager.playerOne);
                            }
                            else {
                                Animator.animateScore(playerTwoScore, DataManager.playerTwo);
                            }

                            gameBoard.setHasWinner(true);
                        }

                        // Get the coordinates of the winning pattern
                        Pair<Pair<Double, Double>, Pair<Double, Double>> winningLineCoordinates = gameBoard.getWinningLineXYCoordinates();
                        double x1 = winningLineCoordinates.getKey().getKey();
                        double y1 = winningLineCoordinates.getKey().getValue();
                        double x2 = winningLineCoordinates.getValue().getKey();
                        double y2 = winningLineCoordinates.getValue().getValue();

                        // Draw a line to indicate the winning pattern
                        Animator.animateWinningLine(gridToppingPane, x1, y1, x2, y2);

                        play = false;
                        return currentCombo;
                    }
                    vectorMultiplier++;
                }
                else {
                    return currentCombo;
                }
            } catch (IndexOutOfBoundsException e) {
                return currentCombo;
            }
        }
    }


    private void initializeGameBoard() {
        GameBoard gameBoard = DataManager.gameBoard;

        for (int y = 0; y < gameBoard.getTiles().size(); y++) {

            List<Tile> row = gameBoard.getTiles().get(y);
            for (int x = 0; x < row.size(); x++) {

                Tile tile = row.get(x);
                tile.getPane().setOnMouseClicked(e -> {

                    if ( play && tile.getPlayer() == null) {
                        // Let the player play, and update the interface/game board
                        doPlayerTurn(tile, gameBoard);

                        // If no one has won yet and there is still available tiles
                        if (play) {
                            // Check if the other player is an AI, in this case, let the AI play
                            if (!DataManager.gameMode.equals("MultiPlayer")) {
                                doAITurn(gameBoard, 500);

                            }
                        }
                    }
                });

                // When loading a save, the game board might have already been used
                if (tile.getPlayer() != null) {
                    // Animate the player's shape on the tile
                    Animator.animateClickedTile(tile);

                    // Check if the current player has won
                    if (play && gameBoard.getHasWinner()) {
                        checkForWinningPattern(tile);
                    }

                    // Check if the game board is full and needs a reset
                    if (play && gameBoard.isFull() && !gameBoard.getHasWinner()) {
                        System.out.println("No player won this time!");
                        play = false;
                    }
                }
                System.out.println();
                gridGame.add(tile.getPane(), x, y);
            }
        }
    }


    private void initializeGame(GameBoard gameBoard) {
        gridGame.getChildren().clear();

        play = true;
        int rows = Settings.numRow;
        int columns = Settings.numColumn;
        int winningCombo = DataManager.winningCombo;

        for (int i = 0; i < rows; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.SOMETIMES);
            gridGame.getColumnConstraints().add(colConstraints);
        }

        for (int i = 0; i < columns; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.SOMETIMES);
            gridGame.getRowConstraints().add(rowConstraints);
        }

        if (gameBoard == null) {
            DataManager.gameBoard = new GameBoard(rows, columns, winningCombo, gridGame.getPrefWidth(), gridGame.getPrefHeight());
        }

        initializeGameBoard();
        System.out.println(DataManager.gameBoard.getCurrentPlayer().getName());
        // Init the player turn label
        Animator.changeLabel(currentPlayerTurn, DataManager.gameBoard.getCurrentPlayer().getName()+"'s Turn", DataManager.gameBoard.getCurrentPlayer().getColor());
    }

    @FXML
    private void saveGame() throws IOException {
        DataManager.save();
        saveLabel.setText("Game has been saved.");
    }






    @FXML
    public  void initialize(){

        playerOneName.setText(DataManager.playerOne.getName());
        playerOneScore.setText(String.valueOf(DataManager.playerOne.getScore()));
        Image img1 = new Image("file:resources/images/cross.png");
        playerOneImage  = new ImageView(img1);
        playerOneImage.setFitHeight(60.0);
        playerOneImage.setFitWidth(60.0);
        playerOneImage.setX(38.0);
        playerOneImage.setY(1.0);
        playerOneImage.setPreserveRatio(true);
        playerOneImagePane.getChildren().add(playerOneImage);
        //firstPlayerGridPane.add(playerOneImagePane,0,0);



        playerTwoName.setText(DataManager.playerTwo.getName());
        playerTwoScore.setText(String.valueOf(DataManager.playerTwo.getScore()));
        Image img2 = new Image("file:resources/images/circle.png");
        playerTwoImage  = new ImageView(img2);
        playerTwoImage.setFitHeight(59.0);
        playerTwoImage.setFitWidth(150.0);
        playerTwoImage.setX(14.0);
        playerTwoImage.setY(0.0);
        playerTwoImage.setPreserveRatio(true);
        playerTwoImagePane.getChildren().add(playerTwoImage);
        //secondPlayerGridPane.add(playerTwoImagePane,0,0);

        // Init grid pane & the game board

        initializeGame(DataManager.gameBoard);

    }

    
}
