package utilitaire;

import javafx.scene.image.Image;

import java.io.Serializable;

public class Player implements Serializable {

    private String name;
    private int score;
    private String color;


    private transient Image shape;
    private String shapePath;

    public Player(String name,String shapePath,int score){
        this.name=name;
        this.score=score;
        this.shapePath = shapePath;

        // Init the transients variables
        initTransients();
    }


    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getColor() {
        return color;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Image getShape() {
        return shape;
    }

    public void setShape(Image shape) {
        this.shape = shape;
    }

    public String getShapePath() {
        return this.shapePath;
    }

    public void setShapePath(String shapePath) {
        this.shapePath = shapePath;
    }

    public void initTransients() {
        shape = new Image(this.getShapePath());

    }
}
