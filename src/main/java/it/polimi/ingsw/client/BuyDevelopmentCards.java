package it.polimi.ingsw.client;

import it.polimi.ingsw.message.action_message.development_message.BuyDevelopmentCardDTO;
import it.polimi.ingsw.message.action_message.development_message.ChooseDevelopmentCardDTO;
import it.polimi.ingsw.message.action_message.development_message.ChooseSlotDTO;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

public class BuyDevelopmentCards implements ClientAction{

    @Override
    public void doAction(SocketConnector clientConnector, View view, ClientGame clientGame) {
        synchronized (clientGame){
            try{
                if(clientGame.getTurnActionMessageDTO().stream().noneMatch(turnActionMessageDTO -> turnActionMessageDTO.getClass().equals(BuyDevelopmentCardDTO.class)))
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        BuyDevelopmentCardDTO buyDevelopmentCard = (BuyDevelopmentCardDTO) clientGame.getTurnActionMessageDTO().get();
        chooseDevelopmentCard(clientConnector, view, clientGame, buyDevelopmentCard);
        chooseSlot(clientConnector, view, clientGame);

        //OK message
        synchronized (clientGame){
            try{
                if (clientConnector.receiveAnyMessage().isEmpty())
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    private void chooseDevelopmentCard(SocketConnector clientConnector, View view, ClientGame clientGame, BuyDevelopmentCardDTO buyDevelopmentCard){
        synchronized (clientGame){
            try{
                if (clientGame.getTurnActionMessageDTO().stream().noneMatch(turnActionMessageDTO -> turnActionMessageDTO.getClass().equals(ChooseDevelopmentCardDTO.class)))
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        ChooseDevelopmentCardDTO chooseDevelopmentCard = (ChooseDevelopmentCardDTO) clientGame.getTurnActionMessageDTO().get();
        chooseDevelopmentCard.setCard(view.buyDevelopmentCards(buyDevelopmentCard.getDevelopmentCards()));
        clientConnector.sendMessage(chooseDevelopmentCard);
    }

    private void chooseSlot(SocketConnector clientConnector, View view, ClientGame clientGame){
        synchronized (clientGame){
            try{
                if (clientGame.getTurnActionMessageDTO().stream().noneMatch(turnActionMessageDTO -> turnActionMessageDTO.getClass().equals(ChooseSlotDTO.class)))
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        ChooseSlotDTO chooseSlot = (ChooseSlotDTO) clientGame.getTurnActionMessageDTO().get();
        chooseSlot.setSlotID(view.chooseSlot());
        clientConnector.sendMessage(chooseSlot);
    }
}
