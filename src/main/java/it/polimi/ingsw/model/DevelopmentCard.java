package it.polimi.ingsw.model;

import java.util.Collection;

public class DevelopmentCard extends Eligible {

    private  DevelopmentColor color;
    private int level;
    private int victoryPoints;
    private TradingRule tradingRule;

    /** Initializes the color, the level and the victoryPoint of a DevelopmentCard
     *
     * @param requirements: used to specify how and which resources requires in order to buy a card
     * @param color: used to specify the color of the DevelopmentCard
     * @param level: used to specify the level of the DevelopmentCard
     * @param victoryPoints: used to specify the victory points of the DevelopmentCard
     * @param tradingRule: used to specify the the rule to trade
     */

    public DevelopmentCard(Collection<Requirement> requirements, DevelopmentColor color, int level, int victoryPoints, TradingRule tradingRule) {
        super(requirements);
        this.color = color;
        this.level = level;
        this.victoryPoints = victoryPoints;
        this.tradingRule = tradingRule;
    }

    public int getVictoryPoints(){
        return victoryPoints;
    }
    public int getLevel(){
        return level;
    }
    public DevelopmentColor getColor(){
        return color;
    }
    public TradingRule getTradingRule(){
        return tradingRule;
    }

    /**Check if a player can buy a DevelopmentCard
     *
     * @param player: used to specify the player that wants buy a DevelopmentCard
     * @return true if a player can buy a card, false if not.
     */
    public boolean buy(Player player){
        if (!isEligible(player))
            return false;
        player.addDevelopmentCard();
        return true;
    }

}
