package it.polimi.ingsw.model.requirement;

import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Map;

public class TradingRule {
    private Map<ResourceType, Integer> input;
    private Map<ResourceType, Integer> output;
    private int victoryPoints;

    /**
     * Adds trading rule to the player
     * @param input: resource that a player has and wants to change
     * @param output: resource that a player has by changing an other resource
     */
    public TradingRule(Map<ResourceType, Integer> input, Map<ResourceType, Integer> output,int victoryPoints) {
        this.input = input;
        this.output = output;
        this.victoryPoints = victoryPoints;
    }

    public Map<ResourceType, Integer> getInput() { return input; }
    public Map<ResourceType, Integer> getOutput() {
        return output;
    }
    public int getVictoryPoints(){return victoryPoints;}

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
        return "\n" + printInput(input) + " --->   " + printOutput(output);
        //return "\nResources required: " + printInput(input) + "\nResource get: " +printOutput(output) ;
    }

    private String printInput(Map<ResourceType,Integer> map){
        StringBuilder print = new StringBuilder();
        for (ResourceType resourceType : map.keySet())
            print.append(map.get(resourceType)).append(" ").append(resourceType.convertColor()).append("  ");
            //print.append(resourceType.convertColor()).append(": ").append(map.get(resourceType)).append(" ");
        return print.toString();
    }
    private String printOutput(Map<ResourceType,Integer> map) {
        StringBuilder print = new StringBuilder();
        if (!map.isEmpty())
            for (ResourceType resourceType : map.keySet())
                print.append(map.get(resourceType)).append(" ").append(resourceType.convertColor()).append("  ");
                //print.append(resourceType.convertColor()).append(": ").append(map.get(resourceType)).append(" ");
        if (map.isEmpty() || victoryPoints > 0)
            print.append("FaithPoints: ").append(victoryPoints).append(" ");
        return print.toString();
    }
}
