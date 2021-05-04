package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Collection;


public class AdditionalTradingRule extends LeaderCardStrategy {
    private TradingRule additionalTradingRule;

    /**
     * Adds an additional trading rule to a player when activated
     * @param victoryPoints the victory points that a trading rule gives
     * @param requirements the resources needed to activate the trading rule
     * @param additionalTradingRule the new trading rule activated
     */
    public AdditionalTradingRule(int victoryPoints, Collection<Requirement> requirements, TradingRule additionalTradingRule) {
        super(victoryPoints,requirements);
        this.additionalTradingRule = additionalTradingRule;
    }

    public TradingRule getAdditionalTradingRule() { return additionalTradingRule; }

    /**
     * Checks if a player has the requirements to activate an additional TradingRule
     * @param player the player on which the card is activated
     * @throws NoEligiblePlayerException catch if the player has not the right requirements to active the card
     */
    @Override
    public void activate(Player player) throws NoEligiblePlayerException{
        super.activate(player);
        player.addAdditionalRule(additionalTradingRule);
    }
}
