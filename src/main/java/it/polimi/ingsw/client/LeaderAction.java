package it.polimi.ingsw.client;

import it.polimi.ingsw.message.action_message.LeaderActionMessageDTO;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

public class LeaderAction implements ClientAction{

    @Override
    public void doAction(SocketConnector clientConnector, View view, ClientGame clientGame) {
        LeaderActionMessageDTO leaderActionMessageDTO ;
        LeaderActionMessageDTO leaderAction = (LeaderActionMessageDTO) clientConnector.receiveMessage(leaderActionMessageDTO.getClass()).get();
        leaderAction.setLeaderCardsActivated(view.chooseLeaderAction(leaderAction.getLeaderCardsToActive()));
        clientConnector.sendMessage(leaderActionMessageDTO);
    }
}
