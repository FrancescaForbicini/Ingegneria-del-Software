package it.polimi.ingsw.client.action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.ClientPlayer;
import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.message.action_message.leader_message.ActivateLeaderCardDTO;
import it.polimi.ingsw.message.action_message.leader_message.DiscardLeaderCardsDTO;
import it.polimi.ingsw.message.action_message.leader_message.LeaderActionDTO;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.List;

public class LeaderAction extends ClientAction {

    @Override
    public void doAction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        ClientPlayer player = clientGameObserverProducer.getPlayers().stream().filter(clientPlayer -> clientPlayer.getUsername().equals(clientGameObserverProducer.getUsername())).findAny().get();
        synchronized (clientGameObserverProducer.getPendingTurnDTOs()){
            try{
                if(!clientGameObserverProducer.getPendingTurnDTOs().getLast().getClass().equals(LeaderActionDTO.class))
                    wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        TurnActionMessageDTO turnActionMessageDTO;
        List<LeaderCard> availableCards = clientGameObserverProducer.getLeaderCards();
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
