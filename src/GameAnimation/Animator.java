package GameAnimation;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import utilitaire.Player;
import utilitaire.Tile;

public class Animator {


     public static void animateTitle(ImageView title) {
        ScaleTransition st = new ScaleTransition(Duration.millis(2000), title);
        st.setByX(-0.2);
        st.setByY(-0.2);
        st.setAutoReverse(true);
        st.setCycleCount(Timeline.INDEFINITE);
        st.play();
    }

    public static void animateFadingNode(Node node, double from, double to, int durationMillis, int count) {
        FadeTransition ft = new FadeTransition(Duration.millis(durationMillis), node);
        ft.setFromValue(from);
        ft.setToValue(to);
        ft.setAutoReverse(true);
        ft.setCycleCount(count);
        ft.play();
    }


    public static void animateClickedTile(Tile tile) {
        ImageView playerShape = new ImageView(tile.getPlayer().getShape());
        System.out.println(tile.getPlayer().getShape().toString());

        ScaleTransition st = new ScaleTransition(Duration.millis(200), playerShape);
        st.setFromX(0);
        st.setFromY(0);
        st.setToX(1);
        st.setToY(1);
        st.play();

        tile.getPane().getChildren().add(playerShape);
        playerShape.fitWidthProperty().bind(tile.getPane().widthProperty());
        playerShape.fitHeightProperty().bind(tile.getPane().heightProperty());
    }

    public static void animateWinningLine(Pane surface, double x1, double y1, double x2, double y2) {
        Line line = new Line(x1, y1, x2, y2);
        line.setStrokeWidth(10);
        line.setStyle("-fx-stroke:green");

        ScaleTransition st = new ScaleTransition(Duration.millis(500), line);
        st.setFromX(0);
        st.setFromY(0);
        st.setToX(1.1);
        st.setToY(1.1);
        st.play();

        surface.setDisable(false);
        surface.getChildren().add(line);
    }

    public  static void animateScore(Label label, Player player) {
        label.setText(String.valueOf(player.getScore()));
    }

    public static void changeLabel(Label label, String text, String colorCode) {
        label.setText(text);
        label.setStyle("-fx-background-color: " + colorCode);
    }
}

