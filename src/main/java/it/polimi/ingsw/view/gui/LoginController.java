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

    private String username;
    private String gameID;
    private Boolean ended = false;


    public String getUsername() {
        return username;
    }

    public String getGameID() {
        return gameID;
    }
    public Boolean isEnded() {
        return ended;
    }

    @FXML
    private void setCredentials(){
        this.username = usernameField.getText();
        GUIApp.getInstance().setUsername(username);
        this.gameID = gameIDField.getText();
        GUIApp.getInstance().setGameID(gameID);
        ended = true;
        //notifyAll();
    }

}
