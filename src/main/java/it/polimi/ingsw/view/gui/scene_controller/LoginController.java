package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.view.Credentials;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class LoginController implements SceneController{
    private final String USERNAME = "username";
    private final String GAME_ID = "game_id";
    private final int PLAYERS_NUMBER = 1;

    @FXML
    AnchorPane mainPane;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField gameIDField;
    @FXML
    private TextField playersNumberField;
    @FXML
    private Button loginButton;

    @FXML
    public void initialize(){
        loginButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> setCredentials());
    }

    private void setCredentials(){
        String username = USERNAME;
        String gameID = GAME_ID;
        int playersNumber = PLAYERS_NUMBER;

        if(usernameField.getText()!=null && !usernameField.getText().equals("") &&
                gameIDField.getText()!=null && !gameIDField.getText().equals("") &&
                playersNumberField.getText()!=null && !playersNumberField.getText().equals("")) {
            username = usernameField.getText();
            gameID = gameIDField.getText();
            playersNumber = Integer.parseInt(playersNumberField.getText());
        }
        GUIController.getInstance().setCredentials(new Credentials(username,gameID,playersNumber));
    }

}
