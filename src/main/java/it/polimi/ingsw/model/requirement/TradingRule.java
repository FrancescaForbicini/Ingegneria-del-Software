package it.polimi.ingsw.model.requirement;

import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Map;

public class TradingRule {
    private Map<ResourceType, Integer> input;
    private Map<ResourceType, Integer> output;
    private int faithPoints;

    /**
     * Adds trading rule to the player
     * @param input: resource that a player has and wants to change
     * @param output: resource that a player has by changing an other resource
     */
    public TradingRule(Map<ResourceType, Integer> input, Map<ResourceType, Integer> output,int faithPoints) {
        this.input = input;
        this.output = output;
        this.faithPoints = faithPoints;
    }

    public Map<ResourceType, Integer> getInput() { return input; }
    public Map<ResourceType, Integer> getOutput() {
        return output;
    }
    public int getFaithPoints(){return faithPoints;}

    /**
     * Checks if the trading rule can be activated
     * @param player the player that wants use the trading rule
     * @return true if the player can use the trading rule, false if not
     */
    public boolean isUsable(Player player){
        for (ResourceType resourceType: input.keySet()){
            if (player.getResourceAmount(resourceType) < input.get(resourceType))
                return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "\nResources required: " + printMap(input) + "\nResource get: " +printMap(output) +"\nVictory Points: " +faithPoints;
    }

    private String printMap(Map<ResourceType,Integer> map) {
        StringBuilder print = new StringBuilder();
        for (ResourceType resourceType : map.keySet())
            print.append(resourceType.convertColor()).append(": ").append(map.get(resourceType)).append(" ");
        return print.toString();
    }
}
