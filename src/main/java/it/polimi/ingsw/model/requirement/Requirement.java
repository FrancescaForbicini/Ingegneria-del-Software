package it.polimi.ingsw.model.requirement;

import it.polimi.ingsw.model.turn_taker.Player;

/**
 * An entity that a player can have to satisfy certain conditions
 */
public abstract class Requirement {
    /**
     * Checks if the conditions are verified
     *
     * @param player used to check if the requirement is satisfied
     * @return true if the player has the requirement, false otherwise
     */
    public abstract boolean isSatisfied(Player player);
    public abstract String toString();
}
