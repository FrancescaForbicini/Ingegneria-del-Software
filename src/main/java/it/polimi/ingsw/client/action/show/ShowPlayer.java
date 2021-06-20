package it.polimi.ingsw.client.action.show;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.turn_taker.ClientPlayer;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;

public class ShowPlayer extends ShowAction {
    private Player currentPlayer;
    private ArrayList<ClientPlayer> clientPlayers;
    private FaithTrack faithTrack;
    private ArrayList<LeaderCard> nonActiveLeaderCards;

    public ShowPlayer(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    @Override
    public void doAction() {
        currentPlayer = clientGameObserverProducer.getCurrentPlayer();
        faithTrack = clientGameObserverProducer.getFaithTrack();
        clientPlayers = clientGameObserverProducer.getPlayers();
        nonActiveLeaderCards = new ArrayList<>();
        for(ClientPlayer clientPlayer : clientPlayers){
            clientPlayer.setFaithTrack(faithTrack);
        }
        boolean isItself;
        ClientPlayer chosenPlayer;
        if(clientPlayers.size() > 1){
            int indexPlayerChosen = 0;
            view.showMessage("Choose which player do you want to see: ");
            do{
                view.showMessage("Choose a player: ");
                indexPlayerChosen = view.choosePlayer(clientPlayers);
            }while (indexPlayerChosen < 0 || indexPlayerChosen > clientPlayers.size());
            chosenPlayer = clientPlayers.get(indexPlayerChosen);
        } else {
            chosenPlayer = clientPlayers.get(0);
        }
        isItself = chosenPlayer.getUsername().equals(currentPlayer.getUsername());
        if(isItself){
            nonActiveLeaderCards.addAll(currentPlayer.getNonActiveLeaderCards());
        }
        view.showPlayer(chosenPlayer, isItself, nonActiveLeaderCards);
    }
}
