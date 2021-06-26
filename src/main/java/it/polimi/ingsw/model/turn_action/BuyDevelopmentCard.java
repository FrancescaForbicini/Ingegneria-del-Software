package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.RequirementResource;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Map;

public class BuyDevelopmentCard implements TurnAction, RemoveResources{
    private final DevelopmentCard card;
    private final int slotID;
    private final Map<ResourceType,Map<Integer,Integer>> inputFromWarehouse;
    private final Map<ResourceType,Integer> inputFromStrongbox;
    public BuyDevelopmentCard(DevelopmentCard card, int slotID, Map<ResourceType,Map<Integer,Integer>> inputFromWarehouse, Map<ResourceType,Integer> inputFromStrongbox){
        this.card = card;
        this.slotID = slotID;
        this.inputFromWarehouse = inputFromWarehouse;
        this.inputFromStrongbox = inputFromStrongbox;
    }
    /**
     * Buys a card if the player has the right requirements
     * @param player the player that wants buy the development card
     */
    @Override
    public void play(Player player) {
        if(isUserInputCorrect(player)) {
            for (Requirement requirement : card.getRequirements()) {
                RequirementResource requirementResource = (RequirementResource) requirement;
                RemoveResources.removeResources(requirementResource.getResourceType(), player, inputFromWarehouse, inputFromStrongbox);
                //RemoveResources.removeResources(totalAmountToRemove, requirementResource.getResourceType(), player, inputFromWarehouse, inputFromStrongbox);
            }
            Game.getInstance().removeDevelopmentCard(card);
            player.addDevelopmentCard(card, slotID);
            if (player.getDevelopmentCardNumber() == 7)
                Game.getInstance().setEnded();
        } else {
            //TODO esplodi
        }




        /* old version
        int totalAmountToRemove;
        if (card.buy(player,this.slotID)) {
            for (Requirement requirement : card.getRequirements()) {
                RequirementResource requirementResource = (RequirementResource) requirement;
                totalAmountToRemove = requirementResource.getQuantity();
                if (player.hasDiscountForResource(requirementResource.getResourceType())) {
                    totalAmountToRemove += player.applyDiscount(requirementResource.getResourceType());
                }
                if (totalAmountToRemove != 0)
                    if (!RemoveResources.removeResources(totalAmountToRemove, requirementResource.getResourceType(), player, inputFromWarehouse, inputFromStrongbox)) {
                        //TODO esplodi
                    }
            }
            Game.getInstance().removeDevelopmentCard(card);
            if (player.getDevelopmentCardNumber() == 7)
                Game.getInstance().setEnded();
        }

         */
    }
    @Override
    public boolean isUserInputCorrect(Player player){
        if(!card.isBuyable(player,this.slotID)){
            return false;
        }
        for (Requirement requirement : card.getRequirements()) {
            RequirementResource requirementResource = (RequirementResource) requirement;
            ResourceType inputResource = requirementResource.getResourceType();
            int totalAmountToRemove = requirementResource.getQuantity();
            if (player.hasDiscountForResource(inputResource)) {
                totalAmountToRemove += player.applyDiscount(inputResource);
            }
            if(!RemoveResources.isInputQuantityRight(inputResource, totalAmountToRemove, inputFromWarehouse, inputFromStrongbox)){
                return false;
            }
            if(totalAmountToRemove > 0) {
                if (!RemoveResources.arePlacesRight(inputResource, player, inputFromWarehouse, inputFromStrongbox)) {
                    return false;
                }
            }
        }
        return true;
    }
}
