package it.polimi.ingsw.model.faith;

import it.polimi.ingsw.controller.Settings;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.view.cli.Color;
import it.polimi.ingsw.model.turn_taker.TurnTaker;

import java.util.*;
import java.util.List;

public class FaithTrack {
    private ArrayList<Cell> cells;
    private ArrayList<CellGroup> groups;
    private Map<String, Integer> markers;

    private static final ThreadLocal<FaithTrack> instance = ThreadLocal.withInitial(FaithTrack::new);
    /**
     * Initializes the game using appropriate settings
     */
    public FaithTrack(ArrayList<Cell> cells, ArrayList<CellGroup> groups) {
        this.cells = cells;
        this.groups = groups;
        markers = new HashMap<>();
    }

    private FaithTrack() {
        cells = Settings.getInstance().getCells();
        groups = Settings.getInstance().getGroups();
        markers = new HashMap<>();
    }

    public void setMarkers(Map<String, Integer> markers) {
        this.markers = markers;
    }

    /**
     * Gets the group of cells that contains the pope cell
     * @param popeCellID the special cell that contains additional victory points
     * @return the group of cells that contains the pope cell
     */
    public CellGroup getGroupByCell(int popeCellID) {
        return groups.stream()
                .filter(group -> group.contains(popeCellID))
                .findFirst().get();
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }

    public ArrayList<CellGroup> getGroups() {
        return groups;
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
                    .forEach(Cell -> player.addPersonalVictoryPoints(getGroupByCell(Cell.getCellID()).getTileVictoryPoints()));
            pastPath.stream()
                    .filter(Cell::isPopeCell)
                    .forEach(Cell::disablePopeCell);
        }
        List<Cell> playerPath = pastPath.subList(currentPosition, currentPosition+steps+1);
        playerPath.forEach(Cell -> player.addPersonalVictoryPoints(Cell.getCellVictoryPoints()));
    }

    /**
     * Moves the turnTaker on the faith track
     * @param turnTaker the turnTaker that wants to move
     * @param steps the amount of steps that the turnTaker wants to do
     */
    public void move(TurnTaker turnTaker, int steps){
        int nextPosition = markers.get(turnTaker.getUsername()) + steps;
        assignVictoryPoints(turnTaker, markers.get(turnTaker.getUsername()),steps);
        markers.replace(turnTaker.getUsername(), Math.min(nextPosition, cells.size()));
        if (nextPosition >= cells.size()){
            Game.getInstance().setEnded();
        }
    }
    public static FaithTrack getInstance() { return instance.get(); }

    public void addNewPlayer(TurnTaker player) {
        markers.put(player.getUsername(),0);
    }

    public int getPosition(TurnTaker player){
        return markers.get(player.getUsername());
    }

    public Cell getCell(int i){
        return cells.get(i);
    }

    public Map<String, Integer> getMarkers() {
        return markers;
    }

    /**
     * Print the faith track
     * @return the string to print faith track
     */
    @Override
    public String toString(){
        int count = 1;
        int pope = 0;
        StringBuilder print = new StringBuilder();
        print.append("Legend: Red for the Vatican Report Section that finish with: ┼,  Yellow for the victory points").append("\n");
        for (int i = 1 ; i < 25 ; i++){
            if (i % 3 == 0){
                print.append("| ").append(Color.ANSI_YELLOW).append("+ ").append(getCell(i).getCellVictoryPoints()).append(Color.RESET).append(" |");
            }
            if ( i % 8 == 0 && i != 24){
                print.append(Color.ANSI_GREEN).append(" ┼ ").append(Color.RESET);
                pope = 0;
            }
                else{
                    if (pope > 0 || ( (i-2) > 0 && ((i-2) % 3 == 0)) ) {
                        print.append("| ").append(Color.ANSI_RED).append(i).append(Color.RESET).append(" |");
                        pope++;
                    }
                    else
                        print.append("| ").append(i).append(Color.RESET).append(" |");
                }
        }
        print.append("\n");
        for (String p : getMarkers().keySet()) {
            print.append(convertColor(count)).append(p).append(Color.RESET).append(": ").append(getMarkers().get(p)).append("\n");
            count++;
        }
        return print.toString();
    }

    /**
     * Converts the color of a specific player
     * @param number the player
     * @return the color of the player
     */
    private StringBuilder convertColor(int number){
        switch (number){
            case 1:
                return new StringBuilder().append(Color.ANSI_RED);
            case 2:
                return new StringBuilder().append(Color.ANSI_GREEN);
            case 3:
                return new StringBuilder().append(Color.ANSI_BLUE);
            case 4:
                return new StringBuilder().append(Color.ANSI_YELLOW);
            default: return null;
        }
    }
}