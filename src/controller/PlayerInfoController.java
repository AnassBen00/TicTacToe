package controller;

import GameAnimation.Animator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utilitaire.Data;
import utilitaire.DataManager;
import utilitaire.Player;
import utilitaire.Sprite;


import java.io.IOException;


public class PlayerInfoController {

    @FXML
    private TextField playerOne;

    @FXML
    private TextField playerTwo;

    @FXML
    private Button nextBtn;

    @FXML private ImageView circleImg;
    @FXML private ImageView crossImg;
    @FXML private Pane panePlayerOne;
    @FXML private Pane panePlayerTwo;
    @FXML private Pane headerPane;
    @FXML private ImageView headerImage;
    @FXML private Button idBackButton;
    @FXML private Pane mainPane;


    public void nextBtnActionPerformed(ActionEvent actionEvent) throws IOException {


        FXMLLoader loader = null;

        if(DataManager.gameMode.equals("SinglePlayer")){
            DataManager.setPlayerOne(new Player(playerOne.getText(),Sprite.crossPath,0));
            DataManager.setPlayerTwo(new Player("IA computer",Sprite.circlePath,0));
        }
        else if (DataManager.gameMode.equals("MultiPlayer")){
            DataManager.setPlayerOne(new Player(playerOne.getText(),Sprite.crossPath,0));
            DataManager.setPlayerTwo(new Player(playerTwo.getText(),Sprite.circlePath,0));
        }

        if(DataManager.playerOne.getName().equals("") ){
            playerOne.setStyle("-fx-focus-color: red;\n" +
                    "    -fx-faint-focus-color: #ff000022;" );
            playerOne.requestFocus();

        }else if(DataManager.playerTwo.getName().equals("")){
            playerTwo.setStyle("-fx-focus-color: red;\n" +
                    "    -fx-faint-focus-color: #ff000022;" );
            playerTwo.requestFocus();
        }
        else{

        loader = new FXMLLoader(getClass().getResource("../application/game.fxml"));

        Parent root = loader.load();
        Stage stage = (Stage) nextBtn.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        //System.out.println(DataManager.playerOne.getName());
        stage.show();
        }


    }
    public void backBtnActionPerformed(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = null;

        if(DataManager.gameMode.equals("SinglePlayer"))  loader = new FXMLLoader(getClass().getResource("../application/difficulty.fxml"));
        else loader = new FXMLLoader(getClass().getResource("../application/gameMode.fxml"));

        Parent root = loader.load();
        Stage stage = (Stage) nextBtn.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void initialize(){
        Image logo =  new Image("file:resources/images/logolabel.jpg");
        headerImage  = new ImageView(logo);
        headerImage.setFitHeight(95.0);
        headerImage.setFitWidth(431.0);
        headerImage.setX(173.0);
        headerImage.setY(100.0);
        headerImage.setPreserveRatio(true);
        mainPane.getChildren().add(headerImage);

        Animator.animateTitle(headerImage);

        Image img1 = new Image("file:resources/images/cross.png");
        crossImg  = new ImageView(img1);
        crossImg.setFitHeight(136.0);
        crossImg.setFitWidth(137.0);
        crossImg.setX(50.0);
        crossImg.setY(30.0);
        crossImg.setPreserveRatio(true);
        panePlayerOne.getChildren().add(crossImg);

        Image img2 = new Image("file:resources/images/circle.png");
        circleImg  = new ImageView(img2);
        circleImg.setFitHeight(122.0);
        circleImg.setFitWidth(159.0);
        circleImg.setX(110.0);
        circleImg.setY(30.0);
        circleImg.setPreserveRatio(true);
        panePlayerTwo.getChildren().add(circleImg);

        Image backImage = new Image("file:resources/images/left-arrow.png");

        ImageView backView = new ImageView(backImage);

        backView.setFitHeight(23.0);
        backView.setFitWidth(18.0);
        backView.setX(18.0);
        backView.setY(22.0);
        backView.setPreserveRatio(true);

        idBackButton.setGraphic(backView);

        if(DataManager.gameMode.equals("SinglePlayer")) playerTwo.setDisable(true);

    }
}
