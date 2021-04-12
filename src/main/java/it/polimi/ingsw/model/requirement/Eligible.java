package it.polimi.ingsw.model.requirement;

import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Collection;

/**
 * An entity that has some requirements
 */
public abstract class Eligible {
    protected Collection<Requirement> requirements;
    /**
     *
     * @param requirements resources that a player has to have in order to buy or activate a card
     */
    public Eligible(Collection<Requirement> requirements) {
        this.requirements = requirements;
    }

    public Collection<Requirement> getRequirements(){
        return this.requirements;
    }

    /**
     * Checks if a player has the requirements to make a particular action
     *
     * @param player used to specify the player
     * @return true if the player has the requirements, false otherwise
     */
    protected boolean isEligible(Player player){
        return requirements.stream().allMatch(requirement -> requirement.isSatisfied(player));
    }
}
