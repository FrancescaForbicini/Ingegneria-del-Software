package it.polimi.ingsw.client.action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.PickLeaderCardsDTO;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.util.List;

public class PickLeaderCards extends ClientAction{
    public PickLeaderCards(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    @Override
    public void doAction() {
        PickLeaderCardsDTO loginMessageDTO = (PickLeaderCardsDTO) clientGameObserverProducer.getPendingTurnDTOs().pop();
        List<LeaderCard> proposedCards = loginMessageDTO.getCards();
        List<LeaderCard> pickedCards = null;
        try {
            pickedCards = view.pickLeaderCards(proposedCards);
        } catch (IOException e) {
            // TODO better handling?
            e.printStackTrace();
        }
        clientConnector.sendMessage(new PickLeaderCardsDTO(pickedCards));
    }
}
