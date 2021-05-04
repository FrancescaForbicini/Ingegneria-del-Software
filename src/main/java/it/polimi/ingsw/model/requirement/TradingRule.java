package it.polimi.ingsw.model.requirement;

import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Map;

public class TradingRule {
    private int victoryPoints;
    private Map<ResourceType, Integer> input;
    private Map<ResourceType, Integer> output;

    /**
     * Adds trading rule to the player
     * @param victoryPoints: the victory points that the card gives to the player
     * @param input: resource that a player has and wants to change
     * @param output: resource that a player has by changing an other resource
     */
    public TradingRule(int victoryPoints, Map<ResourceType, Integer> input, Map<ResourceType, Integer> output) {
        this.victoryPoints = victoryPoints;
        this.input = input;
        this.output = output;
    }

    public int getVictoryPoints(){return victoryPoints;}
    public Map<ResourceType, Integer> getInput() { return input; }
    public Map<ResourceType, Integer> getOutput() {
        return output;
    }

    /**
     * Checks if the trading rule can be activated
     * @param player the player that wants use the trading rule
     * @return true if the player can use the trading rule, false if not
     */
    public boolean isUsable(Player player){
        for (ResourceType resourceType: input.keySet()){
            if (player.getResourceAmount(resourceType) != input.get(resourceType))
                return false;
        }
        return true;
    }
}
