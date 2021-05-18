package it.polimi.ingsw.client;

import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

public class ShowMarket implements ClientAction{
    @Override
    public void doAction(SocketConnector clientConnector, View view, ClientGame clientGame) {
        view.showMarket(clientGame.getMarket());
    }
}
