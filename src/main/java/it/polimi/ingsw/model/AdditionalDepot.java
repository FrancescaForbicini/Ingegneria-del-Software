package it.polimi.ingsw.model;

import java.util.Collection;
import java.util.concurrent.ExecutionException;


public class AdditionalDepot extends LeaderCardStrategy{
    private WarehouseDepot additionalDepot;
    /**
     * @param victoryPoints : used to specify the victory points
     * @param resourceType : used to specify the resources that are needed to activate the card
     * @param requirement: used to verify if a player has the right resources to use the card
     */
    public AdditionalDepot(int victoryPoints, ResourceType resourceType, Collection<Requirement> requirement,WareHouseDepot additioanlDepot)
    {
        super(victoryPoints, resourceType, requirement);
        this.additionalDepot =additionalDepot;
    }

    public WarehouseDepot getAdditionalDepot() {
        return additionalDepot;
    }

    /**Check if a player has the requirements to activate the ability to add a depot in his warehouse
     *
     * @param player: used to specify the player that wants activate a card
     */
    @Override
    public void activate(Player player) {
        if (isEligible(player)){
            player.addDepot(additionalDepot);
        }
    }
}


