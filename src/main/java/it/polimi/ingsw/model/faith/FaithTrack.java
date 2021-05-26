package it.polimi.ingsw.model.faith;

import it.polimi.ingsw.controller.Settings;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.turn_taker.TurnTaker;

import java.util.*;

public class FaithTrack {
    private ArrayList<Cell> cells;
    private ArrayList<CellGroup> groups;
    private Map<TurnTaker, Integer> markers;

    private static final ThreadLocal<FaithTrack> instance = ThreadLocal.withInitial(() -> new FaithTrack());
    /**
     * Initializes the game using appropriate settings
     */
    public FaithTrack(ArrayList<Cell> cells, ArrayList<CellGroup> groups) {
        this.cells = cells;
        this.groups = groups;
        markers = new HashMap<>();
    }

    public FaithTrack() {
        cells = Settings.getInstance().getCells();
        groups = Settings.getInstance().getGroups();
        markers = new HashMap<>();
    }

        public void setMarkers(Map<TurnTaker, Integer> markers) {
        this.markers = markers;
    }

    /**
     * Gets the group of cells that contains the pope cell
     * @param popeCellID the special cell that contains additional victory points
     * @return the group of cells that contains the pope cell
     */
    private CellGroup getGroupByCell(int popeCellID) {
        return groups.stream()
                .filter(group -> group.contains(popeCellID))
                .findFirst().get();
    }

    /**
     * Assigns the victory points based on the position of the player on the faith track
     * @param player the player that it is in the faith track
     * @param currentPosition the current position of the player in the faith track
     * @param steps tha amount of steps that the player wants to do
     */
    public void assignVictoryPoints(TurnTaker player,int currentPosition, int steps){
        List<Cell> pastPath = cells.subList(0,currentPosition+steps+1);
        if(pastPath.stream().anyMatch(Cell::isPopeCell)){//assign points given by pope cells
            pastPath.stream()
                    .filter(Cell::isPopeCell)
                    .forEach(Cell -> assignTileVictoryPoints(getGroupByCell(Cell.getCellID())));
            pastPath.stream()
                    .filter(Cell::isPopeCell)
                    .forEach(Cell::disablePopeCell);
        }
        List<Cell> playerPath = pastPath.subList(currentPosition, currentPosition+steps+1);
        playerPath.forEach(Cell -> player.addPersonalVictoryPoints(Cell.getCellVictoryPoints()));
    }

    /**
     * Moves the player on the faith track
     * @param player the player that wants to move
     * @param steps the amount of steps that the player wants to do
     */
    public void move(TurnTaker player, int steps){
        int nextPosition = markers.get(player) + steps;
        assignVictoryPoints(player, markers.get(player),steps);
        markers.replace(player, Math.max(nextPosition, cells.size()));
        if (nextPosition >= cells.size()){
            Game.getInstance().setEnded();
        }
    }
    public static FaithTrack getInstance() {
        return instance.get();
    }


    public void addNewPlayer(TurnTaker player) {
        markers.put(player,0);
    }

    public int getPosition(TurnTaker player){
        return markers.get(player);
    }

    public Cell getCell(int i){
        return cells.get(i);
    }

    private void assignTileVictoryPoints(CellGroup cellGroup) {
        for(TurnTaker player : this.markers.keySet()){
            if(cellGroup.contains(markers.get(player))){
                player.addPersonalVictoryPoints(cellGroup.getTileVictoryPoints());
            }
        }
    }

    public Map<TurnTaker, Integer> getMarkers() {
        return markers;
    }
}