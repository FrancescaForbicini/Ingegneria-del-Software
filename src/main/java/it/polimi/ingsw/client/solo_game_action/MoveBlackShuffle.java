package it.polimi.ingsw.client.solo_game_action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.solo_game_message.MoveBlackShuffleDTO;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.solo_game.SoloToken;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;



public class MoveBlackShuffle extends SoloGameAction {
    @Override
    public void doAction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        synchronized (clientGameObserverProducer.getSoloTokensDTO()){
            try{
                if (!clientGameObserverProducer.getSoloTokensDTO().getLast().getClass().equals(MoveBlackShuffleDTO.class))
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        MoveBlackShuffleDTO moveBlackShuffleDTO = (MoveBlackShuffleDTO) clientGameObserverProducer.getSoloTokensDTO().getLast();
        clientGameObserverProducer.getOpponent().setDiscardedSoloTokens(moveBlackShuffleDTO.getSoloTokensDiscarded());
        clientGameObserverProducer.getOpponent().setSoloTokens(moveBlackShuffleDTO.getSoloTokensToPick());

        MoveBlackShuffleDTO moveBlackShuffleUpdated = new MoveBlackShuffleDTO(shuffleSoloTokens(clientGameObserverProducer),null,moveBlackCross(clientGameObserverProducer));
        view.showMoveBlackShuffle(moveBlackShuffleUpdated.getSoloTokensToPick(),moveBlackShuffleUpdated.getFaithTrack());
        clientConnector.sendMessage(moveBlackShuffleUpdated);

        //OK message
        synchronized (clientConnector.receiveAnyMessage()){
            try{
                if (clientConnector.receiveAnyMessage().isEmpty())
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }

    }

    private Deck<SoloToken> shuffleSoloTokens(ClientGameObserverProducer clientGameObserverProducer){
        return clientGameObserverProducer.getOpponent().resetDecks();
    }
    private FaithTrack moveBlackCross(ClientGameObserverProducer clientGameObserverProducer){
        clientGameObserverProducer.getFaithTrack().move(clientGameObserverProducer.getOpponent(),1);
        return clientGameObserverProducer.getFaithTrack();
    }
}
