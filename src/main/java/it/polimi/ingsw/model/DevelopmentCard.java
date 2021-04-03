package it.polimi.ingsw.model;

import java.util.Collection;

public class DevelopmentCard extends Requirement {
    private  DevelopmentColor color;
    private int level;
    private int victoryPoints;
    private TradingRule tradingRule;


    /** Initializes the color, the level and the victoryPoint of a DevelopmentCard
     *
     * @param color: used to specify the color of the DevelopmentCard
     * @param level: used to specify the level of the DevelopmentCard
     * @param victoryPoints: used to specify the victory points of the DevelopmentCard
     * @param tradingRule: used to specify the the rule to trade
     */

    public DevelopmentCard(DevelopmentColor color, int level, int victoryPoints, TradingRule tradingRule) {
        this.color = color;
        this.level = level;
        this.victoryPoints = victoryPoints;
        this.tradingRule = tradingRule;
    }

    @Override
    public boolean isSatisfied() {
        return super.isSatisfied();
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

    public boolean buy(){
        // TODO Player
        return false;
    }
    public TradingRule getTradingRule(){
        return tradingRule;
    }
}
