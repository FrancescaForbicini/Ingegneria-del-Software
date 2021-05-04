package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;

import java.util.Collection;



public class AdditionalDepot extends LeaderCardStrategy{
    private final  WarehouseDepot additionalDepot;

    /**
     * Attaches another `WarehouseDepot` to a player when activated
     * @param victoryPoints the victory points that the card gives to the player
     * @param requirements the resource needed to activate the leader card
     * @param additionalDepot the depot to attach to the player who activates the card
     */
    public AdditionalDepot(int victoryPoints, Collection<Requirement> requirements, WarehouseDepot additionalDepot)
    {
        super(victoryPoints, requirements);
        this.additionalDepot = additionalDepot;
    }

    public WarehouseDepot getAdditionalDepot() {
        return additionalDepot;
    }

    /**
     * Checks and attach the depot on the given player
     * @param player the player on which the card is activated
     * @throws NoEligiblePlayerException catch if the player has not the right requirements to active the card
     */
    @Override
    public void activate(Player player) throws NoEligiblePlayerException{
        super.activate(player);
        player.addAdditionalDepot(additionalDepot);
    }
}


