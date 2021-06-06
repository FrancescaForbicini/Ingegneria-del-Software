package it.polimi.ingsw.client.action.leader;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.message.action_message.leader_message.ActivateLeaderCardDTO;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;
import java.util.ArrayList;


public class ActivateLeaderCard extends LeaderAction {
    private final Player player;

    public ActivateLeaderCard(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
        player = clientGameObserverProducer.getCurrentPlayer();
    }

    @Override
    public boolean isDoable() {
        ArrayList<LeaderCard> cards = (ArrayList<LeaderCard>) player.getNonActiveLeaderCards();
        return cards.size() > 0 && cards.stream().anyMatch(leaderCard -> leaderCard.isEligible(player));
    }

    @Override
    public void doAction() {
        ActionMessageDTO actionMessageDTO = new ActivateLeaderCardDTO(view.pickLeaderCards(player.getNonActiveLeaderCards()));
        clientConnector.sendMessage(actionMessageDTO);
    }
}
