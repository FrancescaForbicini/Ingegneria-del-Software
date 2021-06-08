package it.polimi.ingsw.client.action.leader;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.message.action_message.leader_message.DiscardLeaderCardsDTO;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.List;


public class DiscardLeaderCard extends LeaderAction {

    public DiscardLeaderCard(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    @Override
    public boolean isDoable() {
        return clientGameObserverProducer.getCurrentPlayer().getNonActiveLeaderCards().size() > 0;
    }

    @Override
    public void doAction() {
        List<LeaderCard> availableCards = clientGameObserverProducer.getCurrentPlayer().getNonActiveLeaderCards();
        ActionMessageDTO actionMessageDTO = new DiscardLeaderCardsDTO(view.pickLeaderCard(availableCards));
        clientConnector.sendMessage(actionMessageDTO);
    }
}
