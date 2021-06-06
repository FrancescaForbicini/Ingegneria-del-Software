package it.polimi.ingsw.client.action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.message.action_message.leader_message.DiscardLeaderCardsDTO;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;


public class DiscardLeaderCard extends ClientAction {

    public DiscardLeaderCard(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }


    @Override
    public void consumableFrom(ConcurrentLinkedDeque<ClientAction> from) {
        return;
    }

    @Override
    public boolean isDoable() {
        return clientGameObserverProducer.getCurrentPlayer().getNonActivateLeaderCards().size() > 0;
    }

    @Override
    public void doAction() {
        List<LeaderCard> availableCards = clientGameObserverProducer.getCurrentPlayer().getNonActivateLeaderCards();
        ActionMessageDTO actionMessageDTO = new DiscardLeaderCardsDTO(view.pickLeaderCardToDiscard(availableCards));
        clientConnector.sendMessage(actionMessageDTO);
    }
}
