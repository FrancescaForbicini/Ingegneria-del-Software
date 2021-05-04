package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.Eligible;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Collection;

public abstract class LeaderCardStrategy extends Eligible {
    /**
     * Initializes the resource and the victoryPoint of a LeaderCard
     * @param victoryPoints the victory points of the card
     * @param requirements the requirements to activate or buy a card
     */
    public LeaderCardStrategy(int victoryPoints, Collection<Requirement> requirements) {
        super(requirements,victoryPoints);
    }

    public int getVictoryPoints(){
        return super.getVictoryPoints();
    }

    /**
     * Activates the card
     * @param player the player on which the card is activated
     * @throws NoEligiblePlayerException if a player doesn't have the requirements to activate a LeaderCardStrategy
     */
    public void activate(Player player) throws NoEligiblePlayerException{
        if (isEligible(player))
            player.addPersonalVictoryPoints(victoryPoints);
        else
            throw new NoEligiblePlayerException();
    }
}
