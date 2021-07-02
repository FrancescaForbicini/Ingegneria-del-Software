package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Collection;
import java.util.Objects;

/**
 * An entity that has some requirements
 */
public abstract class Eligible {
    protected Collection<Requirement> requirements;
    protected int victoryPoints;

    /**
     * Builds a class to check if the requirements are satisfied
     * @param requirements resources that a player has to have in order to buy or activate a card
     */
    public Eligible(Collection<Requirement> requirements, int victoryPoints) {
        this.requirements = requirements;
        this.victoryPoints = victoryPoints;
    }

    public Collection<Requirement> getRequirements(){
        return this.requirements;
    }

    public int getVictoryPoints(){
        return victoryPoints;
    }

    /**
     * Checks if a player has the requirements to make a particular action
     * @param player used to specify the player
     * @return true if the player has the requirements, false otherwise
     */
    public boolean isEligible(Player player){
        return requirements.stream().allMatch(requirement -> requirement.isSatisfied(player));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Eligible e = (Eligible)o;
        return Objects.equals(requirements, e.getRequirements()) && victoryPoints == e.getVictoryPoints();
    }
}
