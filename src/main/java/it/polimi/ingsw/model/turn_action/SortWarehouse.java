package it.polimi.ingsw.model.turn_action;

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
     * Switches the resources in depotID1 to the depotID2 and vice-versa
     * @param player player that wants to switch the depots
     */
    @Override
    public void play(Player player) {
        player.getWarehouse().switchResource(depotID1,depotID2);
    }

}
