package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.client.turn_taker.ClientPlayer;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

public class ChoosePlayerController {
    private final ArrayList<ClientPlayer> clientPlayersToChoose;
    @FXML
    private MenuButton chooseButton;

    public ChoosePlayerController(ArrayList<ClientPlayer> clientPlayersToChoose) {
        this.clientPlayersToChoose = clientPlayersToChoose;
    }

    public void initialize(){
        for(ClientPlayer clientPlayer : clientPlayersToChoose){
            MenuItem itemToAdd = new MenuItem();
            itemToAdd.setText(clientPlayer.getUsername());
            itemToAdd.setOnAction(actionEvent -> setPickedPlayerIndex(clientPlayersToChoose.indexOf(clientPlayer)));
            chooseButton.getItems().add(itemToAdd);
        }
    }

    private void setPickedPlayerIndex(int pickedPlayerIndex){
        GUIController.getInstance().setPickedIndex(pickedPlayerIndex);
    }
}
