package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Collection;

public class DevelopmentCard extends Eligible {
    private  DevelopmentColor color;
    private int level;
    private TradingRule tradingRule;

    /**
     *
     * @param color: used to specify the color of the DevelopmentCard
     * @param level: used to specify the level of the DevelopmentCard
     * @param victoryPoints: used to specify the victory points of the DevelopmentCard
     * @param tradingRule: used to specify the the rule to trade
     */
    public DevelopmentCard(Collection<Requirement> requirements, DevelopmentColor color, int level, int victoryPoints, TradingRule tradingRule) {
        super(requirements,victoryPoints);
        this.color = color;
        this.level = level;
        this.tradingRule = tradingRule;
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
     * @throws NoEligiblePlayerException catch if the player has not the right requirements to active the card
     */
    public void buy(Player player, int slotID) throws NoEligiblePlayerException {
        if (isEligible(player))
            player.addDevelopmentCard(this,slotID);
        else {
            throw new NoEligiblePlayerException();
        }
    }
    @Override
    public String toString(){
        return "\nDevelopment Card:" + "\nResource required: " + requirements.toString() + "\nColor: " + color.convertColor() +
                "\nLevel: " + level + "\nVictory Points: " + victoryPoints +"\nTrading Rule: " + tradingRule.toString();
    }

}
