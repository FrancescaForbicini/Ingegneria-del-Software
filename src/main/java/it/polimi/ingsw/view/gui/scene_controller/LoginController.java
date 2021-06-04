package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class LoginController{
    @FXML
    AnchorPane mainPane;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField gameIDField;
    @FXML
    private Button loginButton;

    @FXML
    public void initialize(){
        loginButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> setCredentials());
    }

    private void setCredentials(){
        String username = usernameField.getText();
        GUIController.getInstance().setUsername(username);
        String gameID = gameIDField.getText();
        GUIController.getInstance().setGameID(gameID);
    }

}
