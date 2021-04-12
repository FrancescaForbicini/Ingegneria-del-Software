package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Collection;

/**
 * Allows a player to buy a leader card with a discount
 */
public class Discount extends LeaderCardStrategy{
    public Discount(int victoryPoints, ResourceType resourceType, Collection<Requirement> requirement) {
        super(victoryPoints, resourceType, requirement);
    }
    /**
     * Checks if a player has the requirements to activate a discount
     *
     * @param player specifies the player that wants activate a card
     */
    @Override
    public void activate(Player player) {
        if (isEligible(player)){
            player.addDiscount(this.getResourceType());
        }
    }
}
