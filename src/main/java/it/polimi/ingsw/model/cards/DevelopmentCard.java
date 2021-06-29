package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Collection;

public class DevelopmentCard extends Eligible {
    private final DevelopmentColor color;
    private final int level;
    private final TradingRule tradingRule;
    private final String path;

    /**
     *
     * @param color: used to specify the color of the DevelopmentCard
     * @param level: used to specify the level of the DevelopmentCard
     * @param victoryPoints: used to specify the victory points of the DevelopmentCard
     * @param tradingRule: used to specify the the rule to trade
     */
    public DevelopmentCard(Collection<Requirement> requirements, DevelopmentColor color, int level, int victoryPoints, TradingRule tradingRule,String path) {
        super(requirements,victoryPoints);
        this.color = color;
        this.level = level;
        this.tradingRule = tradingRule;
        this.path = path;
    }

    public int getVictoryPoints(){
        return super.getVictoryPoints();
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

    /**
     * Check if a player can buy a DevelopmentCard
     * @param player: used to specify the player that wants buy a DevelopmentCard
     */
    public boolean buy(Player player, int slotID)  {
        return isEligible(player) && player.addDevelopmentCard(this,slotID);
    }

    public boolean isBuyable(Player player, int slotID){
        return isEligible(player) && player.canAddDevelopmentCard(this, slotID);
    }
    @Override
    public String toString(){
        return "DEVELOPMENT CARD " + color.convertColor() + " of level " + level + " (" + victoryPoints + " victory pts)" + "\n" +
                "Requires " + requirements.toString() + " to buy it\n" +
                tradingRule.toString() + "\n";
    }

    public String getPath(){
        return path;
    }

    public static String getBackPath(DevelopmentColor color, int level){
        return "GUIResources/Cards/DevelopmentCards/"+color+"/"+color+"Back"+level+".png";
    }
}
