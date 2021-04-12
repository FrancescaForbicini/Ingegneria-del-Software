package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;

import java.util.Collection;

/**
 * Attaches another `WarehouseDepot` to a player when activated
 */
public class AdditionalDepot extends LeaderCardStrategy{
    private WarehouseDepot additionalDepot;
    /**
     * @param additionalDepot the depot to attach to the player who activates the card
     */
    public AdditionalDepot(int victoryPoints, ResourceType resourceType, Collection<Requirement> requirement, WarehouseDepot additionalDepot)
    {
        super(victoryPoints, resourceType, requirement);
        this.additionalDepot = new WarehouseDepot(2, resourceType, true); // TODO hardcoded?
    }

    public WarehouseDepot getAdditionalDepot() {
        return additionalDepot;
    }

    /**
     * Checks and attach the depot on the given player
     */
    @Override
    public void activate(Player player) {
        if (isEligible(player))
            player.addAdditionalDepot(additionalDepot);
    }
}


