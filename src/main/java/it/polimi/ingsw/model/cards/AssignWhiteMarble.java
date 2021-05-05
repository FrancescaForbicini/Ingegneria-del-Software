package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Collection;

public class AssignWhiteMarble extends LeaderCardStrategy{
    private ResourceType resourceType;

    /**
     * Adds a meaning for a white marble from the market
     * @param victoryPoints the victory points that the card gives to the player
     * @param resourceType the resource in which the white marbles changes
     * @param requirements the resource needed to activate the card
     */
    public AssignWhiteMarble(int victoryPoints, ResourceType resourceType, Collection<Requirement> requirements) {
        super(victoryPoints, requirements);
        this.resourceType = resourceType;
    }

    public ResourceType getResourceType(){
        return resourceType;
    }
    /**
     * Checks if a player has the requirements to activate the ability to set a white marble in a different color
     * @param player the player on which the card is activated
     * @throws NoEligiblePlayerException catch if the player has not the right requirements to active the card
     */
    @Override
    public void activate(Player player) throws NoEligiblePlayerException {
        super.activate(player);
        player.addActiveWhiteConversion(this.getResourceType());
    }
}