package it.polimi.ingsw.client;

import it.polimi.ingsw.message.action_message.development_message.BuyDevelopmentCardDTO;
import it.polimi.ingsw.message.action_message.development_message.ChooseDevelopmentCardDTO;
import it.polimi.ingsw.message.action_message.development_message.ChooseSlotDTO;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

public class BuyDevelopmentCards implements ClientAction{

    @Override
    public void doAction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        synchronized (clientGameObserverProducer){
            try{
                if(clientGameObserverProducer.getTurnActionMessageDTO().stream().noneMatch(turnActionMessageDTO -> turnActionMessageDTO.getClass().equals(BuyDevelopmentCardDTO.class)))
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        BuyDevelopmentCardDTO buyDevelopmentCard = (BuyDevelopmentCardDTO) clientGameObserverProducer.getTurnActionMessageDTO().get();
        chooseDevelopmentCard(clientConnector, view, clientGameObserverProducer, buyDevelopmentCard);
        chooseSlot(clientConnector, view, clientGameObserverProducer);

        //OK message
        synchronized (clientGameObserverProducer){
            try{
                if (clientConnector.receiveAnyMessage().isEmpty())
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    private void chooseDevelopmentCard(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer, BuyDevelopmentCardDTO buyDevelopmentCard){
        synchronized (clientGameObserverProducer){
            try{
                if (clientGameObserverProducer.getTurnActionMessageDTO().stream().noneMatch(turnActionMessageDTO -> turnActionMessageDTO.getClass().equals(ChooseDevelopmentCardDTO.class)))
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        ChooseDevelopmentCardDTO chooseDevelopmentCard = (ChooseDevelopmentCardDTO) clientGameObserverProducer.getTurnActionMessageDTO().get();
        chooseDevelopmentCard.setCard(view.buyDevelopmentCards(buyDevelopmentCard.getDevelopmentCards()));
        clientConnector.sendMessage(chooseDevelopmentCard);
    }

    private void chooseSlot(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer){
        synchronized (clientGameObserverProducer){
            try{
                if (clientGameObserverProducer.getTurnActionMessageDTO().stream().noneMatch(turnActionMessageDTO -> turnActionMessageDTO.getClass().equals(ChooseSlotDTO.class)))
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        ChooseSlotDTO chooseSlot = (ChooseSlotDTO) clientGameObserverProducer.getTurnActionMessageDTO().get();
        chooseSlot.setSlotID(view.chooseSlot());
        clientConnector.sendMessage(chooseSlot);
    }
}
