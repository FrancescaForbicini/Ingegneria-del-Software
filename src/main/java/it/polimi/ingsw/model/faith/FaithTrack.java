package it.polimi.ingsw.model.faith;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class FaithTrack {
    private final ArrayList<Cell> path;
    private final Collection<GroupCell> groups;
    private Map<Player, Integer> markers;

    //private static ThreadLocal<FaithTrack> instance = ThreadLocal.withInitial(() -> new FaithTrack());

    /**
     * Initializes the game using appropriate settings
     */
    public FaithTrack(ArrayList<Cell> cells, ArrayList<GroupCell> groups) {
        path = cells;
        this.groups = groups;
        // TODO cells, groups, properly initialize with SETTINGS
    }

    /**
     * Returns the thread local singleton instance
     */
    /*
    public static FaithTrack getInstance() {
        return instance.get();
    }

     */

    private GroupCell getGroupByCell(Cell popeCell) {
        return groups.stream()
                .filter(groupCell -> groupCell.getCells().contains(popeCell))
                .findFirst().get();
    }
/*
    private void assignVictoryPoints(Player player, int nextPosition, int steps) {
        for (int i = 0; i < steps; i++, nextPosition--) {
            try {
                Cell passedCell = path.get(nextPosition);
                player.addPersonalVictoryPoints(passedCell.getCellVictoryPoints());
                if (!passedCell.isPopeCell())
                    break;
                GroupCell groupCell = getGroupByCell(passedCell);
                groupCell.assignTileVictoryPoints();
                passedCell.disablePopeCell();
            } catch (IndexOutOfBoundsException e) {
                continue;
            }
        }
    }

 */

    private void assignVictoryPoints(Player player,int currentPosition, int steps){
        List<Cell> pastPath = path.subList(0,currentPosition+steps+1);
        if(pastPath.stream().anyMatch(Cell::isPopeCell)){//assign points given by pope cells
            pastPath.stream()
                    .filter(Cell::isPopeCell)
                    .forEach(Cell -> getGroupByCell(Cell).assignTileVictoryPoints());
        }
        List<Cell> playerPath = pastPath.subList(currentPosition, currentPosition+steps+1);
        playerPath.stream().forEach(Cell -> player.addPersonalVictoryPoints(Cell.getCellVictoryPoints()));
    }
    public void move(Player player, int steps){
        int nextPosition = markers.get(player) + steps;
        //assignVictoryPoints(player, nextPosition, steps);
        assignVictoryPoints(player, markers.get(player),steps);
        markers.put(player, Math.max(nextPosition,path.size()));
        if (nextPosition >= path.size()){
            Game.getInstance().setEnded();
        }
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
