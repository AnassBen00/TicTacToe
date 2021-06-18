package utilitaire;

import java.io.Serializable;

public class Data implements Serializable {

    private int rows;
    private int columns;
    private int winningCombo;

    private String gameMode;
    private GameBoard gameBoard;
    private Player playerOne;
    private Player playerTwo;

    Data(int rows, int columns, int winningCombo, String gameMode, GameBoard gameBoard, Player player1, Player player2) {
        this.rows = rows;
        this.columns = columns;
        this.winningCombo = winningCombo;

        this.gameMode = gameMode;
        this.gameBoard = gameBoard;
        this.playerOne = player1;
        this.playerTwo = player2;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getWinningCombo() {
        return winningCombo;
    }

    public String getGameMode() {
        return gameMode;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }
}
