package utilitaire;

import controller.GameController;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class GameBoard implements Serializable {

    private List<List<Tile>> tiles = new ArrayList<>();
    private int rows;
    private int columns;

    private double width;
    private double height;

    private int winningCombo;
    private List<Tile> winningTiles = new ArrayList<>();

    private int turn = 1;

    private Player currentPlayer;
    private boolean hasWinner;


    public GameBoard(int rows, int columns, int winningCombo, double width, double height) {
        currentPlayer = DataManager.playerOne;
        this.rows = rows;
        this.columns = columns;
        this.winningCombo = winningCombo;
        this.width = width;
        this.height = height;

        for (int y = 0; y < rows; y++) {

            List<Tile> row = new ArrayList<>();
            for (int x = 0; x < columns; x++) {

                if (x == 0 && y == 0) {                     // Top left corner
                    row.add(new Tile(x, y, Sprite.backgroundAnglePath));
                }
                else if (x == rows-1 && y == 0) {           // Top right corner
                    Tile tile = new Tile(x, y, Sprite.backgroundAnglePath);
                    tile.resetRotation(90);
                    row.add(tile);
                }
                else if (x == rows-1 && y == columns-1) {   // Bottom right corner
                    Tile tile = new Tile(x, y, Sprite.backgroundAnglePath);
                    tile.resetRotation(180);
                    row.add(tile);
                }
                else if (x == 0 && y == columns-1) {        // Bottom left corner
                    Tile tile = new Tile(x, y, Sprite.backgroundAnglePath);
                    tile.resetRotation(270);
                    row.add(tile);
                }
                else if (y == 0) {                          // Top row
                    row.add(new Tile(x, y, Sprite.backgroundEdgePath));
                }
                else if (x == rows-1) {                     // Right column
                    Tile tile = new Tile(x, y, Sprite.backgroundEdgePath);
                    tile.resetRotation(90);
                    row.add(tile);
                }
                else if (y == columns-1) {                  // Bottom row
                    Tile tile = new Tile(x, y, Sprite.backgroundEdgePath);
                    tile.resetRotation(180);
                    row.add(tile);
                }
                else if (x == 0) {                          // Left column
                    Tile tile = new Tile(x, y, Sprite.backgroundEdgePath);
                    tile.resetRotation(270);
                    row.add(tile);
                }
                else {                                      // Middle tiles
                    row.add(new Tile(x, y, Sprite.backgroundMiddlePath));
                }
            }
            tiles.add(row);
        }
    }

    public List<List<Tile>> getTiles() {
        return tiles;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public int getWinningCombo() {
        return winningCombo;
    }

    public List<Tile> getWinningTiles() {
        return winningTiles;
    }

    public int getTurn() {
        return turn;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean getHasWinner() {
        return hasWinner;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setWinningCombo(int winningCombo) {
        this.winningCombo = winningCombo;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setHasWinner(boolean hasWinner) {
        this.hasWinner = hasWinner;
    }

    public void setTiles(List<List<Tile>> tiles) {
        this.tiles = tiles;
    }

    public void setWinningTiles(List<Tile> winningTiles) {
        this.winningTiles = winningTiles;
    }

    // Used when de-serializing (loading) a game
    public void initPanes() {
        for (List<Tile> row : tiles) {
            for (Tile tile : row) {
                System.out.print("X ");
                tile.initTransients();
            }
            System.out.println();
        }
    }

    public void switchPlayerTurn() {
        if (currentPlayer == DataManager.playerOne) {
            currentPlayer = DataManager.playerTwo;
        }
        else {
            currentPlayer = DataManager.playerOne;
        }
    }

    public boolean isFull() {
        return turn == rows * columns;
    }

    public int getAvailableTiles() {
        return (rows * columns) - turn + 1;
    }

    public Tile getTileAt(int x, int y) {
        return tiles.get(y).get(x);
    }

    public Pair<Tile, Tile> getWinningLineEnds() {
        Tile first = null;
        Tile second = null;

        int firstWeight = -1;
        int secondWeight = 1_000_000;

        for (Tile tile : winningTiles) {
            int weight = tile.getY()*columns + tile.getX();
            if (weight > firstWeight) {
                first = tile;
                firstWeight = weight;
            }
            if (weight < secondWeight) {
                second = tile;
                secondWeight = weight;
            }
        }

        return new Pair<>(first, second);
    }

    public Pair<Double, Double> getTileMiddleXYCoordinates(Tile tile) {
        double tileWidth = width/columns;
        double tileHeight = height/rows;

        double x = tile.getX() * tileWidth + tileWidth/2;
        double y = tile.getY() * tileHeight + tileHeight/2;

        return new Pair<>(x, y);
    }

    public Pair<Pair<Double, Double>, Pair<Double, Double>> getWinningLineXYCoordinates() {
        Pair<Tile, Tile> winningLineEnds = getWinningLineEnds();
        Tile first = winningLineEnds.getKey();
        Tile second = winningLineEnds.getValue();

        Pair<Double, Double> firstCoords = getTileMiddleXYCoordinates(first);
        Pair<Double, Double> secondCoords = getTileMiddleXYCoordinates(second);

        return new Pair<>(firstCoords, secondCoords);
    }
}
