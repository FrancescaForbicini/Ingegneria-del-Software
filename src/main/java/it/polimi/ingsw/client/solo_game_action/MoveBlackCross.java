package it.polimi.ingsw.client.solo_game_action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.message.action_message.solo_game_message.MoveBlackCrossDTO;
import it.polimi.ingsw.message.action_message.solo_game_message.MoveBlackShuffleDTO;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.solo_game.SoloToken;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

public class MoveBlackCross extends SoloGameAction {
    @Override
    public void doAction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        synchronized (clientGameObserverProducer.getSoloTokensDTO()){
            try{
                if (!clientGameObserverProducer.getSoloTokensDTO().getLast().getClass().equals(MoveBlackCrossDTO.class))
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        MoveBlackCrossDTO moveBlackCrossDTO = (MoveBlackCrossDTO) clientGameObserverProducer.getSoloTokensDTO().getLast();

        MoveBlackCrossDTO moveBlackCrossUpdated = new MoveBlackCrossDTO(moveBlackCross(clientGameObserverProducer));
        view.showMoveBlackCross(moveBlackCrossUpdated.getFaithTrack());
        clientConnector.sendMessage(moveBlackCrossDTO);

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

    private FaithTrack moveBlackCross(ClientGameObserverProducer clientGameObserverProducer){
        clientGameObserverProducer.getFaithTrack().move(clientGameObserverProducer.getOpponent(),2);
        return clientGameObserverProducer.getFaithTrack();
    }
}

