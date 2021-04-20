package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Collection;

/**
 * Adds an additional trading rule to a player when activated
 */
public class AdditionalTradingRule extends LeaderCardStrategy {
    private TradingRule additionalTradingRule;
    /**
     * @param additionalTradingRule: used to add a TradingRule to a player
     */
    public AdditionalTradingRule(int victoryPoints, ResourceType resourceType, Collection<Requirement> requirement, TradingRule additionalTradingRule) {
        super(victoryPoints, resourceType,requirement);
        this.additionalTradingRule = additionalTradingRule;
    }

    public TradingRule getAdditionalTradingRule() {
        return additionalTradingRule;
    }

    /**
     * Checks if a player has the requirements to activate an additional TradingRule
     */
    @Override
    public void activate(Player player) throws NoEligiblePlayerException{
        super.activate(player);
        player.addAdditionalRule(additionalTradingRule);
    }
}
