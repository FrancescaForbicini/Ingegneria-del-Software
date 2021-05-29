package it.polimi.ingsw.view.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class LoginController extends Pane {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField gameIDField;
    @FXML
    private Button loginButton;

    private String username;
    private String gameID;
    private String IP;


    public String getUsername() {
        return username;
    }

    public String getGameID() {
        return gameID;
    }

    public String getIP() {
        return IP;
    }

    public String areCredentialsPresent(){
        if (username != null && gameID != null)
            return "OK";
        return "KO";
    }

    @FXML
    private synchronized void setUsernameGameID(){
        this.username = usernameField.getText();
        this.gameID = gameIDField.getText();
        System.out.println(username);
        notifyAll();
    }

}
