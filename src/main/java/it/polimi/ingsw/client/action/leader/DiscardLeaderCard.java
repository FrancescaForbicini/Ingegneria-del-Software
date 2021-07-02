package it.polimi.ingsw.client.action.leader;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.message.action_message.leader_message.DiscardLeaderCardsDTO;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.server.connector.Connector;
import it.polimi.ingsw.view.View;

/**
 * Discard a Leader Card
 */
public class DiscardLeaderCard extends LeaderAction {

    public DiscardLeaderCard(Connector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public boolean isDoable() {
        return clientGameObserverProducer.getCurrentPlayer().getNonActiveLeaderCards().size() > 0;
    }

    /**
     * {@inheritDoc}
     *
     * Discards a chosen Leader Card and give 1 faithpoint to the player who discarded it
     *
     */
    @Override
    public void doAction() {
        Player player = clientGameObserverProducer.getCurrentPlayer();
        int pickedLeaderCardIndex = view.pickLeaderCard(player.getNonActiveLeaderCards());
        LeaderCard pickedLeaderCard = player.getNonActiveLeaderCards().get(pickedLeaderCardIndex);
        ActionMessageDTO actionMessageDTO = new DiscardLeaderCardsDTO(pickedLeaderCard);
        clientConnector.sendMessage(actionMessageDTO);
    }
}
