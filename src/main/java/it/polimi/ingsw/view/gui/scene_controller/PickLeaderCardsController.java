package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.client.ClientPlayer;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class PickLeaderCardsController {
    @FXML
    private Button button0;
    @FXML
    private ImageView card0;
    @FXML
    private Button button1;
    @FXML
    private ImageView card1;
    @FXML
    private Button button2;
    @FXML
    private ImageView card2;
    @FXML
    private Button button3;
    @FXML
    private ImageView card3;

    private ArrayList<LeaderCard> proposedLeaderCards;

    public void initialize(){
//        ClientPlayer self = GUIController.getInstance().getSelf();
        ArrayList<ImageView> cards = new ArrayList<>();
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(button0);
        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);
        cards.add(card0);
        cards.add(card1);
        cards.add(card2);
        cards.add(card3);
        proposedLeaderCards = GUIController.getInstance().getLeaderCards();
        if(proposedLeaderCards.size()>2){
            for(LeaderCard leaderCard : proposedLeaderCards){
                buttons.get(proposedLeaderCards.indexOf(leaderCard)).setDisable(false);
                buttons.get(proposedLeaderCards.indexOf(leaderCard)).setVisible(true);
                cards.get(proposedLeaderCards.indexOf(leaderCard)).setImage(new Image(getLeaderCardPath(leaderCard)));
            }
        }else{
            for(Button button : buttons){
                button.setDisable(true);
            }
            buttons.get(0).setVisible(false);
            buttons.get(3).setVisible(false);
            cards.get(1).setImage(new Image(getLeaderCardPath(proposedLeaderCards.get(0))));
//            if(proposedLeaderCards.get(0).isEligible(self)){
//                buttons.get(1).setDisable(false);
//            }
            cards.get(2).setImage(new Image(getLeaderCardPath(proposedLeaderCards.get(1))));
//            if(proposedLeaderCards.get(1).isEligible(self)){
//                buttons.get(2).setDisable(false);
//            }
        }
    }

    private String getLeaderCardPath(LeaderCard leaderCard) {
        String cardType; //TODO maybe add method to LeaderCard class or maybe add path attribute and getPath() method
        if(leaderCard instanceof AdditionalDepot) {
            cardType = ((AdditionalDepot) leaderCard).getDepotResourceType().name();
        }else if(leaderCard instanceof AdditionalTradingRule){
            cardType = ((AdditionalTradingRule) leaderCard).getAdditionalTradingRule().getInput().keySet().getClass().getName();
        }else if(leaderCard instanceof AssignWhiteMarble){
            cardType = ((AssignWhiteMarble) leaderCard).getResourceType().name();
        }else{
            cardType = ((Discount) leaderCard).getResourceType().name();
        }
        return "ing-sw-2021-Forbicini-Fontana-Fanton/src/GUIResources/Cards/LeaderCards/"+
                leaderCard.getClass().toString()+cardType+".png";
    }
}
