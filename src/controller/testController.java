package controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class testController {

    @FXML private ImageView grid;
    @FXML private Pane pane;



    @FXML
    public void initialize(){
        Image img = new Image("file:resources/images/grid.png");
        grid  = new ImageView(img);


        grid.setPreserveRatio(true);

        pane.getChildren().add(grid);
    }
}
