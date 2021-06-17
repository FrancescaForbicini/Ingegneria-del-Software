package it.polimi.ingsw.client.action.turn;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.development_message.BuyDevelopmentCardDTO;
import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.RequirementResource;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BuyDevelopmentCard extends TurnAction implements Remove {
    private final Player player;
    private DevelopmentCard card;
    private int slot;
    private final ArrayList<DevelopmentCard> cardsAvailable;
    private ResourcesChosen resourcesChosen;
    public BuyDevelopmentCard(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
        player = clientGameObserverProducer.getCurrentPlayer();
        cardsAvailable = new ArrayList<>();
        resourcesChosen = new ResourcesChosen(new HashMap<>(),new HashMap<>());
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
        Player playerClone = new Player(player.getUsername());
        do {
            if (cardsAvailable.size() == 1){
                card = cardsAvailable.get(0);
                view.showMessage("You can buy only this card: " + card.toString());
            }
            else
                chooseDevelopmentCard(cardsAvailable);
            chooseSlot();
        }while (!player.getPersonalBoard().checkAddCard(card,slot));
        playerClone = Remove.clone(player);
        RequirementResource requirementResource;
        int amount;
        for (Requirement requirement: card.getRequirements()){
            requirementResource = (RequirementResource) requirement;
            amount = requirementResource.getQuantity();
            if (player.isDiscount(requirementResource.getResourceType()))
                amount = amount -1;
            Remove.inputFrom(view,resourcesChosen,requirementResource.getResourceType(),playerClone,amount);
        }
        BuyDevelopmentCardDTO buyDevelopmentCardDTO = new BuyDevelopmentCardDTO(card, slot,resourcesChosen.getResourcesTakenFromWarehouse(),resourcesChosen.getResourcesTakenFromStrongbox());
        clientConnector.sendMessage(buyDevelopmentCardDTO);
    }
    private void chooseDevelopmentCard(ArrayList<DevelopmentCard> developmentCardsAvailable){
        card = developmentCardsAvailable.get(view.buyDevelopmentCards(developmentCardsAvailable));
    }

    private void chooseSlot() {
        ArrayList<Integer> slotsAvailable = new ArrayList<>();
        for (DevelopmentSlot developmentSlot: player.getDevelopmentSlots()) {
            if (developmentSlot.checkAddCard(card))
                slotsAvailable.add(developmentSlot.getSlotID());
        }
        if (slotsAvailable.size() == 1){
            slot = slotsAvailable.get(0);
            view.showMessage("You can only put the card in the slot" + slot);
        }
        else {
            slot = slotsAvailable.get(view.chooseSlot(slotsAvailable));
        }
    }
}
