package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Collection;

public abstract class LeaderCard extends Eligible {
    /**
     * Initializes the resource and the victoryPoint of a LeaderCard
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
     * @param player the player on which the card is activated
     */
    public boolean activate(Player player) throws NoEligiblePlayerException {
        return isEligible(player);
    }

    @Override
    public String toString() {
        return "\nLEADER CARD (" + victoryPoints + " victory pts)" +
                "\nRequires " + requirements.toString();
    }
}
