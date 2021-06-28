package it.polimi.ingsw.model.requirement;

import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Map;

/**
 * Trading rule of a card
 */
public class TradingRule {
    private final Map<ResourceType, Integer> input;
    private final Map<ResourceType, Integer> output;
    private final int faithPoints;

    /**
     * Adds trading rule to the player
     *
     * @param input: resource that a player has and wants to change
     * @param output: resource that a player has by changing an other resource
     */
    public TradingRule(Map<ResourceType, Integer> input, Map<ResourceType, Integer> output,int faitPoints) {
        this.input = input;
        this.output = output;
        this.faithPoints = faitPoints;
    }

    public Map<ResourceType, Integer> getInput() { return input; }
    public Map<ResourceType, Integer> getOutput() {
        return output;
    }
    public int getFaithPoints(){return faithPoints;}

    /**
     * Checks if the trading rule can be activated
     *
     * @param player the player that wants use the trading rule
     * @return true if the player can use the trading rule, false if not
     */
    public boolean isUsable(Player player){
        for (ResourceType resourceType: input.keySet()){
            if (player.getResourceQuantity(resourceType) < input.get(resourceType))
                return false;
        }
        return true;
    }

    /**
     * Prints the trading rule
     * @return string to show the trading rule
     */
    @Override
    public String toString() {
        return "Trading: IN " + printInput(input) + " --->   OUT " + printOutput(output) + "\n";
    }

    /**
     * Prints the resources required to use the trading rule
     *
     * @param map mapping of resources needed to use the trading rule
     * @return string to show the resources required
     */
    private String printInput(Map<ResourceType,Integer> map){
        StringBuilder print = new StringBuilder();
        for (ResourceType resourceType : map.keySet())
            print.append(map.get(resourceType)).append(" ").append(resourceType.convertColor()).append("  ");
        return print.toString();
    }

    /**
     * Prints the resources obtained from the trading rule
     *
     * @param map mapping of resources obtained from the trading rule
     * @return string to show the resources obtained
     */
    private String printOutput(Map<ResourceType,Integer> map) {
        StringBuilder print = new StringBuilder();
        if (!map.isEmpty())
            for (ResourceType resourceType : map.keySet())
                print.append(map.get(resourceType)).append(" ").append(resourceType.convertColor()).append("  ");
        if (map.isEmpty() || faithPoints > 0)
            print.append("FaithPoints: ").append(faithPoints).append(" ");
        return print.toString();
    }
}
