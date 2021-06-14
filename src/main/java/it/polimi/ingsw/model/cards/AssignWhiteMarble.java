package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Collection;

public class AssignWhiteMarble extends LeaderCard {
    private final ResourceType resourceType;

    /**
     * Adds a meaning for a white marble from the market
     * @param victoryPoints the victory points that the card gives to the player
     * @param resourceType the resource in which the white marbles changes
     * @param requirements the resource needed to activate the card
     */
    public AssignWhiteMarble(int victoryPoints, ResourceType resourceType, Collection<Requirement> requirements) {
        super(requirements, victoryPoints);
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
    public boolean activate(Player player) throws NoEligiblePlayerException {
        if (super.activate(player)) {
            player.addActiveWhiteConversion(this.getResourceType());
            player.addPersonalVictoryPoints(victoryPoints);
            return true;
        }
        return false;
    }
    @Override
    public String toString(){
        return super.toString() + "\nWhite Marble converted to " + resourceType.convertColor();
    }
}
