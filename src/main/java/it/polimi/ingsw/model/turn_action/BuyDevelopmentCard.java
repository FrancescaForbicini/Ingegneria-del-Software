package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.RequirementResource;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Map;

public class BuyDevelopmentCard implements TurnAction{
    private final DevelopmentCard card;
    private final int slotID;
    private final Map<ResourceType,Integer> inputFromWarehouse;
    private final Map<ResourceType,Integer> inputFromStrongbox;
    public BuyDevelopmentCard(DevelopmentCard card, int slotID, Map<ResourceType,Integer> inputFromWarehouse, Map<ResourceType,Integer> inputFromStrongbox){
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
        int amount;
        if (card.buy(player,this.slotID)) {
            for (Requirement requirement : card.getRequirements()) {
                RequirementResource requirementResource = (RequirementResource) requirement;
                amount = requirementResource.getQuantity();
                if (player.isDiscount(requirementResource.getResourceType())) {
                    amount = amount -1;
                }
                if (amount != 0)
                    if (!RemoveResources.removeResources(amount,requirementResource.getResourceType(),player,inputFromWarehouse,inputFromStrongbox)) {
                        //TODO esplodi
                    }
            }
            Game.getInstance().removeDevelopmentCard(card);
            if (player.getDevelopmentCardNumber() == 7)
                Game.getInstance().setEnded();
        }
    }
}
