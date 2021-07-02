package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.view.gui.HasPath;

import java.util.Collection;

/**
 * Represents a DevelopmentCard of the actual game
 */
public class DevelopmentCard extends Eligible implements HasPath {
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
     * Add this to the player's slot
     *
     * @param player used to specify the player that wants buy a DevelopmentCard
     * @param slotID slot where the card will be put
     * @return true iff the operation ends
     */
    public boolean buy(Player player, int slotID)  {
        return isEligible(player) && player.addDevelopmentCard(this,slotID);
    }

    /**
     * Checks if this can be bought by the player and can be added to the given slot
     *
     * @param player whom to add this
     * @param slotID where to add this
     * @return true iff both conditions are verified
     */
    public boolean isBuyable(Player player, int slotID){
        return isEligible(player) && player.canAddDevelopmentCard(this, slotID);
    }
    @Override
    public String toString(){
        return "DEVELOPMENT CARD " + color.convertColor() + " of level " + level + " (" + victoryPoints + " victory pts)" + "\n" +
                "Requires " + requirements.toString() + " to buy it\n" +
                tradingRule.toString() + "\n";
    }

    @Override
    public String getPath(){
        return path;
    }

    /**
     * Gets the path for the image of the back of a card specified by color and level
     *
     * @param color of the card
     * @param level of the card
     * @return a String containing the path to the graphic resource
     */
    public static String getBackPath(DevelopmentColor color, int level){
        return "GUIResources/Cards/DevelopmentCards/"+color+"/"+color+"Back"+level+".png";
    }
}
