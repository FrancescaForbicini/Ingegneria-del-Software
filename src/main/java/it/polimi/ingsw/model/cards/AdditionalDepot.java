package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Collection;



public class AdditionalDepot extends LeaderCard {
    private final ResourceType depotResourceType;
    private final int depotLevel;

    /**
     * Attaches another `WarehouseDepot` to a player when activated
     * @param victoryPoints the victory points that the card gives to the player
     * @param requirements the resource needed to activate the leader card
     */
    public AdditionalDepot(Collection<Requirement> requirements, int victoryPoints, ResourceType depotResourceType, int depotLevel)
    {
        super(requirements, victoryPoints);
        this.depotResourceType = depotResourceType;
        this.depotLevel = depotLevel;
    }

    /**
     * Checks and attach the depot on the given player
     * @param player the player on which the card is activated
     * @throws NoEligiblePlayerException catch if the player has not the right requirements to active the card
     */
    @Override
    public void activate(Player player) throws NoEligiblePlayerException{
        super.activate(player);
        player.addAdditionalDepot(this.depotResourceType, this.depotLevel);
    }
}


