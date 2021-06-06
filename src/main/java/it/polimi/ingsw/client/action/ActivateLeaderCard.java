package it.polimi.ingsw.client.action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.message.action_message.leader_message.ActivateLeaderCardDTO;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;


public class ActivateLeaderCard extends ClientAction {

    public ActivateLeaderCard(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    @Override
    public void consumableFrom(ConcurrentLinkedDeque<ClientAction> from) {
        return;
    }

    @Override
    public boolean isDoable() {
        ArrayList<LeaderCard> cards = (ArrayList<LeaderCard>) clientGameObserverProducer.getCurrentPlayer().getNonActivateLeaderCards();
        Player player = clientGameObserverProducer.getCurrentPlayer();
        return cards.size() > 0 && cards.stream().anyMatch(leaderCard -> leaderCard.isEligible(player));
    }

    @Override
    public void doAction() {
        List<LeaderCard> availableCards = clientGameObserverProducer.getCurrentPlayer().getNonActivateLeaderCards();
        ActionMessageDTO actionMessageDTO = new ActivateLeaderCardDTO(view.pickLeaderCardToDiscard(availableCards));
        clientConnector.sendMessage(actionMessageDTO);
    }
}
