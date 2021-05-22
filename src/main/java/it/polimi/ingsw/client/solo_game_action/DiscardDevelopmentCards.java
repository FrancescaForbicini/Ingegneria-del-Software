package it.polimi.ingsw.client.solo_game_action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.message.action_message.solo_game_message.DiscardDevelopmentCardsDTO;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

public class DiscardDevelopmentCards extends SoloGameAction {

    @Override
    public void doAction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        //TODO a livello server prima vedo se riesco a scartare le due development cards
        synchronized (clientGameObserverProducer.getSoloTokensDTO()){
            try{
                if (!clientGameObserverProducer.getSoloTokensDTO().getLast().getClass().equals(DiscardDevelopmentCardsDTO.class))
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        DiscardDevelopmentCardsDTO discardDevelopmentCardsDTO = (DiscardDevelopmentCardsDTO) clientGameObserverProducer.getSoloTokensDTO().getLast();
        DiscardDevelopmentCardsDTO discardDevelopmentCardsUpdated= new DiscardDevelopmentCardsDTO(view.DevelopmentCardsToDiscard(discardDevelopmentCardsDTO.getDevelopmentCardsAvailable(),discardDevelopmentCardsDTO.getDevelopmentCardsToDiscard()),null);
        clientConnector.sendMessage(discardDevelopmentCardsUpdated);

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
}
