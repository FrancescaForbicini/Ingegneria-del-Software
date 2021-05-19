package it.polimi.ingsw.client;

import it.polimi.ingsw.message.action_message.development_message.BuyDevelopmentCardDTO;
import it.polimi.ingsw.message.action_message.development_message.ChooseDevelopmentCardDTO;
import it.polimi.ingsw.message.action_message.development_message.ChooseSlotDTO;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

public class BuyDevelopmentCards implements ClientAction{

    @Override
    public void doAction(SocketConnector clientConnector, View view, ClientGame clientGame) {
        BuyDevelopmentCardDTO buyDevelopmentCard ;
        ChooseDevelopmentCardDTO developmentCard = (ChooseDevelopmentCardDTO) clientConnector.receiveMessage(ChooseDevelopmentCardDTO.class).get();
        developmentCard.setCard(view.buyDevelopmentCards(buyDevelopmentCard.getDevelopmentCardsDeck()));
        clientConnector.sendMessage(developmentCard);
        ChooseSlotDTO chooseSlot = (ChooseSlotDTO) clientConnector.receiveMessage(ChooseSlotDTO.class).get();
        chooseSlot.setSlotID(view.chooseSlot());
        clientConnector.sendMessage(chooseSlot);
    }
}
