package it.polimi.ingsw.client.action.turn;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.development_message.BuyDevelopmentCardDTO;
import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.Arrays;

public class BuyDevelopmentCard extends TurnAction {
    private final Player player;
    private DevelopmentCard card;
    private int slot;
    private final ArrayList<DevelopmentCard> cardsAvailable;

    public BuyDevelopmentCard(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
        player = clientGameObserverProducer.getCurrentPlayer();
        cardsAvailable = new ArrayList<>();
    }

    @Override
    public boolean isDoable(){
        for(DevelopmentCard developmentCard: clientGameObserverProducer.getDevelopmentCards()) {
            if (Arrays.stream(player.getDevelopmentSlots()).anyMatch(developmentSlot -> developmentSlot.checkAddCard(developmentCard)) && developmentCard.isEligible(player))
                cardsAvailable.add(developmentCard);
        }
        return !cardsAvailable.isEmpty();
    }

    @Override
    public void doAction(){
        do {
            if (cardsAvailable.size() == 1){
                card = cardsAvailable.get(0);
                view.showMessage("You can buy only this card: " + card.toString());
            }
            else
                chooseDevelopmentCard(cardsAvailable);
            chooseSlot();
        }while (!player.getPersonalBoard().checkAddCard(card,slot));
        BuyDevelopmentCardDTO buyDevelopmentCardDTO = new BuyDevelopmentCardDTO(card, slot);
        clientConnector.sendMessage(buyDevelopmentCardDTO);
    }
    private void chooseDevelopmentCard(ArrayList<DevelopmentCard> developmentCardsAvailable){
        card = view.buyDevelopmentCards(developmentCardsAvailable);
    }

    private void chooseSlot() {
        ArrayList<Integer> slots = new ArrayList<>();
        for (DevelopmentSlot developmentSlot: player.getDevelopmentSlots()) {
            if (developmentSlot.checkAddCard(card))
                slots.add(developmentSlot.getSlotID());
        }
        if (slots.size() == 1){
            slot = slots.get(0);
            view.showMessage("You can only put the card in the slot" + slot);
        }
        else {
            slot = view.chooseSlot();
            while (slots.stream().noneMatch(s -> s.equals(slot))){
                view.showMessage("Error! Choose another slot: ");
                slot = view.chooseSlot();
            }
        }
    }
}
