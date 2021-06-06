package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Collection;


public class AdditionalTradingRule extends LeaderCard {
    private final TradingRule additionalTradingRule;

    /**
     * Adds an additional trading rule to a player when activated
     * @param victoryPoints the victory points that a trading rule gives
     * @param requirements the resources needed to activate the trading rule
     * @param additionalTradingRule the new trading rule activated
     */
    public AdditionalTradingRule(int victoryPoints, Collection<Requirement> requirements, TradingRule additionalTradingRule) {
        super(requirements, victoryPoints);
        this.additionalTradingRule = additionalTradingRule;
    }

    public TradingRule getAdditionalTradingRule() { return additionalTradingRule; }

    /**
     * Checks if a player has the requirements to activate an additional TradingRule
     * @param player the player on which the card is activated
     * @throws NoEligiblePlayerException catch if the player has not the right requirements to active the card
     */
    @Override
    public boolean activate(Player player) throws NoEligiblePlayerException{
        if (super.activate(player)) {
            player.addAdditionalRule(additionalTradingRule);
            player.addPersonalVictoryPoints(additionalTradingRule.getVictoryPoints());
            return true;
        }
        return false;
    }


    @Override
    public String toString(){
        return "\n" + requirements.toString() + "\nVictory Points: " + victoryPoints +
                "" + additionalTradingRule.toString();
    }
}
