package it.polimi.ingsw.model.faith;

import it.polimi.ingsw.controller.Settings;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ThreadLocalCleanable;
import it.polimi.ingsw.model.turn_taker.Opponent;
import it.polimi.ingsw.model.turn_taker.TurnTaker;
import it.polimi.ingsw.view.cli.Color;
import it.polimi.ingsw.view.gui.custom_gui.Modifiable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representation of the Faith Track where players can obtain points or end the game (if they arrive at the end)
 */
public class FaithTrack implements ThreadLocalCleanable {
public class FaithTrack implements ThreadLocalCleanable, Modifiable {
    private final ArrayList<Cell> cells;
    private final ArrayList<CellGroup> groups;
    private Map<String, Integer> markers;


    private static ThreadLocal<FaithTrack> instance = ThreadLocal.withInitial(FaithTrack::load);

    /**
     * Initializes the game using appropriate settings
     */
    public FaithTrack(ArrayList<Cell> cells, ArrayList<CellGroup> groups) {
        this.cells = cells;
        this.groups = groups;
        this.markers = new HashMap<>();
    }

    private static FaithTrack load() {
        return new FaithTrack(
                Settings.getInstance().getCells(),
                Settings.getInstance().getGroups()
        );
    }

    public void setMarkers(Map<String, Integer> markers) {
        this.markers = markers;
    }

    /**
     * Gets the group of cells that contains the pope cell
     *
     * @param popeCellID the special cell that contains additional victory points
     * @return the group of cells that contains the pope cell
     */
    public CellGroup getGroupByCell(int popeCellID) {
        return groups.stream()
                .filter(group -> group.contains(popeCellID))
                .findFirst().get();
    }

    /**
     * Checks if a TurnTaker is present inside a certain CellGroup
     *
     * @param turnTaker to be checked
     * @param cellGroup to be checked
     * @return true iff the TurnTaker is inside the CellGroup
     */
    private boolean isTurnTakerOnGroup(TurnTaker turnTaker, CellGroup cellGroup) {
        int turnTakerPosition = markers.get(turnTaker.getUsername());
        return cellGroup.contains(turnTakerPosition);
    }

    /**
     * Assigns the victory points based on the position of the player on the faith track
     *
     * @param player the player that it is in the faith track
     * @param currentPosition the current position of the player in the faith track
     * @param steps tha amount of steps that the player wants to do
     */
    public void assignVictoryPoints(TurnTaker player,int currentPosition, int steps){
        List<Cell> pastPath = cells.subList(currentPosition + 1, Math.min(cells.size(), currentPosition + steps + 1));
        if(pastPath.stream().anyMatch(Cell::isPopeCell)) {
            pastPath.stream().filter(Cell::isPopeCell).forEach(cell -> {
                CellGroup groupCell = getGroupByCell(cell.getCellID());
                Game.getInstance().getTurnTakers().stream().filter(turnTaker -> isTurnTakerOnGroup(turnTaker, groupCell))
                        .forEach(p -> p.addPersonalVictoryPoints(groupCell.getTileVictoryPoints()));
                        cell.disablePopeCell();
                    }
            );
        }
        pastPath.forEach(cell -> player.addPersonalVictoryPoints(cell.getCellVictoryPoints()));
    }

    /**
     * Moves the turnTaker on the faith track
     *
     * @param turnTaker the turnTaker that wants to move
     * @param steps the amount of steps that the turnTaker wants to do
     */
    public void move(TurnTaker turnTaker, int steps){
        String faithId = turnTaker.getUsername();
        int nextPosition = markers.get(faithId) + steps;
        assignVictoryPoints(turnTaker, markers.get(faithId),steps);
        markers.replace(faithId, Math.min(nextPosition, cells.size() - 1));
        if (nextPosition >= cells.size()){
            if (turnTaker.equals(Opponent.getInstance()))
                Opponent.getInstance().setWinner();
            Game.getInstance().setEnded();
        }
    }

    /**
     * Moves the Opponent during a Solo Game
     *
     * @param steps to be taken by the Opponent
     */
    public void moveOpponent(int steps){
        move(Opponent.getInstance(), steps);
    }
    public static FaithTrack getInstance() { return instance.get(); }

    /**
     * Add a new Player to the Track, starting from cell 0
     *
     * @param player to be added
     */
    public void addNewPlayer(TurnTaker player) {
        markers.put(player.getUsername(),0);
    }

    /**
     * Gets the position of the Player inside the Track
     *
     * @param player whose position is asked
     * @return the position where the player is
     */
    public int getPosition(TurnTaker player){
        return markers.get(player.getUsername());
    }

    public Cell getCell(int i){
        return cells.get(i);
    }

    /**
     * Gets a mapping between each player and the position occupied
     *
     * @return mapping
     */
    public Map<String, Integer> getMarkers() {
        return markers;
    }

    @Override
    public String toString(){
        int count = 1;
        int pope = 0;
        StringBuilder print = new StringBuilder();
        print.append("Legend: Red for the Vatican Report Section that finish with: ┼,  Yellow for the victory points refers to the cell before").append("\n");
        for (int i = 1 ; i < 25 ; i++){
            if (cells.get(i).isPopeCell()){
                print.append(Color.ANSI_GREEN).append(" ┼ ").append(Color.RESET);
                pope = 0;
            }
            else{
                if (i == 5 || i == 12 || i == 19  || pope > 0) {
                    print.append("| ").append(Color.ANSI_RED).append(i).append(Color.RESET).append(" |");
                    pope++;
                }
                else
                    print.append("| ").append(i).append(Color.RESET).append(" |");
                if (cells.get(i).getCellVictoryPoints() > 0){
                    print.append("| ").append(Color.ANSI_YELLOW).append("+ ").append(getCell(i).getCellVictoryPoints()).append(Color.RESET).append(" |");
                }
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
     *
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void clean() {
        instance.remove();
    }
}