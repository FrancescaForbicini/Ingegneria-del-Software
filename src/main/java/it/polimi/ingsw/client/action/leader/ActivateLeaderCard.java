package it.polimi.ingsw.client.action.leader;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.message.action_message.leader_message.ActivateLeaderCardDTO;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.server.connector.Connector;
import it.polimi.ingsw.view.View;
import java.util.ArrayList;

/**
 * Activate a Leader Card
 */
public class ActivateLeaderCard extends LeaderAction {
    private ArrayList<LeaderCard> leaderCardsEligible;
    private Player player;

    public ActivateLeaderCard(Connector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
        leaderCardsEligible = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public boolean isDoable() {
        updateLeaderCards();
        return leaderCardsEligible.size() > 0;
    }

    /**
     * {@inheritDoc}
     *
     * Activates a chosen Leader Card and makes its power usable by the player who activated the Card
     *
     */
    @Override
    public void doAction() {
        LeaderCard pickedLeaderCard;
        player = clientGameObserverProducer.getCurrentPlayer();
        updateLeaderCards();
        int pickedLeaderCardIndex = view.pickLeaderCard(leaderCardsEligible);
        pickedLeaderCard = leaderCardsEligible.get(pickedLeaderCardIndex);
        ActionMessageDTO actionMessageDTO = new ActivateLeaderCardDTO(pickedLeaderCard);
        clientConnector.sendMessage(actionMessageDTO);
    }

    /**
     * Update the list of eligible Leader Cards accordingly with the player's status
     *
     */
    private void updateLeaderCards(){
        player = clientGameObserverProducer.getCurrentPlayer();
        leaderCardsEligible = new ArrayList<>();
        for (LeaderCard leaderCard: player.getNonActiveLeaderCards()){
            if (leaderCard.isEligible(player))
                leaderCardsEligible.add(leaderCard);
        }
    }
}
