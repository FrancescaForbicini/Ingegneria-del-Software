package it.polimi.ingsw.model;

import java.util.Collection;

public abstract class Eligible extends Requirement{
    protected Collection<Requirement> requirements;

    /** Controls if a player has the requirement to buy or activate a card
     *
     * @param requirements: resource that a player has to have in order to buy or activate a card
     */
    public Eligible(Collection<Requirement> requirements) {
        this.requirements = requirements;
    }
    public Collection<Requirement> getRequirements(){
        return this.requirements;
    }
    protected boolean isEligible(){
        // TODO Player
        return false;
    }
}
