package it.polimi.ingsw.client.action.turn_action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.message.action_message.development_message.BuyDevelopmentCardDTO;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.Arrays;

public class BuyDevelopmentCard extends ClientAction {
    private final Player player;
    private DevelopmentCard card;
    private int slot;
    private ArrayList<DevelopmentCard> cardsAvailable;

    public BuyDevelopmentCard(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
        player = clientGameObserverProducer.getCurrentPlayer();

    }

    @Override
    public boolean isDoable(){
        for(DevelopmentCard developmentCard: clientGameObserverProducer.getDevelopmentCards()) {
            if (Arrays.stream(player.getDevelopmentSlots()).anyMatch(developmentSlot -> developmentSlot.addCard(developmentCard)))
                cardsAvailable.add(developmentCard);
        }
        return cardsAvailable.isEmpty();
    }

    @Override
    public void doAction(){
        do {
            chooseDevelopmentCard(cardsAvailable);
            chooseSlot();
        }while (card.buy(player,slot));
        BuyDevelopmentCardDTO buyDevelopmentCardDTO = new BuyDevelopmentCardDTO(card, slot);
        clientConnector.sendMessage(buyDevelopmentCardDTO);
    }
    private void chooseDevelopmentCard(ArrayList<DevelopmentCard> developmentCardsAvailable){
        card = view.buyDevelopmentCards(developmentCardsAvailable);
    }

    private void chooseSlot(){
        slot = view.chooseSlot();
    }
}
