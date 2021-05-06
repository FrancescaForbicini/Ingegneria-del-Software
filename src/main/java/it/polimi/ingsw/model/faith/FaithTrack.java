package it.polimi.ingsw.model.faith;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.*;

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
        markers = new HashMap<>();
    }

    public void setMarkers(Map<Player, Integer> markers) {
        this.markers = markers;
    }

    /**
     * Gets the group of cells that contains the pope cell
     * @param popeCell the special cell that contains additional victory points
     * @return the group of cells that contains the pope cell
     */
    private GroupCell getGroupByCell(Cell popeCell) {
        return groups.stream()
                .filter(groupCell -> groupCell.getCells().contains(popeCell))
                .findFirst().get();
    }

    /**
     * Assigns the victory points based on the position of the player on the faith track
     * @param player the player that it is in the faith track
     * @param currentPosition the current position of the player in the faith track
     * @param steps tha amount of steps that the player wants to do
     */
    public void assignVictoryPoints(Player player,int currentPosition, int steps){
        List<Cell> pastPath = path.subList(0,currentPosition+steps+1);
        if(pastPath.stream().anyMatch(Cell::isPopeCell)){//assign points given by pope cells
            pastPath.stream()
                    .filter(Cell::isPopeCell)
                    .forEach(Cell -> getGroupByCell(Cell).assignTileVictoryPoints(this));
        }
        List<Cell> playerPath = pastPath.subList(currentPosition, currentPosition+steps+1);
        playerPath.stream().forEach(Cell -> player.addPersonalVictoryPoints(Cell.getCellVictoryPoints()));
    }

    /**
     * Moves the player on the faith track
     * @param player the player that wants to move
     * @param steps the amount of steps that the player wants to do
     */
    public void move(Player player, int steps){
        int nextPosition = markers.get(player) + steps;
        assignVictoryPoints(player, markers.get(player),steps);
        markers.replace(player, Math.max(nextPosition,path.size()));
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