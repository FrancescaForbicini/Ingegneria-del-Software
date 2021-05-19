package it.polimi.ingsw.client;

import it.polimi.ingsw.message.action_message.leader_message.ActiveLeaderCardsDTO;
import it.polimi.ingsw.message.action_message.leader_message.DiscardLeaderCardsDTO;
import it.polimi.ingsw.message.action_message.leader_message.LeaderActionMessageDTO;
import it.polimi.ingsw.message.update.PlayerMessageDTO;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

public class LeaderAction implements ClientAction{

    @Override
    public void doAction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        ClientPlayer player = clientGameObserverProducer.getPlayers().stream().filter(clientPlayer -> clientPlayer.getUsername().equals(clientGameObserverProducer.getUsername())).findAny().get();
        synchronized (clientGameObserverProducer){
            try{
                if(clientGameObserverProducer.getTurnActionMessageDTO().isEmpty())
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        synchronized (clientGameObserverProducer){
            try{
                if (clientGameObserverProducer.getTurnActionMessageDTO().stream().noneMatch(turnActionMessageDTO -> turnActionMessageDTO.getClass().equals(LeaderActionMessageDTO.class)))
                    wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        LeaderActionMessageDTO leaderActionMessage = (LeaderActionMessageDTO) clientGameObserverProducer.getTurnActionMessageDTO().get();
        if (leaderActionMessage.getClass().getName().equals(ActiveLeaderCardsDTO.class.getName())){
            ActiveLeaderCardsDTO activeLeaderCards = (ActiveLeaderCardsDTO) leaderActionMessage;
            chooseLeaderActionToActive(clientConnector,view,activeLeaderCards);
        }

        else{
            DiscardLeaderCardsDTO discardLeaderCards = (DiscardLeaderCardsDTO) leaderActionMessage;
            chooseLeaderActionToDiscard(clientConnector,view,discardLeaderCards);
        }

        //update player
        synchronized (clientGameObserverProducer){
            try{
                if(clientGameObserverProducer.getUpdateMessageDTO().stream().noneMatch(updateMessageDTO -> updateMessageDTO.getClass().equals(PlayerMessageDTO.class)))
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private void chooseLeaderActionToActive(SocketConnector clientConnector, View view, ActiveLeaderCardsDTO activeLeaderCards){
        activeLeaderCards.setLeaderCardsToActive(view.activeLeaderCards(activeLeaderCards.getLeaderCardsToActive()));
        clientConnector.sendMessage(activeLeaderCards);
    }
    private void chooseLeaderActionToDiscard(SocketConnector clientConnector, View view, DiscardLeaderCardsDTO discardLeaderCards){
        discardLeaderCards.setLeaderCardsToDiscard(view.discardLeaderCards(discardLeaderCards.getLeaderCardsToDiscard()));
        clientConnector.sendMessage(discardLeaderCards);
    }
}
