package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.RequirementResource;
import it.polimi.ingsw.model.turn_taker.Player;

public class BuyDevelopmentCard implements TurnAction{
    private final DevelopmentCard card;
    private final int slotID;

    public BuyDevelopmentCard(DevelopmentCard card, int slotID){
        this.card = card;
        this.slotID = slotID;
    }
    /**
     * Buys a card if the player has the right requirements
     * @param player the player that wants buy the development card
     */
    @Override
    public void play(Player player) {
        int depot;
        //if (card.buy(player,this.slotID)) {
        for (Requirement requirement : card.getRequirements()) {
            RequirementResource requirementResource = (RequirementResource) requirement;
            depot = player.getWarehouse().findDepotsByType(requirementResource.getResourceType()).getDepotID();
            player.getWarehouse().removeResource(requirementResource.getQuantity(), depot);
        }
        Game.getInstance().removeDevelopmentCard(card);
        //}
    }


}
