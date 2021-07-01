package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.view.gui.HasPath;
import it.polimi.ingsw.view.gui.custom_gui.Modifiable;

import java.util.Collection;

/**
 * Abstraction to represent any type of LeaderCard
 */
public abstract class LeaderCard extends Eligible implements HasPath, Modifiable {
    /**
     * Initializes the resource and the victoryPoint of a LeaderCard
     *
     * @param victoryPoints the victory points of the card
     * @param requirements the requirements to activate or buy a card
     */
    public LeaderCard(Collection<Requirement> requirements, int victoryPoints) {
        super(requirements,victoryPoints);
    }

    public int getVictoryPoints(){
        return super.getVictoryPoints();
    }

    /**
     * Activates the card
     *
     * @param player the player on which the card is activated
     */
    public boolean activate(Player player) {
        return isEligible(player);
    }

    @Override
    public abstract String getPath();

    @Override
    public String toString() {
        return "LEADER CARD (" + victoryPoints + " victory pts)\n" +
                "Requires " + requirements.toString() + "\n";
    }
}
