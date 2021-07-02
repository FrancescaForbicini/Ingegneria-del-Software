package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Collection;

/**
 * Represents a LeaderCard whose power is to give an additional TradingRule to the player, when the card is activated
 * The TraddingRule can be used with the same rules of the ones given by the DevelopmentCards
 */
public class AdditionalTradingRule extends LeaderCard {
    private final TradingRule additionalTradingRule;
    private final String path;

    /**
     * Adds an additional trading rule to a player when activated
     *
     * @param victoryPoints the victory points that a trading rule gives
     * @param requirements the resources needed to activate the trading rule
     * @param additionalTradingRule the new trading rule activated
     */
    public AdditionalTradingRule(int victoryPoints, Collection<Requirement> requirements, TradingRule additionalTradingRule,String path) {
        super(requirements, victoryPoints);
        this.additionalTradingRule = additionalTradingRule;
        this.path = path;
    }

    public TradingRule getAdditionalTradingRule() { return additionalTradingRule; }

    /**
     * Checks if a player has the requirements to activate an additional TradingRule
     *
     * @param player the player on which the card is activated
     */
    @Override
    public boolean activate(Player player) {
        if (super.activate(player)) {
            player.addAdditionalRule(additionalTradingRule);
            player.addPersonalVictoryPoints(victoryPoints);
            return true;
        }
        return false;
    }


    @Override
    public String toString(){
        return  super.toString() + "Additional Trading Rule: " + additionalTradingRule.toString();
    }

    @Override
    public String getPath(){
        return path;
    }
}
