package it.polimi.ingsw.client.action.leader;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.message.action_message.leader_message.ActivateLeaderCardDTO;
import it.polimi.ingsw.model.cards.leader_cards.LeaderCard;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;
import java.util.ArrayList;


public class ActivateLeaderCard extends LeaderAction {
    private ArrayList<LeaderCard> leaderCardsEligible;
    private Player player;

    public ActivateLeaderCard(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
        leaderCardsEligible = new ArrayList<>();
    }

    @Override
    public boolean isDoable() {
        updateLeaderCards();
        return leaderCardsEligible.size() > 0;
    }

    @Override
    public void doAction() {
        LeaderCard pickedLeaderCard;
        player = clientGameObserverProducer.getCurrentPlayer();
        updateLeaderCards();
        if (leaderCardsEligible.size() == 1) {
            view.showMessage("You will active this leader card: " + leaderCardsEligible.get(0) + "\n");
            pickedLeaderCard = leaderCardsEligible.get(0);
        }
        else{
            int pickedLeaderCardIndex = view.pickLeaderCard(leaderCardsEligible);
            pickedLeaderCard = leaderCardsEligible.get(pickedLeaderCardIndex);
        }
        ActionMessageDTO actionMessageDTO = new ActivateLeaderCardDTO(pickedLeaderCard);
        clientConnector.sendMessage(actionMessageDTO);
    }

    private void updateLeaderCards(){
        player = clientGameObserverProducer.getCurrentPlayer();
        leaderCardsEligible = new ArrayList<>();
        for (LeaderCard leaderCard: player.getNonActiveLeaderCards()){
            if (leaderCard.isEligible(player))
                leaderCardsEligible.add(leaderCard);
        }
    }
}
