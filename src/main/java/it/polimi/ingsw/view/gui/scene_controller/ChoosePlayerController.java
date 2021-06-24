package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.client.turn_taker.ClientPlayer;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

public class ChoosePlayerController {
    @FXML
    private MenuButton chooseButton;

    public void initialize(){
        ArrayList<ClientPlayer> clientPlayers = GUIController.getInstance().getPlayersToShow();
        for(ClientPlayer clientPlayer : clientPlayers){
            MenuItem itemToAdd = new MenuItem();
            itemToAdd.setText(clientPlayer.getUsername());
            itemToAdd.setOnAction(actionEvent -> setPickedPlayerIndex(clientPlayers.indexOf(clientPlayer)));
            chooseButton.getItems().add(itemToAdd);
        }
    }

    private void setPickedPlayerIndex(int pickedPlayerIndex){
        GUIController.getInstance().setPickedIndex(pickedPlayerIndex);
    }
}
