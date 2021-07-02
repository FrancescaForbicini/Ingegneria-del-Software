package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.turn_taker.Player;

/**
 * Sorts the warehouse
 */
public class SortWarehouse implements TurnAction {
    private final int depotID1;
    private final int depotID2;

    public SortWarehouse(int depotID1, int depotID2) {
        this.depotID1 = depotID1;
        this.depotID2 = depotID2;
    }

    /**
     * {@inheritDoc}
     *
     * Switches the resources in depotID1 to the depotID2 and vice-versa (if both non additional)
     * Moves resource only from depot1 to depot2 (if one of them is additional)
     *
     * @param player player that wants to switch the depots
     */
    @Override
    public void play(Player player) {
        if (player.getWarehouse().canSwitchDepots(depotID1,depotID2)) {
            player.getWarehouse().switchResource(depotID1,depotID2);
        }else{
            //if the game is corrupted, the game will end
            Game.getInstance().setEnded();
            Game.getInstance().setCorrupted();
        }
    }

}
