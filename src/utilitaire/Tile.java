package utilitaire;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.Serializable;


public class Tile implements Serializable {

    private transient Pane pane;

    private int x;
    private int y;
    private Player player;

    private String backgroundPath;
    private transient ImageView background;
    private int rotation;


    Tile(int x, int y, String backgroundImagePath) {
        this.x = x;
        this.y = y;
        backgroundPath = backgroundImagePath;

        // Init the transients variables
        initTransients();
    }

    public Pane getPane() {
        return pane;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getBackgroundPath() {
        return backgroundPath;
    }

    public ImageView getBackground() {
        return background;
    }

    public int getRotation() {
        return rotation;
    }

    public void initTransients() {
        // Init background image from path
        background = new ImageView(new Image(backgroundPath));

        // Init pane
        pane = new Pane();
        pane.getChildren().add(background);
        background.fitWidthProperty().bind(pane.widthProperty());
        background.fitHeightProperty().bind(pane.heightProperty());

        // Reset background sprite rotation
        resetRotation(this.rotation);
    }

    public void resetRotation(int rotation) {
        this.rotation = rotation;
        background.setRotate(rotation);
    }
}
