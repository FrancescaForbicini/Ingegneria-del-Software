package it.polimi.ingsw.client.action.show;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.turn_taker.ClientPlayer;
import it.polimi.ingsw.client.turn_taker.ClientTurnTaker;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.server.connector.Connector;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;

/**
 * Shows a player
 */
public class ShowPlayer extends ShowAction {
    private Player currentPlayer;
    private ArrayList<ClientPlayer> clientPlayers;
    private FaithTrack faithTrack;

    public ShowPlayer(Connector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    /**
     * {@inheritDoc}
     *
     * Displays the current status of a chosen Player among a list of available ones
     * Displays the first one if no valid choice should be made
     *
     */
    @Override
    public void doAction() {
        currentPlayer = clientGameObserverProducer.getCurrentPlayer();
        faithTrack = clientGameObserverProducer.getFaithTrack();
        clientPlayers = clientGameObserverProducer.getPlayers();
        ClientPlayer chosenPlayer;
        for(ClientTurnTaker clientTurnTaker : clientPlayers){
            clientTurnTaker.setFaithTrack(faithTrack);
        }
        int chosenPlayerIndex;
        if(clientPlayers.size()>1) {
            view.showMessage("Choose which player do you want to see: ");
            chosenPlayerIndex = view.choosePlayer(clientPlayers);
        }
        else {
            chosenPlayerIndex = 0;
        }
        chosenPlayer = clientPlayers.get(chosenPlayerIndex);
        view.showPlayer(chosenPlayer);
    }
}
