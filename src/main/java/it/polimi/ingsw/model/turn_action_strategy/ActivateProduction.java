package it.polimi.ingsw.model.turn_action_strategy;

import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.turn_taker.Player;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ActivateProduction implements TurnActionStrategy{
    @Override
    public void play (Player player){
        Collection<TradingRule> tradingRules = usableTradingRules(player);
        // TODO pickTradingRules()
        Map<ResourceType,Integer> totalInput= getTotal(tradingRules);
        // TODO getInput() --> ask to the player the resource and from where
    }

    /**
     * Get all the trading rules that a player can use
     * @param player
     * @return
     */
    private Collection <TradingRule> usableTradingRules(Player player){
        return player.getPersonalBoard().getActiveTradingRules().stream().filter(tradingRule -> tradingRule.isUsable(player)).collect(Collectors.toList());
    }

    /**
     * Collect all the input of the trading rules
     * @param tradingRules
     * @return
     */
    private Map<ResourceType,Integer> getTotal(Collection<TradingRule> tradingRules){
        Map<ResourceType,Integer> total = new HashMap<>();
        tradingRules.forEach(tradingRule -> total.putAll(tradingRule.getInput()));
        return total;
    }
}
