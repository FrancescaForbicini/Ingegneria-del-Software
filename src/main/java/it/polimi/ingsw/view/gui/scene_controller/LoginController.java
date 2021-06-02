package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginController{
    @FXML
    private TextField usernameField;
    @FXML
    private TextField gameIDField;
    @FXML
    private Button loginButton;

    @FXML
    public void initialize(){
        bindEvents();
    }

    private void bindEvents(){
        loginButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> setCredentials());
    }

    private void setCredentials(){
        String username = usernameField.getText();
        GUIController.getInstance().setUsername(username);
        String gameID = gameIDField.getText();
        GUIController.getInstance().setGameID(gameID);
        notifyAll();
    }

}
