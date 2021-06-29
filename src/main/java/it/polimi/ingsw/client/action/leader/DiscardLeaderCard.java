package it.polimi.ingsw.client.action.leader;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.message.action_message.leader_message.DiscardLeaderCardsDTO;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;


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
        Player player = clientGameObserverProducer.getCurrentPlayer();
        int pickedLeaderCardIndex = view.pickLeaderCard(player.getNonActiveLeaderCards());
        LeaderCard pickedLeaderCard = player.getNonActiveLeaderCards().get(pickedLeaderCardIndex);
        ActionMessageDTO actionMessageDTO = new DiscardLeaderCardsDTO(pickedLeaderCard);
        clientConnector.sendMessage(actionMessageDTO);
    }
}
