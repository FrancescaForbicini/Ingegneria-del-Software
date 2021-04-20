package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.Eligible;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Collection;

public abstract class LeaderCardStrategy extends Eligible {
    private final int victoryPoints;
    protected final ResourceType resourceType;

    /**
     * Initializes the resource and the victoryPoint of a LeaderCard
     *
     * @param victoryPoints the victory points of the card
     * @param resourceType the resource type associated with the card
     * @param requirements the requirements to activate or buy a card
     */
    public LeaderCardStrategy(int victoryPoints, ResourceType resourceType, Collection<Requirement> requirements) {
        super(requirements);
        this.victoryPoints = victoryPoints;
        this.resourceType = resourceType;
    }

    public int getVictoryPoints(){
        return victoryPoints;
    }
    public ResourceType getResourceType() {
        return resourceType;
    }

    /**
     * Activates the card
     * @param player the player on which the card is activated
     * @throws NoEligiblePlayerException if a player doesn't have the requirements to activate a LeaderCardStrategy
     */
    public void activate(Player player) throws NoEligiblePlayerException{
        if (!isEligible(player))
            throw new NoEligiblePlayerException();
        player.addPersonalVictoryPoints(victoryPoints);
    }
}
