package it.polimi.ingsw.client;

import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.message.action_message.leader_message.ActivateLeaderCardDTO;
import it.polimi.ingsw.message.action_message.leader_message.DiscardLeaderCardsDTO;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.List;

public class LeaderAction implements ClientAction{

    @Override
    public void doAction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        TurnActionMessageDTO turnActionMessageDTO;
        List<LeaderCard> availableCards = null; // TODO take from clientGameObserverProducer
        switch (view.chooseLeaderCardAction()) {
            case ACTIVATE:
                turnActionMessageDTO = new ActivateLeaderCardDTO(view.pickLeaderCardToActivate(availableCards));
                break;
            case DISCARD:
                turnActionMessageDTO = new DiscardLeaderCardsDTO(view.pickLeaderCardToDiscard(availableCards));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.chooseLeaderCardAction());
        }
        clientConnector.sendMessage(turnActionMessageDTO);
    }
}
