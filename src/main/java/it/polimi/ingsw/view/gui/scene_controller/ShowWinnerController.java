package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class ShowWinnerController {
    @FXML
    private Label winnerLabel;

    public void initialize(){
        String winnerUsername = GUIController.getInstance().getWinner();
        winnerLabel.setText(winnerUsername);
    }
}
