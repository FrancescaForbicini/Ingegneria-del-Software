package it.polimi.ingsw.client.action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.PickStartingLeaderCardsDTO;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class PickStartingLeaderCards extends ClientAction{
    public PickStartingLeaderCards(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    @Override
    public void doAction() {
        PickStartingLeaderCardsDTO loginMessageDTO = (PickStartingLeaderCardsDTO) clientGameObserverProducer.getPendingTurnDTOs().pop();
        List<LeaderCard> proposedCards = loginMessageDTO.getCards();
        ArrayList<LeaderCard> pickedCards = view.pickStartingLeaderCards(proposedCards);
        clientConnector.sendMessage(new PickStartingLeaderCardsDTO(pickedCards));
    }

    @Override
    public void consumeFrom(ConcurrentLinkedDeque<ClientAction> from) {
        from.remove(this);
    }
}
