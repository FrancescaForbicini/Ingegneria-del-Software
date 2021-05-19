package it.polimi.ingsw.client;

import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

public class ShowPlayer implements ClientAction{
    @Override
    public void doAction(SocketConnector clientConnector, View view, ClientGame clientGame) {
        view.showPlayer(clientGame.getPlayers());
    }
}
