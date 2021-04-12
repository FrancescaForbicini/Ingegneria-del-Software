package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Collection;

/**
 * Adds a meaning for a white marble from the market
 */
public class AssignWhiteMarble extends LeaderCardStrategy{
    public AssignWhiteMarble(int victoryPoints, ResourceType resourceType, Collection<Requirement> requirement) {
        super(victoryPoints, resourceType, requirement);
    }
    /**Check if a player has the requirements to activate the ability to set a white marble in a different color
     *
     * @param player: used to specify the player that wants activate a card
     */
    @Override
    public void activate(Player player) {
        if (isEligible(player)){
            player.addWhiteMarbleResource(this.getResourceType());
        }
    }


}
