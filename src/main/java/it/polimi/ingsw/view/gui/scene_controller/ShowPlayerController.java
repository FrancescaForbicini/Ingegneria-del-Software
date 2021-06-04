package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.client.ClientPlayer;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;


public class ShowPlayerController {
    @FXML
    private ImageView card00;
    @FXML
    private ImageView card01;
    @FXML
    private ImageView card02;
    @FXML
    private ImageView card10;
    @FXML
    private ImageView card11;
    @FXML
    private ImageView card12;
    @FXML
    private ImageView card20;
    @FXML
    private ImageView card21;
    @FXML
    private ImageView card22;

    public void initialize(){
        ClientPlayer player = GUIController.getInstance().getPickedPlayer();

    }
    private String getDevelopmentCardPath(DevelopmentCard developmentCard){
        return "ing-sw-2021-Forbicini-Fontana-Fanton/src/GUIResources/DevelopmentCards/"+
                developmentCard.getColor().toString()+"/"+
                developmentCard.getColor()+developmentCard.getVictoryPoints()+".png";
    }
}
