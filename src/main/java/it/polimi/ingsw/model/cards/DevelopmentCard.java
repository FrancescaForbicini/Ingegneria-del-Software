package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.requirement.Eligible;
import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Collection;

public class DevelopmentCard extends Eligible {
    private  DevelopmentColor color;
    private int level;
    private int victoryPoints;
    private TradingRule tradingRule;

    /**
     *
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

    /**
     * Check if a player can buy a DevelopmentCard
     * @param player: used to specify the player that wants buy a DevelopmentCard
     */
    public void buy(Player player) throws NoEligiblePlayerException {
        if (isEligible(player))
            player.addDevelopmentCard(this);
        else
            throw new NoEligiblePlayerException();
    }

}
