package it.polimi.ingsw.model.turn_action_strategy;

import it.polimi.ingsw.model.board.NotEnoughSpaceException;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.MarbleType;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class TakeFromMarket implements TurnActionStrategy{
    /*
    @Override
    public void play(Player player) {
        int discarded = 0;
        //choose line or column and the number
        ArrayList<MarbleType> marbles = Market.getInstance().getMarblesFromLine(rc,num);
        ArrayList<ResourceType> resources = convertMarble(player,marbles);
        //TODO: sistemare meccanismo di comunicazione con l'utente
        //player chooses resource for white marbles (now converted in ResourceType.Any)
        //player should be able to choose for each resource where to put it
        //return resources?
    }

     */
    public ArrayList<ResourceType> getResourceFromMarket(Player player, String rc, int num){
        ArrayList<MarbleType> marbles = Market.getInstance().getMarblesFromLine(rc,num);
        return convertMarble(player,marbles);
    }

    /**
     * Convert the marble to the resource
     * @param player player that wants to convert a marble to the correspondent resource
     * @param marbles the marbles that the player has taken from the market
     */
    private ArrayList<ResourceType> convertMarble(Player player, Collection<MarbleType> marbles ) {
        ArrayList<ResourceType> resources = new ArrayList<>();
        for (MarbleType marbleType : marbles) {
            if (marbleType.equals(MarbleType.Red)) {
                //TODO: sistemare ThreadLocal Singletons
                FaithTrack.getInstance().move(player, 1);
            } else {
                if(marbleType.equals(MarbleType.White)) {
                    int activeWhite = player.getAmountActiveWhiteConversions();
                    if (activeWhite == 1) {
                        resources.add(player.getActiveWhiteConversions().get(0));
                    } else if (activeWhite > 1) {
                        resources.add(conversion(marbleType));
                    }
                }
                else{
                    resources.add(conversion(marbleType));
                }
            }
        }
        return resources;
    }

    /**
     * Convert a marble to the own resource
     * @param marbleType type to be converted
     * @return the resource type that corresponds to the marble
     */
    private ResourceType conversion (MarbleType marbleType){
        switch(marbleType){
            case White:
                return ResourceType.Any;
            case Blue:
                return ResourceType.Shields;
            case Grey:
                return ResourceType.Stones;
            case Yellow:
                return ResourceType.Coins;
            case Purple:
                return ResourceType.Servants;
        }
        return null;
    }

    @Override
    public void play(Player player) {
        //TODO
    }
}
