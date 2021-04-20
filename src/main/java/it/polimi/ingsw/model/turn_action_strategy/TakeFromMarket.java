package it.polimi.ingsw.model.turn_action_strategy;

import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.MarbleType;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Map;

public class TakeFromMarket implements TurnActionStrategy{
    @Override
    public void play(Player player) {
        int num=5;
        //choose line or column and the number
        Map<MarbleType,Integer> marbles= Market.getInstance().getMarblesFromLine("row",num);
        convertMarble(player,marbles);
    }

    /**
     * Convert the marble to the resource
     * @param player player that wants to convert a marble to the correspondent resource
     * @param marbles the marbles that the player has taken from the market
     */
    private void convertMarble(Player player,Map<MarbleType,Integer> marbles ){
        for (MarbleType marbleType: marbles.keySet()){
            if (MarbleType.White.equals(marbleType)) {
                //TODO number of whiteMarble
                player.addWhiteMarbleResource(Marble.conversion(marbleType));
                continue;
            }
            if (marbleType.equals(MarbleType.Red)) {
                FaithTrack.getInstance().move(player, marbles.get(marbleType));
                continue;
            }
            player.getPersonalBoard().addResourceToWarehouse(Marble.conversion(marbleType),marbles.get(marbleType));
        }
    }
}
