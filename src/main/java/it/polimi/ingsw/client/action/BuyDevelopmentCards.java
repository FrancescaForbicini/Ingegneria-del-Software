package it.polimi.ingsw.client.action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.development_message.BuyDevelopmentCardDTO;
import it.polimi.ingsw.message.action_message.development_message.ChooseDevelopmentCardDTO;
import it.polimi.ingsw.message.action_message.development_message.ChooseSlotDTO;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

public class BuyDevelopmentCards implements ClientAction {

    @Override
    public void doAction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {

        synchronized (clientGameObserverProducer.getActions()){
            try{
                if(!clientGameObserverProducer.getPendingTurnDTOs().getLast().getClass().equals(BuyDevelopmentCardDTO.class))
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        BuyDevelopmentCardDTO buyDevelopmentCard = new BuyDevelopmentCardDTO(clientGameObserverProducer.getDevelopmentCards());
        chooseDevelopmentCard(clientConnector, view, clientGameObserverProducer, buyDevelopmentCard);
        chooseSlot(clientConnector, view, clientGameObserverProducer);

        //OK message
        synchronized (clientGameObserverProducer.getPendingTurnDTOs()){
            try{
                if (clientConnector.receiveAnyMessage().isEmpty())
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    private void chooseDevelopmentCard(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer, BuyDevelopmentCardDTO buyDevelopmentCard){
        synchronized (clientGameObserverProducer.getPendingTurnDTOs()){
            try{
                if (!clientGameObserverProducer.getPendingTurnDTOs().getLast().getClass().equals(ChooseDevelopmentCardDTO.class))
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        ChooseDevelopmentCardDTO chooseDevelopmentCard = new ChooseDevelopmentCardDTO(view.buyDevelopmentCards(buyDevelopmentCard.getDevelopmentCards()));
        clientConnector.sendMessage(chooseDevelopmentCard);
    }

    private void chooseSlot(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer){
        synchronized (clientGameObserverProducer.getPendingTurnDTOs()){
            try{
                if (!clientGameObserverProducer.getPendingTurnDTOs().getLast().getClass().equals(ChooseSlotDTO.class))
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        ChooseSlotDTO chooseSlot = new ChooseSlotDTO(view.chooseSlot());
        clientConnector.sendMessage(chooseSlot);
    }
}
