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

public class BuyDevelopmentCard extends TurnAction implements AbleToRemoveResources {
    private final Player player;
    private DevelopmentCard card;
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
            if (Arrays.stream(player.getDevelopmentSlots()).anyMatch(developmentSlot -> developmentSlot.canAddCard(developmentCard)) && developmentCard.isEligible(player))
                cardsAvailable.add(developmentCard);
        }
        return !cardsAvailable.isEmpty();
    }

    @Override
    public void doAction(){
        Player playerClone;
        int chosenSlot;
        //TODO cardAvailable should be updated each time (so that do-while is not needed)
        do {
            if (cardsAvailable.size() == 1){
                card = cardsAvailable.get(0);
                view.showMessage("You can buy only this card: " + card.toString());
            }
            else
                card = cardsAvailable.get(view.buyDevelopmentCards(cardsAvailable));
            chosenSlot = chooseSlot();
        }while (!player.getPersonalBoard().canAddCardToSlot(card,chosenSlot));
        playerClone = AbleToRemoveResources.clone(player);
        RequirementResource requirementResource;
        int amountRequired;
        for (Requirement requirement: card.getRequirements()){
            requirementResource = (RequirementResource) requirement;
            amountRequired = requirementResource.getQuantity();
            if (player.hasDiscountForResource(requirementResource.getResourceType()))
                amountRequired += player.applyDiscount(requirementResource.getResourceType());
            AbleToRemoveResources.removeResourceFromPlayer(view,resourcesChosen,requirementResource.getResourceType(),playerClone,amountRequired);
        }
        BuyDevelopmentCardDTO buyDevelopmentCardDTO = new BuyDevelopmentCardDTO(card, chosenSlot,resourcesChosen.getResourcesTakenFromWarehouse(),resourcesChosen.getResourcesTakenFromStrongbox());
        clientConnector.sendMessage(buyDevelopmentCardDTO);
    }

    private int chooseSlot() {
        int chosenSlot;
        ArrayList<Integer> slotsAvailable = new ArrayList<>();
        for (DevelopmentSlot developmentSlot: player.getDevelopmentSlots()) {
            if (developmentSlot.canAddCard(card))
                slotsAvailable.add(developmentSlot.getSlotID());
        }
        if (slotsAvailable.size() == 1){
            chosenSlot = slotsAvailable.get(0);
            view.showMessage("You can only put the card in the slot" + chosenSlot);
        }
        else {
            chosenSlot = slotsAvailable.get(view.chooseSlot(slotsAvailable));
        }
        return chosenSlot;
    }
}
