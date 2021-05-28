package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.view.View;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.IOException;

import static javafx.application.Application.launch;

public class MainScene extends VBox {
    public MainScene(View gui){
        /*
        this.setStyle("-fx-background-color: black ");
        this.setAlignment(Pos.CENTER);
        ImageView image = new ImageView("logo.png");
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(25,25,200,100));
        this.getChildren().add(image);
        HBox usernameBox = new HBox();
        Label username = new Label("Username: ");
        username.setAlignment(Pos.CENTER);
        username.setEffect(new InnerShadow());
        username.setFont(Font.font(null, FontWeight.BOLD, 20));
        username.setTextFill(Color.GOLD);
        usernameBox.getChildren().add(username);
        TextField userTextField = new TextField();
        userTextField.setAlignment(Pos.CENTER);
        userTextField.setMaxSize(100,100);
        usernameBox.getChildren().add(userTextField);
        Group groupUsername = new Group(usernameBox);
        this.getChildren().add(groupUsername);
        Label gameID = new Label("Game ID: ");
        gameID.setAlignment(Pos.CENTER);
        HBox gameIDBox = new HBox();
        gameID.setEffect(new InnerShadow());
        gameID.setFont(Font.font(null, FontWeight.BOLD, 20));
        gameID.setTextFill(Color.GOLD);
        gameIDBox.getChildren().add(gameID);
        TextField gameIDField = new TextField();
        gameIDField.setMaxSize(100,100);
        gameIDBox.getChildren().add(gameIDField);
        Group groupGameID = new Group(gameIDBox);
        this.getChildren().add(groupGameID);
        Label IP = new Label("IP: ");;
        HBox IPBox = new HBox();
        IP.setEffect(new InnerShadow());
        IP.setFont(Font.font(null, FontWeight.BOLD, 20));
        IP.setTextFill(Color.GOLD);
        IPBox.getChildren().add(IP);
        TextField IPField = new TextField();
        IPField.setMaxSize(100,100);
        IPField.setAlignment(Pos.CENTER);
        IPBox.getChildren().add(IPField);
        Group groupIP = new Group(IPBox);
        this.getChildren().add(groupIP);
        HBox hBox = new HBox(10);
        Button buttonLogin = new Button("Login");
        //per definire proprietà grafiche bottone
        hBox.setAlignment(Pos.BASELINE_RIGHT);
        buttonLogin.setFont(Font.font(null,FontWeight.EXTRA_BOLD,20));
        hBox.getChildren().add(buttonLogin);
        this.setSpacing(20);
        //Dopo click visualizzo
        final Text click = new Text();
        buttonLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                LoginController loginScene = new LoginController();
                loginScene.setUsername(userTextField.getText());
                loginScene.setGameID(gameID.getText());
                loginScene.setIP(IP.getText());
                //TODO check if the login is valid
                click.setFill(Color.CYAN);
                //TODO start scene
                try {
                    gui.errorStartGame();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        hBox.getChildren().add(click);
        this.getChildren().add(hBox);

         */
    }
}
