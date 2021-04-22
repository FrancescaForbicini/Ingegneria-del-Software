package it.polimi.ingsw.model.faith;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class FaithTrack {
    private List<Cell> path;
    private Collection<GroupCell> groups;
    private Map<Player, Integer> markers;

    private static ThreadLocal<FaithTrack> instance = ThreadLocal.withInitial(() -> new FaithTrack());

    /**
     * Initializes the game using appropriate settings
     */
    private FaithTrack() {
        // TODO cells, groups, properly initialize with SETTINGS
    }

    /**
     * Returns the thread local singleton instance
     */
    public static FaithTrack getInstance() {
        return instance.get();
    }
    private GroupCell getGroupByCell(Cell popeCell) {
        return groups.stream()
                .filter(groupCell -> groupCell.getCells().contains(popeCell))
                .findFirst().get();
    }

    private void assignVictoryPoints(Player player, int nextPosition, int steps) {
        for (int i = 0; i < steps; i++, nextPosition--) {
            try {
                Cell passedCell = path.get(nextPosition);
                player.addPersonalVictoryPoints(passedCell.getCellVictoryPoints());
                if (!passedCell.isPopeSpace())
                    break;
                GroupCell groupCell = getGroupByCell(passedCell);
                groupCell.assignTileVictoryPoints();
                passedCell.disablePopeCell();
            } catch (IndexOutOfBoundsException e) {
                continue;
            }
        }
    }

    public void move(Player player, int steps){
        int nextPosition = markers.get(player) + steps;
        if (nextPosition >= path.size()){
            Game.getInstance().setEnded();
        }
        assignVictoryPoints(player, nextPosition, steps);
        markers.put(player, nextPosition);
    }

    public void addNewPlayer(Player player) {
        markers.put(player,0);
    }

    public int getPosition(Player player){
        return markers.get(player);
    }

    public Cell getCell(int i){
        return path.get(i);
    }

    public Map<Player, Integer> getMarkers() {
        return markers;
    }
}
