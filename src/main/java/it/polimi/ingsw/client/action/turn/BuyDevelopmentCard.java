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

/**
 * Buys a development card
 */
public class BuyDevelopmentCard extends TurnAction{
    private Player player;
    private DevelopmentCard cardChosen;
    private ArrayList<DevelopmentCard> cardsAvailable;
    private final  ResourcesChosen resourcesChosen;

    public BuyDevelopmentCard(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
        cardsAvailable = new ArrayList<>();
        resourcesChosen = new ResourcesChosen(new HashMap<>(),new HashMap<>());
    }


    /**
     * Checks if the player can buy a development card
     * @return true iff there are cards that can be bought
     */
    @Override
    public boolean isDoable(){
        checkCardsAvailable(clientGameObserverProducer.getCurrentPlayer());
        return !cardsAvailable.isEmpty();
    }

    /**
     * Buys the card chosen by the player
     */
    @Override
    public void doAction(){
        player = clientGameObserverProducer.getCurrentPlayer();

        checkCardsAvailable(player);

        cardChosen = cardsAvailable.get(view.buyDevelopmentCards(cardsAvailable));

        int slotChosen = chooseSlot(player);

        takeResourcesFrom(player);

        BuyDevelopmentCardDTO buyDevelopmentCardDTO = new BuyDevelopmentCardDTO(cardChosen, slotChosen,resourcesChosen.getResourcesTakenFromWarehouse(),resourcesChosen.getResourcesTakenFromStrongbox());
        clientConnector.sendMessage(buyDevelopmentCardDTO);
    }

    /**
     * Checks if there are cards that can be bought
     */
    private void checkCardsAvailable (Player player){
        cardsAvailable = new ArrayList<>();
        for(DevelopmentCard developmentCard: clientGameObserverProducer.getDevelopmentCards()) {
            if (Arrays.stream(player.getDevelopmentSlots()).anyMatch(developmentSlot -> developmentSlot.canAddCard(developmentCard)) && developmentCard.isEligible(player))
                cardsAvailable.add(developmentCard);
        }
    }

    /**
     * Asks to the player to choose the slot to put the development card bought
     * @return the slot chosen
     */
    private int chooseSlot(Player player) {
        DevelopmentSlot chosenSlot;
        ArrayList<DevelopmentSlot> slotsAvailable = new ArrayList<>();
        for (DevelopmentSlot developmentSlot: player.getDevelopmentSlots()) {
            if (developmentSlot.canAddCard(cardChosen))
                slotsAvailable.add(developmentSlot);
        }
        if (slotsAvailable.size() == 1){
            chosenSlot = slotsAvailable.get(0);
            view.showMessage("You can only put the card in the slot " + chosenSlot.getSlotID() + "\n");
        }
        else {
            chosenSlot = slotsAvailable.get(view.chooseSlot(slotsAvailable));
        }
        return chosenSlot.getSlotID();
    }

    /**
     * Takes the resources from the warehouse or strongbox to buy the development card
     */
    private void takeResourcesFrom(Player player){
        RequirementResource requirementResource;
        int amountRequired;
        Player playerClone = RequireToRemoveResources.clone(player);
        for (Requirement requirement: cardChosen.getRequirements()){
            requirementResource = (RequirementResource) requirement;
            amountRequired = requirementResource.getQuantity();
            if (player.hasDiscountForResource(requirementResource.getResourceType()))
                amountRequired += player.applyDiscount(requirementResource.getResourceType());
            RequireToRemoveResources.removeResourceFromPlayerClone(view,resourcesChosen,requirementResource.getResourceType(),playerClone,amountRequired);
        }
    }
}
