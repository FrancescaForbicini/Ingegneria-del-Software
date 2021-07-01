package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Collection;


/**
 * Represents a LeaderCard whose power is to give an additional depot to the player, when the card is activated
 * The depot cannot contain resources different from its original one,
 * but can contain it even there is already a depot in the warehouse containing the same ResourceType
 */
public class AdditionalDepot extends LeaderCard {
    private final ResourceType depotResourceType;
    private final int depotLevel;
    private final String path;

    /**
     * Attaches another `WarehouseDepot` to a player when activated
     * @param victoryPoints the victory points that the card gives to the player
     * @param requirements the resource needed to activate the leader card
     */
    public AdditionalDepot(Collection<Requirement> requirements, int victoryPoints, ResourceType depotResourceType, int depotLevel,String path)
    {
        super(requirements, victoryPoints);
        this.depotResourceType = depotResourceType;
        this.depotLevel = depotLevel;
        this.path = path;
    }

    /**
     * Checks and attach the depot on the given player
     *
     * @param player the player on which the card is activated
     */
    @Override
    public boolean activate(Player player) {
        if (super.activate(player)) {
            player.addAdditionalDepot(this.depotResourceType, this.depotLevel);
            player.addPersonalVictoryPoints(victoryPoints);
            return true;
        }
        return false;
    }

    public ResourceType getDepotResourceType() {
        return depotResourceType;
    }

    public int getDepotLevel() {
        return depotLevel;
    }

    @Override
    public String toString(){
        return super.toString() + "Additional Depot: " + depotLevel + " " + depotResourceType.convertColor() + "\n";
    }
    @Override
    public String getPath(){
        return path;
    }
}


