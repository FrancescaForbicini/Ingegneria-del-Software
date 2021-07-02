package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.client.turn_taker.ClientPlayer;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

/**
 * Scenes to choose which player to show
 */
public class ChoosePlayerController implements SceneController {
    private final ArrayList<ClientPlayer> clientPlayersToChoose;
    @FXML
    private MenuButton chooseButton;

    /**
     * Initializes the lists of players
     *
     * @param clientPlayersToChoose lists of the player to choose to show
     */
    public ChoosePlayerController(ArrayList<ClientPlayer> clientPlayersToChoose) {
        this.clientPlayersToChoose = clientPlayersToChoose;
    }

    /**
     * Initializes the scene
     */
    public void initialize(){
        for(ClientPlayer clientPlayer : clientPlayersToChoose){
            MenuItem itemToAdd = new MenuItem();
            itemToAdd.setText(clientPlayer.getUsername());
            itemToAdd.setOnAction(actionEvent -> setPickedPlayerIndex(clientPlayersToChoose.indexOf(clientPlayer)));
            chooseButton.getItems().add(itemToAdd);
        }
    }

    /**
     * Sets the index of the player chosen
     * @param pickedPlayerIndex index of the player chosen
     */
    private void setPickedPlayerIndex(int pickedPlayerIndex){
        GUIController.getInstance().setPickedIndex(pickedPlayerIndex);
    }
}
