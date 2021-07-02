package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.RequirementResource;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Map;

/**
 * Buys 1 DevelopmentCard and adds it a player's slot
 */
public class BuyDevelopmentCard implements TurnAction, RequireInputToRemove {
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
     * {@inheritDoc}
     *
     * Buys a card if the player has the right requirements
     *
     * @param player the player that wants buy the development card
     */
    @Override
    public void play(Player player) {
        if(isUserInputCorrect(player)) {
            for (Requirement requirement : card.getRequirements()) {
                RequirementResource requirementResource = (RequirementResource) requirement;
                RequireInputToRemove.removeResources(requirementResource.getResourceType(), player, inputFromWarehouse, inputFromStrongbox);
            }
            Game.getInstance().removeDevelopmentCard(card);
            player.addDevelopmentCard(card, slotID);
            player.addPersonalVictoryPoints(card.getVictoryPoints());
            if (player.getDevelopmentCardNumber() == 7)
                Game.getInstance().setEnded();
        } else {
            //if the game is corrupted, the game will end
            Game.getInstance().setEnded();
            Game.getInstance().setCorrupted();
        }
    }

    /**
     * {@inheritDoc}
     *
     * Checks if the the player can buy the chosen card and if can place it in one slot
     *
     * @param player player that has to remove the resources
     * @return true iff the card can be bought and the player has at least one available slot to contain it
     */
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
            if(!RequireInputToRemove.isInputQuantityRight(inputResource, totalAmountToRemove, inputFromWarehouse, inputFromStrongbox)){
                return false;
            }
            if(totalAmountToRemove > 0) {
                if (!RequireInputToRemove.arePlacesRight(inputResource, player, inputFromWarehouse, inputFromStrongbox)) {
                    return false;
                }
            }
        }
        return true;
    }
}
