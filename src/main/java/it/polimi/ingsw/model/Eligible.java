package it.polimi.ingsw.model;

import java.util.Collection;

public abstract class Eligible {
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

    /**Check if a player has the requirements to make a particular action
     *
     * @param player: used to specify the player
     * @return true if the player has the requirements, false if not
     */
    protected boolean isEligible(Player player){
        for (Requirement r : requirements){
            if (!r.isSatisfied(player))
                return false;
        }
        return true;
    }
}
